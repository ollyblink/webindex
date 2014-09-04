package rest.dao;

import index.girindex.IGIRIndex;
import index.girindex.combinationstrategy.CombinationStrategyFactory;
import index.girindex.combinationstrategy.ICombinationStrategy;
import index.girindex.combinationstrategy.combfamily.CombMNZ;
import index.girindex.implementations.GIRSeparatedIndexes;
import index.girindex.utils.GIRIndexType;
import index.spatialindex.implementations.ISpatialIndex;
import index.spatialindex.implementations.SpatialOnlyIndex;
import index.spatialindex.utils.GeometryConverter;
import index.spatialindex.utils.SpatialDocument;
import index.textindex.implementations.ITextIndex;
import index.textindex.implementations.RAMTextOnlyIndex;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocs;
import index.textindex.utils.informationextractiontools.GermanTextInformationExtractor;
import index.textindex.utils.informationextractiontools.ITextInformationExtractor;
import index.utils.Document;
import index.utils.GeometryWrapper;
import index.utils.Score;
import index.utils.identifers.TermDocsIdentifier;
import index.utils.indexmetadata.OverallTextIndexMetaData;
import index.utils.indexmetadata.TextIndexMetaData;
import index.utils.query.GIRQuery;
import index.utils.query.SpatialIndexQuery;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.dbconnection.AbstractDBConnector;
import utils.dbconnection.PGDBConnector;
import utils.dbcrud.DBDataManager;
import utils.dbcrud.DBTablesManager;

import com.vividsolutions.jts.geom.Geometry;

public enum IndexDao {
	INSTANCE;

	private final ITextInformationExtractor DEFAULT_TEXT_EXTRACTOR = new GermanTextInformationExtractor();
	private final GIRIndexType CURRENT_GIR_TYPE = GIRIndexType.SEPARATED;
	private final ICombinationStrategy DEFAULT_COMBINATION_STRATEGY = new CombMNZ();
	private IGIRIndex index;
	private DBDataManager dbDataManager;

	private IndexDao() {
		String host = "localhost";
		String port = "5432";
		String database = "girindex";
		String user = "postgres";
		String password = "32qjivkd";

		AbstractDBConnector db = new PGDBConnector(host, port, database, user, password);
		DBTablesManager dbManager = new DBTablesManager(db);
		this.dbDataManager = new DBDataManager(dbManager, DEFAULT_TEXT_EXTRACTOR, true);
		dbDataManager.initializeDBTables();
		this.index = getGIRIndex(GIRIndexType.SEPARATED);
	}

	private IGIRIndex getGIRIndex(GIRIndexType type) {
		switch (type) {
		case SEPARATED:
		default:
			ITextIndex textIndex = initializeTextIndex();
			ISpatialIndex spatialIndex = initializeSpatialIndex();
			ICombinationStrategy combinationStrategy = DEFAULT_COMBINATION_STRATEGY;
			IGIRIndex girIndex = new GIRSeparatedIndexes(textIndex, spatialIndex, combinationStrategy);
			return girIndex;
		}
	}

	private ISpatialIndex initializeSpatialIndex() {
		ISpatialIndex spatialIndex = new SpatialOnlyIndex();

		ArrayList<SpatialDocument> locations = dbDataManager.getLocations(null);
		for (SpatialDocument doc : locations) {
			spatialIndex.addDocument(doc);
		}

		return spatialIndex;
	}

	private ITextIndex initializeTextIndex() {
		ArrayList<Term> terms = dbDataManager.getTerms();
		ArrayList<Document> documents = dbDataManager.getDocuments(null);
		ArrayList<TermDocs> termDocs = dbDataManager.getTermDocs();
		OverallTextIndexMetaData overallTextIndexMetaData = dbDataManager.getOverallTextIndexMetaData();

		HashMap<Document, List<Term>> docAndTerms = new HashMap<>();

		for (Document document : documents) {
			List<Term> docTerms = new ArrayList<Term>();
			for (TermDocs termDoc : termDocs) {
				if (document.getId().getId().equals(termDoc.getId().getDocid())) {
					String termid = termDoc.getId().getTermid();
					for (int i = 0; i < terms.size(); ++i) {
						if (terms.get(i).getIndexedTerm().getTermId().equals(termid)) {
							docTerms.add(terms.get(i));
						}
					}
				}
			}
			docAndTerms.put(document, docTerms);
		}

		HashMap<TermDocsIdentifier, TermDocs> termDocsMeta = new HashMap<>();

		for (TermDocs t : termDocs) {
			termDocsMeta.put(t.getId(), t);
		}

		ITextIndex textIndex = new RAMTextOnlyIndex(new TextIndexMetaData(termDocsMeta, overallTextIndexMetaData), DEFAULT_TEXT_EXTRACTOR);
		textIndex.addDocuments(docAndTerms);

		return textIndex;
	}

	public void populateIndex() {
		this.index = getGIRIndex(CURRENT_GIR_TYPE);
	}

	public Ranking submitQuery(String textsimilaritytype, String spatialrelationship, String locationquery, String textquery, String textintersected, String textspatialintersected, String combinationstrategy) {
		Ranking restRanking = new Ranking();
		if (index != null) {
			boolean isTextIntersected = getIsIntersected(textintersected);
			boolean isTextSpatialIntersected = getIsIntersected(textspatialintersected);

			TextIndexQuery textQuery = null;
			if (textquery != null && textquery.trim().length() > 0) {
				textQuery = new TextIndexQuery(textquery, textsimilaritytype, isTextIntersected);
			}
			SpatialIndexQuery spatialQuery = null;
			if (locationquery != null && locationquery.trim().length() > 0) {
				spatialQuery = new SpatialIndexQuery(spatialrelationship, locationquery);
			} 
			// query index
			if (textQuery != null && spatialQuery != null) {// GIR
				GIRQuery girQuery = new GIRQuery(isTextSpatialIntersected, textQuery, spatialQuery);
				index.setCombinationStrategy(CombinationStrategyFactory.create(combinationstrategy));
				restRanking = index.queryIndex(girQuery);
				addCoordinates(restRanking);
			} else if (textQuery == null && spatialQuery != null) {// Spatial only
				restRanking = index.queryIndex(spatialQuery);
			} else if (textQuery != null && spatialQuery == null) {// Text only
				restRanking = index.queryIndex(textQuery);
				addCoordinates(restRanking);
			}

		}
		return restRanking;
	}

	/**
	 * This method solely adds coordinates to those documents retrieved by only text index. Takes the first one if multiple exist.
	 * 
	 * @param ranking
	 */
	private void addCoordinates(Ranking ranking) {
		RESTTextQueryMetaData textQueryMetaData = ranking.getTextQueryMetaData();
		if (textQueryMetaData != null) {
			ArrayList<RESTScore> originalTermScores = textQueryMetaData.getScores();
			for (RESTScore score : originalTermScores) {
				if (score.getGeometry() == null) {
					List<Long> docids = new ArrayList<>();
					docids.add(score.getDocument().getId().getId());
					ArrayList<SpatialDocument> locations = dbDataManager.getLocations(docids);
					if (locations != null && locations.size() > 0) {
						score.setGeometry(GeometryConverter.convertJTStoRESTGeometry(locations.get(0).getDocumentFootprint()));
					}
				}
			}
		}
	}

	private boolean getIsIntersected(String isIntersected) {
		switch (isIntersected.trim()) {
		case "intersection":
			return true;
		case "union":
		default:
			return false;
		}
	}

	public void dropAndInitializeTables() {
		dbDataManager.dropTables();
		dbDataManager.initializeDBTables();
	}

}
