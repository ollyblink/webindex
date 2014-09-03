package utils.dbcrud;

import index.spatialindex.utils.GeometryConverter;
import index.spatialindex.utils.SpatialDocument;
import index.textindex.implementations.ITextIndexNoInsertion;
import index.textindex.implementations.RAMTextOnlyIndex;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocs;
import index.textindex.utils.informationextractiontools.ITextInformationExtractor;
import index.textindex.utils.informationextractiontools.MockTextInformationExtractor;
import index.utils.Document;
import index.utils.documenttransformation.ExtractionRequest;
import index.utils.documenttransformation.spatialtransformation.GeoReferencingStage;
import index.utils.documenttransformation.spatialtransformation.GeoTaggingStage;
import index.utils.documenttransformation.texttransformation.IndexAndOriginalTokenExtractionStage;
import index.utils.documenttransformation.texttransformation.StemmingStage;
import index.utils.documenttransformation.texttransformation.StopwordRemovalStage;
import index.utils.documenttransformation.texttransformation.TermFrequencyExtractionStage;
import index.utils.documenttransformation.texttransformation.TokenizationStage;
import index.utils.identifers.TermDocsIdentifier;
import index.utils.indexmetadata.OverallTextIndexMetaData;
import index.utils.indexmetadata.TextIndexMetaData;

import java.rmi.NoSuchObjectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.postgis.PGgeometry;

import testutils.DBInitializer;
import utils.dbconnection.AbstractDBConnector;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Implements all the database indexing and access methods used by the text and spatial indexes. Does the whole logic of the database. Uses a DBTablesManager to initialise and drop tables.
 * 
 * @author rsp
 *
 */
public class DBDataManager implements IDataManager {

	private DBTablesManager dbTablesManager;
	private BlockingQueue<String> documentQueue;
	private ITextInformationExtractor tokenizer;
	private boolean showTransformations;
	private boolean isRunning;

	public DBDataManager(DBTablesManager dbManager, ITextInformationExtractor tokenizer, boolean showTransformations) {
		this.dbTablesManager = dbManager;
		this.tokenizer = tokenizer;
		this.showTransformations = showTransformations;
		this.isRunning = true;
	}

	@Override
	public void addDocuments(final List<String> texts) {

		for (String text : texts) {
			addDocument(text);
		}
		updateTermNi();
		updateMetadataA();
		updateTermIdfs();
		updateTermOccursInDocumentTFIDFs();
		updateDocumentVectorNorms();
		updateMetadataB();
	}

	@Override
	public void addDocumentDeferred(String pureText) {
		try {
			documentQueue.put(pureText);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * Text only retrieval
	 */

	@Override
	public ArrayList<TermDocs> getTermDocs() {
		ArrayList<TermDocs> termDocs = new ArrayList<>();
		String sql = "select * from term_docs";
		try {
			PreparedStatement statement = dbTablesManager.getConnection().prepareStatement(sql);
			ResultSet r = statement.executeQuery();

			while (r.next()) {
				TermDocs td = new TermDocs(r.getString(1), r.getLong(2), r.getInt(3), r.getFloat(4), r.getFloat(5), r.getFloat(6), r.getFloat(7), r.getFloat(8));
				termDocs.add(td);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return termDocs;
	}

	@Override
	public ArrayList<Term> getTerms() {
		HashMap<Term, ArrayList<String>> originalTerms = new HashMap<>();
		String sql = "select * from terms inner join original_terms on id=termid";
		try {
			PreparedStatement statement = dbTablesManager.getConnection().prepareStatement(sql);
			ResultSet r = statement.executeQuery();
			while (r.next()) {
				String termid = r.getString(1);
				Term term = new Term(termid);
				ArrayList<String> oTs = originalTerms.get(term);
				if (oTs == null) {
					oTs = new ArrayList<>();
					term = new Term(termid, oTs, r.getInt(2), r.getFloat(3), r.getFloat(4));
					originalTerms.put(term, oTs);
				}
				oTs.add(r.getString(5));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>(originalTerms.keySet());
	}

	@Override
	public ArrayList<Document> getDocuments(List<Long> docids) {

		ArrayList<Document> documents = new ArrayList<>();
		String sql = "select * from documents d ";
		if (docids != null && docids.size() > 0) {
			sql += getWhereClause("d", "id", docids);
		}
		try {
			PreparedStatement statement = dbTablesManager.getConnection().prepareStatement(sql);
			ResultSet r = statement.executeQuery();
			while (r.next()) {
				Document d = new Document(r.getLong(1), r.getString(2), r.getInt(3), r.getInt(4), r.getInt(5), r.getFloat(6), r.getFloat(7), r.getFloat(8));
				documents.add(d);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return documents;
	}

	@Override
	public OverallTextIndexMetaData getOverallTextIndexMetaData() {
		String sql = "select * from metadata";
		OverallTextIndexMetaData metaData = null;
		try {
			PreparedStatement statement = dbTablesManager.getConnection().prepareStatement(sql);
			ResultSet r = statement.executeQuery();
			while (r.next()) {
				metaData = new OverallTextIndexMetaData(r.getFloat(2), r.getFloat(3), r.getFloat(4), r.getFloat(5), r.getFloat(6), r.getFloat(7), r.getInt(8));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return metaData;
	}

	@Override
	public void close() {
		isRunning = false;
		dbTablesManager.closeConnection();
	}

	@Override
	public boolean isDocumentQueueEmptyEmpty() {
		return documentQueue.isEmpty();
	}

	@SuppressWarnings("unchecked")
	protected void addDocument(final String text) {
		String transformedText = text.replace("´", "''").replace("`", "''").replace("'", "''");
		ExtractionRequest request = new ExtractionRequest(text);
		// Terms
		StopwordRemovalStage stopwordRemovalStage = new StopwordRemovalStage(showTransformations, tokenizer);
		StemmingStage stemmingStage = new StemmingStage(showTransformations, tokenizer);
		TokenizationStage tokenizationStage = new TokenizationStage(showTransformations, tokenizer);
		IndexAndOriginalTokenExtractionStage indexAndOriginalTokenExtractionStage = new IndexAndOriginalTokenExtractionStage(showTransformations, tokenizer);
		TermFrequencyExtractionStage termFrequencyExtractionStage = new TermFrequencyExtractionStage(showTransformations, tokenizer);

		// Spatial
		GeoTaggingStage geoTaggingStage = new GeoTaggingStage(showTransformations);
		GeoReferencingStage geoReferencingStage = new GeoReferencingStage(showTransformations);

		// Chaining the stages together in an appropriate way

		// spatial extraction
		geoTaggingStage.setSuccessor(geoReferencingStage);
		geoReferencingStage.setPrecursor(geoTaggingStage);
		geoTaggingStage.handleRequest(request);

		// Check if there were any locations found.
		// If not: skip this document...
		List<Geometry> possibleLocations = (List<Geometry>) request.getTransformationStage(geoReferencingStage.getClass().getSimpleName());
		if (possibleLocations == null || possibleLocations.size() == 0) {
			System.out.println("No locations found. Document \"" + transformedText + "\" could not be georeferenced. Discarded.");
			// return;
		}
		// text extraction
		stopwordRemovalStage.setSuccessor(stemmingStage);
		stemmingStage.setPrecursor(stopwordRemovalStage);
		stemmingStage.setSuccessor(tokenizationStage);
		tokenizationStage.setPrecursor(stemmingStage);
		tokenizationStage.setSuccessor(indexAndOriginalTokenExtractionStage);
		indexAndOriginalTokenExtractionStage.setPrecursor(stopwordRemovalStage); // so that only the relevant terms are analysed
		indexAndOriginalTokenExtractionStage.setSuccessor(termFrequencyExtractionStage);
		termFrequencyExtractionStage.setPrecursor(indexAndOriginalTokenExtractionStage);
		stopwordRemovalStage.handleRequest(request);

		HashMap<Term, Integer> termFreqs = (HashMap<Term, Integer>) request.getTransformationStage(termFrequencyExtractionStage.getClass().getSimpleName());
		List<Geometry> geometries = (List<Geometry>) request.getTransformationStage(geoReferencingStage.getClass().getSimpleName());

		try {
			PreparedStatement termInsertion = dbTablesManager.getConnection().prepareStatement("insert into terms(id) values(?);");
			PreparedStatement originalTermInsertion = dbTablesManager.getConnection().prepareStatement("insert into original_terms(original_term, termid) values(?,?);");
			PreparedStatement documentInsertion = dbTablesManager.getConnection().prepareStatement("insert into documents(fulltext,size_in_bytes,raw_nr_of_words,indexed_nr_of_terms) values(?,?,?,?);");
			PreparedStatement termInDocInsertion = dbTablesManager.getConnection().prepareStatement("insert into term_docs(termid, docid, fij, doc_tf1, doc_tf2_3) values (?,?,?,?,?)");

			// Start by inserting the actual document
			insertDocument(transformedText, new ArrayList<>(termFreqs.keySet()), documentInsertion);

			// Now the terms and term-to-doc relationships
			for (Term indexTerm : termFreqs.keySet()) {
				insertTerm(termInsertion, indexTerm);
				insertOriginalTerms(originalTermInsertion, indexTerm);
				insertTermDocs(transformedText, termFreqs, termInDocInsertion, indexTerm);
			}

			// Last but not least, insert all the document's spatial footprints.
			if (geometries != null) {
				List<SpatialDocument> dFPs = new ArrayList<>(geometries.size());
				for (Geometry geometry : geometries) {
					Long docid = getDocumentId(transformedText);
					List<Long> docids = new ArrayList<>();
					docids.add(docid);
					dFPs.add(new SpatialDocument(getDocuments(docids).get(0), geometry));
				}

				insertLocation(dFPs);
			}

			// Done, the document is now completely indexed in the database.

		} catch (SQLException | NoSuchObjectException e) {
			e.printStackTrace();
		}
	}

	private Long getDocumentId(String text) {
		String sql = "select id from documents where fulltext like '%" + text.replace("''", "%") + "%';";
		long id = -1l;
		try {
			Statement s = getConnection().createStatement();
			ResultSet set = s.executeQuery(sql);
			while (set.next()) {
				id = set.getLong(1);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * Used by spatial indexes
	 * 
	 * @param dFPs
	 * @throws SQLException
	 * @throws NoSuchObjectException
	 */
	protected void insertLocation(List<SpatialDocument> dFPs) throws SQLException, NoSuchObjectException {
		if (dFPs == null) {
			return;
		} else {
			if (dbTablesManager.getConnector().tableExists("locations")) {
				String sql = "Insert into locations(docid, geometry) values (?,?);";
				PreparedStatement statement = dbTablesManager.getConnection().prepareStatement(sql);
				for (SpatialDocument dFP : dFPs) {
					if (!alreadyIndexed("select count(*) from locations where docid=" + dFP.getDocument().getId().getId() + " AND geometry=ST_GEOMETRYFROMTEXT('" + GeometryConverter.convertJTStoPostGis(dFP.getDocumentFootprint()) + "');")) {
						statement.setLong(1, dFP.getDocument().getId().getId());
						statement.setObject(2, GeometryConverter.convertJTStoPostGis(dFP.getDocumentFootprint()));
						statement.executeUpdate();
					}
				}
			} else {
				System.err.println("IndexDocumentProvider::storePersistently::could not insert, \"locations\" table does not exist.");
			}
		}
	}

	protected void updateDocumentVectorNorms() {
		String sql = "Update documents d set  doc_vectornorm1 = (select sqrt(sum(power(doc_term_tfidf1,2))) from term_docs where d.id=docid )," + "doc_vectornorm2 = (select sqrt(sum(power(doc_term_tfidf2,2))) from term_docs where d.id=docid )," + "doc_vectornorm3 = (select sqrt(sum(power(doc_term_tfidf3,2))) from term_docs where d.id=docid )"

		+ ";";
		// System.out.println(sql);
		executeSimpleUpdate(sql);
	}

	protected void updateTermOccursInDocumentTFIDFs() {
		String sql = "update term_docs td set doc_term_tfidf1 =  t.term_idf1*doc_tf1, doc_term_tfidf2 = doc_tf2_3, doc_term_tfidf3 = doc_tf2_3*(t.term_idf1) from terms t where td.termid = t.id;";
		executeSimpleUpdate(sql);
	}

	private void executeSimpleUpdate(String... sqls) {
		try {
			Statement s = dbTablesManager.getConnection().createStatement();
			for (String sql : sqls) {
				s.execute(sql);
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int getN() throws SQLException {
		int N = 0;
		String getN = "Select N from metadata where id=1";
		Statement s = dbTablesManager.getConnection().createStatement();
		ResultSet nRes = s.executeQuery(getN);
		while (nRes.next()) {
			N = nRes.getInt(1);
		}
		s.close();
		return N;
	}

	protected void updateTermIdfs() {
		try {
			Double N = new Double(getN());
			Statement s = dbTablesManager.getConnection().createStatement();
			String updateIDFs = "Update terms set term_idf1 = log(2, (" + N + "/ni)), term_idf2 = log(2,(1+(" + N + "/ni)));";
			s.execute(updateIDFs);
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void insertTermDocs(String transformedText, Map<Term, Integer> termFreqs, PreparedStatement termInDocInsertion, Term indexTerm) throws SQLException {
		Statement statement = dbTablesManager.getConnection().createStatement();
		ResultSet lastDocId = statement.executeQuery("select id from documents where fulltext like '%" + transformedText.replace("''", "%") + "%';");
		// System.out.println("select id from documents where fulltext like '%" + transformedText.replace("''","%") + "%';");
		long docId = 0;
		while (lastDocId.next()) {
			docId = lastDocId.getLong(1);
		}
		statement.close();

		int freq = termFreqs.get(indexTerm);
		if (!alreadyIndexed("select count(*) from term_docs where termid = '" + indexTerm.getIndexedTerm().getTermId() + "' AND docid='" + docId + "';")) {
			termInDocInsertion.setString(1, indexTerm.getIndexedTerm().getTermId());
			termInDocInsertion.setLong(2, docId);
			termInDocInsertion.setInt(3, freq);
			termInDocInsertion.setFloat(4, freq);
			termInDocInsertion.setFloat(5, (float) (1 + (Math.log(freq) / Math.log(2))));
			termInDocInsertion.execute();
		}
	}

	protected void insertOriginalTerms(PreparedStatement originalTermInsertion, Term indexTerm) throws SQLException {
		ArrayList<String> originalTerms = indexTerm.getOriginalTerms();
		for (String originalTerm : originalTerms) {
			if (!alreadyIndexed("select count(original_term) from original_terms where original_term='" + originalTerm + "';")) {
				originalTermInsertion.setString(1, originalTerm);
				originalTermInsertion.setString(2, indexTerm.getIndexedTerm().getTermId());
				originalTermInsertion.execute();
			}
		}
	}

	protected void insertTerm(PreparedStatement termInsertion, Term indexTerm) throws SQLException {
		if (!alreadyIndexed("select count(id) from terms where id = '" + indexTerm.getIndexedTerm() + "';")) {
			termInsertion.setString(1, indexTerm.getIndexedTerm().getTermId());
			termInsertion.execute();
		}
	}

	private void insertDocument(final String text, ArrayList<Term> indexTerms, PreparedStatement documentInsertion) throws SQLException {
		// System.out.println("select count(id) from documents where fulltext like '%" + text + "%';");
		if (!alreadyIndexed("select count(id) from documents where fulltext like '%" + text + "%';")) {
			documentInsertion.setString(1, text);
			documentInsertion.setInt(2, getSizeInBytes(text));
			documentInsertion.setInt(3, text.toLowerCase().replace(".", " ").replace(",", " ").replace(";", " ").toLowerCase().split("\\s+").length);
			documentInsertion.setInt(4, indexTerms.size());
			documentInsertion.execute();
		}
	}

	private int getSizeInBytes(String text) {
		try {
			return text.getBytes("UTF-8").length;
		} catch (Exception e) {
			return text.getBytes().length;
		}
	}

	/**
	 * updates ni of a term
	 * 
	 * @param indexTerm
	 * @throws SQLException
	 */
	protected void updateTermNi() {
		try {
			Statement statement;
			statement = dbTablesManager.getConnection().createStatement();

			String updateNSql = "Update terms t set ni=(select count(*)from terms termsCounter inner join term_docs td on t.id = td.termid inner join documents d on d.id = td.docid where termsCounter.id = t.id);";

			statement.executeUpdate(updateNSql);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateMetadataA() {
		try {
			PreparedStatement metadataSql = null;
			String avgDocLengthSizeInBytesSQL = "(select avg(size_in_bytes) from documents)";
			String avgDocLengthRawNrOfWords = "(select avg(raw_nr_of_words) from documents)";
			String avgDocLengthIndexedNrOfTerms = "(select avg(indexed_nr_of_terms) from documents)";

			String N = "(select count(id) from documents)";
			if (!alreadyIndexed("select count(*) from metadata;")) {
				metadataSql = dbTablesManager.getConnection().prepareStatement("insert into metadata(" + "avg_doc_length_size_in_bytes, " + "avg_doc_length_raw_nr_of_words," + "avg_doc_length_indexed_nr_of_words," + "N" + ") values(" + avgDocLengthSizeInBytesSQL + "," + avgDocLengthRawNrOfWords + "," + avgDocLengthIndexedNrOfTerms + ", " + N + ");");

			} else {
				metadataSql = dbTablesManager.getConnection().prepareStatement("update metadata set " + "avg_doc_length_size_in_bytes = " + avgDocLengthSizeInBytesSQL + ", " + "avg_doc_length_raw_nr_of_words = " + avgDocLengthRawNrOfWords + "," + "avg_doc_length_indexed_nr_of_words = " + avgDocLengthIndexedNrOfTerms + "," + "N = " + N + " where id=1");

			}

			// System.err.println(metadataSql.toString());
			metadataSql.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void updateMetadataB() {
		try {
			String docVectorNorm1 = "(select avg(doc_vectornorm1) from documents)";
			String docVectorNorm2 = "(select avg(doc_vectornorm2) from documents)";
			String docVectorNorm3 = "(select avg(doc_vectornorm3) from documents)";

			PreparedStatement metadataSql = dbTablesManager.getConnection().prepareStatement("update metadata set " + "avg_doc_length_vectornorm1 = " + docVectorNorm1 + "," + "avg_doc_length_vectornorm2 = " + docVectorNorm2 + "," + "avg_doc_length_vectornorm3 = " + docVectorNorm3);
			metadataSql.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean alreadyIndexed(String sql) {
		try {
			Statement statement = dbTablesManager.getConnection().createStatement();
			ResultSet lastDocId = statement.executeQuery(sql);
			int count = 0;
			while (lastDocId.next()) {
				count = lastDocId.getInt(1);
			}
			statement.close();
			return count > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public HashMap<Long, Integer> getIndexedNrOfTermsInDocuments(List<Long> docids) {
		String sql = "select id, indexed_nr_of_terms from documents where ";

		for (int i = 0; i < docids.size() - 1; ++i) {
			sql += "id = " + docids.get(i) + " OR ";
		}
		if (docids.size() > 0) {
			sql += "id = " + docids.get(docids.size() - 1) + ";";
		}

		HashMap<Long, Integer> results = new HashMap<Long, Integer>();

		try {
			Statement statement = dbTablesManager.getConnection().createStatement();
			ResultSet set = statement.executeQuery(sql);

			while (set.next()) {
				results.put(set.getLong(1), set.getInt(2));
			}
			statement.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return results;
	}

	/*
	 * END Text only
	 */

	/*
	 * Spatial only
	 */

	// private String getWhereClause(List<Score> dFPs) {
	//
	// if (dFPs == null || dFPs.size() == 0) {
	// return ";";
	// }
	// int counter = 0;
	// String whereClause = "where ";
	// for (Score dFP : dFPs) {
	// if (++counter == dFPs.size()) {
	// whereClause += "d.id=" + dFP.getDocument().getId().getId() + ";";
	// } else {
	// whereClause += "d.id=" + dFP.getDocument().getId().getId() + " OR ";
	// }
	// }
	// return whereClause;
	// }

	private String getWhereClause(String tableName, String colName, List<Long> ids) {
		if (ids == null || ids.size() == 0) {
			return ";";
		}
		int counter = 0;
		String whereClause = " where ";
		for (Long id : ids) {
			if (++counter == ids.size()) {
				whereClause += tableName + "." + colName + "= " + id + ";";
			} else {
				whereClause += tableName + "." + colName + "=" + id + " OR ";
			}
		}
		return whereClause;
	}

	@Override
	public ArrayList<SpatialDocument> getLocations(List<Long> docids) {
		if (dbTablesManager.getConnector().tableExists("locations")) {

			try {
				String sql = "Select l.docid, l.geometry from locations l " + getWhereClause("l", "docid",docids);
				// System.out.println(sql);
				return retrieveDocuments(sql);
			} catch (SQLException | NoSuchObjectException e) {
				e.printStackTrace();
				return new ArrayList<>();
			}
		} else {
			System.err.println("IndexDocumentProvider::getSpatialDocuments::could not retrieve, \"locations\" table does not exist.");
			return new ArrayList<>();
		}
	}

	private ArrayList<SpatialDocument> retrieveDocuments(String sql) throws NoSuchObjectException, SQLException {
		ArrayList<SpatialDocument> spatialDocuments = new ArrayList<>();

		Statement statement = dbTablesManager.getConnection().createStatement();
		ResultSet set = statement.executeQuery(sql);
		while (set.next()) {
			Long id = set.getLong(1);
			List<Long> ids = new ArrayList<>();
			ids.add(id);
			Geometry documentFootprint = GeometryConverter.convertPostGisToJTS((PGgeometry) set.getObject(2));
			SpatialDocument spatialDocument = new SpatialDocument(getDocuments(ids).get(0), documentFootprint);
			spatialDocuments.add(spatialDocument);
		}
		statement.close();

		return spatialDocuments;
	}

	//

	/*
	 * End spatial only retrieval
	 */

	/*
	 * Some service methods to abstract the DBTablesManager
	 */
	public boolean initializeDBTables() {
		return dbTablesManager.initializeDBTables();
	}

	public void dropTables() {
		dbTablesManager.dropTables();
	}

	public Connection getConnection() {
		return dbTablesManager.getConnection();
	}

	public void closeConnection() {
		dbTablesManager.closeConnection();
	}

	public AbstractDBConnector getConnector() {
		return dbTablesManager.getConnector();
	}

	public static HashMap<Term, List<Document>> createIndexableDocuments() {
		String[] d = { "To do is to be. To be is to do.", "To be or not to be. I am what I am.", "I think therefore I am. Do be do be do.", "Do do do, da da da. Let it be, let it be." };

		DBDataManager dbDataManager = DBInitializer.initTestTextDB(new MockTextInformationExtractor(), DBInitializer.initDB(), d);
		ArrayList<Term> terms = dbDataManager.getTerms();
		ArrayList<Document> documents = dbDataManager.getDocuments(null);
		ArrayList<TermDocs> termDocs = dbDataManager.getTermDocs();

		HashMap<Term, List<Document>> converted = new HashMap<>();
		for (Term term : terms) {
			List<Document> docs = converted.get(term.getIndexedTerm());
			if (docs == null) {
				docs = new ArrayList<Document>();
				converted.put(term, docs);
			}

			List<Document> toFind = new ArrayList<>();
			for (TermDocs tds : termDocs) {
				if (tds.getId().getTermid().equals(term.getIndexedTerm().getTermId())) {
					toFind.add(new Document(tds.getId().getDocid()));
				}
			}
			for (Document find : toFind) {
				for (Document doc : documents) {
					if (find.equals(doc)) {
						converted.get(term).add(doc);
					}
				}
			}

		}
		return converted;
	}

	public static ITextIndexNoInsertion getTestIndex() {
		DBDataManager dbDataManager = new DBDataManager(DBInitializer.initDB(), null, false);

		HashMap<Term, List<Document>> documents = DBDataManager.createIndexableDocuments();
		ArrayList<TermDocs> termDocs = dbDataManager.getTermDocs();
		HashMap<TermDocsIdentifier, TermDocs> termDocsMeta = new HashMap<>();

		for (TermDocs t : termDocs) {
			termDocsMeta.put(t.getId(), t);
		}
		RAMTextOnlyIndex ramTextOnlyIndex = new RAMTextOnlyIndex(new TextIndexMetaData(termDocsMeta, dbDataManager.getOverallTextIndexMetaData()), null);
		ramTextOnlyIndex.addTerms(documents);
		return ramTextOnlyIndex;
	}

	@Override
	public Map<Document, List<Term>> getDocsAndTerms() {
		Map<Term, List<Document>> termDocs = getTermAndDocOccurrences();
		Map<Document, List<Term>> docTerms = new HashMap<Document, List<Term>>();

		for (Term term : termDocs.keySet()) {
			List<Document> docs = termDocs.get(term);
			for (Document doc : docs) {
				List<Term> list = docTerms.get(doc);
				if (list == null) {
					list = new ArrayList<>();
					docTerms.put(doc, list);
				}
				list.add(term);
			}
		}
		return docTerms;
	}

	@Override
	public Map<Term, List<Document>> getTermAndDocOccurrences() {
		ArrayList<Term> terms = getTerms();
		ArrayList<Document> documents = getDocuments(null);
		ArrayList<TermDocs> termDocs = getTermDocs();

		HashMap<Term, List<Document>> converted = new HashMap<>();
		for (Term term : terms) {
			List<Document> docs = converted.get(term.getIndexedTerm());
			if (docs == null) {
				docs = new ArrayList<Document>();
				converted.put(term, docs);
			}

			List<Document> toFind = new ArrayList<>();
			for (TermDocs tds : termDocs) {
				if (tds.getId().getTermid().equals(term.getIndexedTerm().getTermId())) {
					toFind.add(new Document(tds.getId().getDocid()));
				}
			}
			for (Document find : toFind) {
				for (Document doc : documents) {
					if (find.equals(doc)) {
						converted.get(term).add(doc);
					}
				}
			}

		}
		return converted;
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public void setDocumentQueue(BlockingQueue<String> documentQueue2) {
		this.documentQueue = documentQueue2;
	}

	@Override
	public DBTablesManager getDBTablesManager() {
		return dbTablesManager;
	}

}
