package index.girindex.implementations;

import index.girindex.combinationstrategy.combfamily.CombMNZ;
import index.spatialindex.implementations.SpatialOnlyIndex;
import index.textindex.implementations.RAMTextOnlyIndex;
import index.textindex.utils.TermDocs;
import index.textindex.utils.informationextractiontools.MockTextInformationExtractor;
import index.utils.Ranking;
import index.utils.Score;
import index.utils.identifers.TermDocsIdentifier;
import index.utils.indexmetadata.TextIndexMetaData;
import index.utils.query.GIRQuery;
import index.utils.query.SpatialIndexQuery;
import index.utils.query.TextIndexQuery;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testutils.DBInitializer;
import utils.dbcrud.DBDataManager;
import utils.dbcrud.DBTablesManager;

public class SeparatedGIRIndexTest {

	private static SeparatedGIRIndex girIndex;
	private static DBTablesManager dbTablesManager;
	private static DBDataManager dbDataManager;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String[] docs = new String[] { "This text is about Zürich, Schweiz", "The mayor of London was not amused", "Many people died in World War II when Berlin was bombed by the allies" };

		dbTablesManager = DBInitializer.initDB();
		dbDataManager = DBInitializer.initTestTextDB(new MockTextInformationExtractor(), dbTablesManager, docs);

		HashMap<TermDocsIdentifier, TermDocs> termDocsMap = new HashMap<TermDocsIdentifier, TermDocs>();
		for (TermDocs td : dbDataManager.getTermDocs()) {
			termDocsMap.put(td.getId(), td);
		}
		RAMTextOnlyIndex textIndex = new RAMTextOnlyIndex(new TextIndexMetaData(termDocsMap, dbDataManager.getOverallTextIndexMetaData()), new MockTextInformationExtractor());
		textIndex.addDocuments(dbDataManager.getDocsAndTerms());
		SpatialOnlyIndex spatialIndex = new SpatialOnlyIndex();
		spatialIndex.addDocuments(dbDataManager.getLocations(null));

		girIndex = new SeparatedGIRIndex(textIndex, spatialIndex, new CombMNZ());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DBInitializer.tearDownTestDB(dbTablesManager);
	}

	@Test
	public void testTextAndInRelation() {
		TextIndexQuery textQuery = new TextIndexQuery("text", "cosine3", false);
		SpatialIndexQuery spatialQuery = new SpatialIndexQuery("point_in", "Switzerland");
		GIRQuery girQuery = new GIRQuery(true, textQuery, spatialQuery);
		Ranking hits = girIndex.queryIndex(girQuery);
		System.out.println(hits.getResults().size());
		System.out.println(hits.getRankingMetaData());
		for (Score s : hits) {
			System.out.println(s.getDocument().getId().getId() + ": " + s.getScore());
		}
	}

	@Test
	public void testTextAndOverlapsRelation() {
		TextIndexQuery textQuery = new TextIndexQuery("text", "cosine3", false);
		SpatialIndexQuery spatialQuery = new SpatialIndexQuery("overlaps", "Canton of Zurich");
		GIRQuery girQuery = new GIRQuery(true, textQuery, spatialQuery);
		Ranking hits = girIndex.queryIndex(girQuery);
		System.out.println(hits.getResults().size());
		System.out.println(hits.getRankingMetaData());
		for (Score s : hits) {
			System.out.println(s.getDocument().getId().getId() + ": " + s.getScore());
		}
	}
}
