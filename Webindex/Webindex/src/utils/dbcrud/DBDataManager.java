package utils.dbcrud;

import index.spatialindex.utils.GeometryConverter;
import index.spatialindex.utils.Location;
import index.textindex.implementations.ITextIndex;
import index.textindex.implementations.RAMTextOnlyIndex;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocs;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.textindex.utils.texttransformation.MockTextTokenizer;
import index.utils.Document;
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.postgis.PGgeometry;

import testutils.DBInitializer;
import utils.dbconnection.AbstractDBConnector;

/**
 * Implements all the database indexing and access methods used by the text and spatial indexes. Does the whole logic of the database. Uses a
 * DBTablesManager to initialise and drop tables.
 * 
 * @author rsp
 *
 */
public class DBDataManager implements IDataProvider {

	private DBTablesManager dbTablesManager;
	private BlockingQueue<String> documentQueue;
	private DocumentConsumer consumer;
	private boolean indexIsRunning;
	private ITextTokenizer tokenizer;

	private final class DocumentConsumer implements Runnable {

		boolean isUpdated;
		private BlockingQueue<String> documentQueue;
		private DBDataManager dbDataProvider;

		public DocumentConsumer(DBDataManager dbDataProvider, BlockingQueue<String> documentQueue) {
			this.dbDataProvider = dbDataProvider;
			this.documentQueue = documentQueue;
			this.isUpdated = true;
		}

		@Override
		public void run() {
			while (indexIsRunning) {
				if (!documentQueue.isEmpty()) {
					try {
						String documentToIndex = documentQueue.take();
						if (documentQueue.size() % 50 == 0) {
							System.out.println("indexing document. " + documentQueue.size() + " documents left to index.");
						}
						dbDataProvider.addDocument(documentToIndex);
						isUpdated = false;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (!isUpdated) {
					updateIndex();
				}
			}
		}

		private void updateIndex() {
			System.out.println("Updating index.");
			System.out.println("Updating first part of meta data.");
			dbDataProvider.updateMetadataA();
			System.out.println("Finished first part of meta data.");
			System.out.println("Updating term idfs.");
			dbDataProvider.updateTermIdfs();
			System.out.println("Finished term idfs.");
			System.out.println("Updating term_doc tfidfs.");
			dbDataProvider.updateTermOccursInDocumentTFIDFs();
			System.out.println("Finished term_doc tfidfs.");
			System.out.println("Updating document vector norms.");
			dbDataProvider.updateDocumentVectorNorms();
			System.out.println("Finished document vector norms.");
			System.out.println("Updating last part of meta data.");
			dbDataProvider.updateMetadataB();
			System.out.println("Finished last part of meta data.");
			isUpdated = true;
			System.out.println("finished updating index.");
		}

	}

	public DBDataManager(DBTablesManager dbManager, ITextTokenizer tokenizer, int queueSize) {
		this.dbTablesManager = dbManager;
		this.tokenizer = tokenizer;
		this.indexIsRunning = true;
		this.documentQueue = new ArrayBlockingQueue<String>(queueSize);
		this.consumer = new DocumentConsumer(this, documentQueue);
		new Thread(consumer).start();
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
				TermDocs td = new TermDocs(
						r.getString(1),
						r.getLong(2), 
						r.getInt(3), 
						r.getFloat(4), 
						r.getFloat(5), 
						r.getFloat(6), 
						r.getFloat(7),
						r.getFloat(8)); 
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
	public ArrayList<Document> getDocuments() {
		ArrayList<Document> documents = new ArrayList<>();
		String sql = "select * from documents";
		try {
			PreparedStatement statement = dbTablesManager.getConnection().prepareStatement(sql);
			ResultSet r = statement.executeQuery();
			while (r.next()) {
				Document d = new Document(r.getLong(1), r.getString(2), r.getInt(3), r.getInt(4), r.getInt(5), r.getFloat(6), r.getFloat(7),
						r.getFloat(8));
				documents.add(d);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return documents;
	}

	@Override
	public ArrayList<Location> getLocations() {
		ArrayList<Location> locations = new ArrayList<>();
		String sql = "select * from locations";
		try {
			PreparedStatement statement = dbTablesManager.getConnection().prepareStatement(sql);
			ResultSet r = statement.executeQuery();
			while (r.next()) {
				Location l = null;
				try {
					l = new Location(r.getLong(1), GeometryConverter.convertPostGisToJTS((PGgeometry) r.getObject(2)));
				} catch (NoSuchObjectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				locations.add(l);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locations;
	}

	@Override
	public OverallTextIndexMetaData getOverallTextIndexMetaData() {
		String sql = "select * from metadata";
		OverallTextIndexMetaData metaData = null;
		try {
			PreparedStatement statement = dbTablesManager.getConnection().prepareStatement(sql);
			ResultSet r = statement.executeQuery();
			while (r.next()) {
				metaData = new OverallTextIndexMetaData(r.getFloat(2),r.getFloat(3),r.getFloat(4),r.getFloat(5),r.getFloat(6),r.getFloat(7),r.getInt(8));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return metaData;
	}
	@Override
	public void close() {
		this.indexIsRunning = false;
		dbTablesManager.closeConnection();
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
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void addDocument(final String text) {
		String transformedText = text.replace("´", "'");

		Map<Term, Integer> termFreqs = tokenizer.transform(transformedText);
		try {
			PreparedStatement termInsertion = dbTablesManager.getConnection().prepareStatement("insert into terms(id) values(?);");
			PreparedStatement originalTermInsertion = dbTablesManager.getConnection().prepareStatement(
					"insert into original_terms(original_term, termid) values(?,?);");
			PreparedStatement documentInsertion = dbTablesManager.getConnection().prepareStatement(
					"insert into documents(fulltext,size_in_bytes,raw_nr_of_words,indexed_nr_of_terms) values(?,?,?,?);");
			PreparedStatement termInDocInsertion = dbTablesManager.getConnection().prepareStatement(
					"insert into term_docs(termid, docid, fij, doc_tf1, doc_tf2_3) values (?,?,?,?,?)");

			insertDocument(transformedText, new ArrayList<>(termFreqs.keySet()), documentInsertion);

			for (Term indexTerm : termFreqs.keySet()) {
				insertTerm(termInsertion, indexTerm);
				insertOriginalTerms(originalTermInsertion, indexTerm);
				insertTermDocs(transformedText, termFreqs, termInDocInsertion, indexTerm);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateDocumentVectorNorms() {
		String sql = "Update documents d set "
				+ "doc_vectornorm1 = (select sqrt(sum(power(doc_term_tfidf1,2))) from term_docs where d.id=docid group by docid ),"
				+ "doc_vectornorm2 = (select sqrt(sum(power(doc_term_tfidf2,2))) from term_docs where d.id=docid group by docid),"
				+ "doc_vectornorm3 = (select sqrt(sum(power(doc_term_tfidf3,2))) from term_docs where d.id=docid group by docid)"

				+ ";";
		// System.out.println(sql);
		executeSimpleUpdate(sql);
	}

	private void updateTermOccursInDocumentTFIDFs() {
		String sql = "update term_docs td set doc_term_tfidf1 =  t.term_idf1*doc_tf1, doc_term_tfidf2 = doc_tf2_3, doc_term_tfidf3 = doc_tf2_3*(t.term_idf1) from terms t where td.termid = t.id;";

		// String correctFijThatAreZero = "Update term_docs set doc_term_tfidf1 = 0, doc_term_tfidf2 = 0, doc_term_tfidf3=0 where fij=0;";
		executeSimpleUpdate(sql/* , correctFijThatAreZero */);
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

	private void updateTermIdfs() {
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

	private void insertTermDocs(String transformedText, Map<Term, Integer> termFreqs, PreparedStatement termInDocInsertion, Term indexTerm)
			throws SQLException {
		Statement statement = dbTablesManager.getConnection().createStatement();
		ResultSet lastDocId = statement.executeQuery("select id from documents where fulltext like '%" + transformedText + "%';");
		long docId = 0;
		while (lastDocId.next()) {
			docId = lastDocId.getLong(1);
		}
		statement.close();

		int freq = termFreqs.get(indexTerm);
		if (!alreadyIndexed("select count(*) from term_docs where termid = '" + indexTerm.getIndexedTerm().getTermId() + "' AND docid='" + docId
				+ "';")) {
			termInDocInsertion.setString(1, indexTerm.getIndexedTerm().getTermId());
			termInDocInsertion.setLong(2, docId);
			termInDocInsertion.setInt(3, freq);
			termInDocInsertion.setFloat(4, freq);
			termInDocInsertion.setFloat(5, (float) (1 + (Math.log(freq) / Math.log(2))));
			termInDocInsertion.execute();
		}
	}

	private void insertOriginalTerms(PreparedStatement originalTermInsertion, Term indexTerm) throws SQLException {
		ArrayList<String> originalTerms = indexTerm.getOriginalTerms();
		for (String originalTerm : originalTerms) {
			if (!alreadyIndexed("select count(original_term) from original_terms where original_term='" + originalTerm + "';")) {
				originalTermInsertion.setString(1, originalTerm);
				originalTermInsertion.setString(2, indexTerm.getIndexedTerm().getTermId());
				originalTermInsertion.execute();
			}
		}
	}

	private void insertTerm(PreparedStatement termInsertion, Term indexTerm) throws SQLException {
		if (!alreadyIndexed("select count(id) from terms where id = '" + indexTerm.getIndexedTerm() + "';")) {
			termInsertion.setString(1, indexTerm.getIndexedTerm().getTermId());
			termInsertion.execute();
		}
	}

	private void insertDocument(final String text, ArrayList<Term> indexTerms, PreparedStatement documentInsertion) throws SQLException {
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
	private void updateTermNi() {
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
				metadataSql = dbTablesManager.getConnection().prepareStatement(
						"insert into metadata(" + "avg_doc_length_size_in_bytes, " + "avg_doc_length_raw_nr_of_words,"
								+ "avg_doc_length_indexed_nr_of_words," + "N" + ") values(" + avgDocLengthSizeInBytesSQL + ","
								+ avgDocLengthRawNrOfWords + "," + avgDocLengthIndexedNrOfTerms + ", " + N + ");");

			} else {
				metadataSql = dbTablesManager.getConnection().prepareStatement(
						"update metadata set " + "avg_doc_length_size_in_bytes = " + avgDocLengthSizeInBytesSQL + ", "
								+ "avg_doc_length_raw_nr_of_words = " + avgDocLengthRawNrOfWords + "," + "avg_doc_length_indexed_nr_of_words = "
								+ avgDocLengthIndexedNrOfTerms + "," + "N = " + N + " where id=1");

			}

			// System.err.println(metadataSql.toString());
			metadataSql.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateMetadataB() {
		try {
			String docVectorNorm1 = "(select avg(doc_vectornorm1) from documents)";
			String docVectorNorm2 = "(select avg(doc_vectornorm2) from documents)";
			String docVectorNorm3 = "(select avg(doc_vectornorm3) from documents)";

			PreparedStatement metadataSql = dbTablesManager.getConnection().prepareStatement(
					"update metadata set " + "avg_doc_length_vectornorm1 = " + docVectorNorm1 + "," + "avg_doc_length_vectornorm2 = "
							+ docVectorNorm2 + "," + "avg_doc_length_vectornorm3 = " + docVectorNorm3);
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
	 * END Text only retrieval
	 */

	/*
	 * Spatial only retrieval
	 */
	/**
	 * Used by spatial indexes. this method returns all docs if the docids parameter is not specified or null. Else only returns the docs specified.
	 * 
	 * @param docids
	 * @return
	 */
	// public List<SpatialIndexDocumentMetaData> getDocumentLocations(Long... docids) {
	// if (dbTablesManager.getConnector().tableExists("locations")) {
	// String sql = "Select l.docid, l.geometry from locations l " + getWhereClause("l", docids);
	// return new ArrayList<>(retrieveDocuments(sql).values());
	// }
	// return new ArrayList<>();
	// }

	// /**
	// * Used by spatial indexes
	// *
	// * @param ranking
	// * @return
	// */
	// public ArrayList<Document> getDocumentIdAndFulltext(List<SpatialScoreTriple> ranking) {
	// HashMap<Long, Document> indexDocuments = new HashMap<Long, Document>();
	// try {
	// PreparedStatement statement = dbTablesManager.getConnection().prepareStatement(
	// "Select d.id, d.fulltext from documents d " + getWhereClause(ranking));
	// ResultSet set = statement.executeQuery();
	// while (set.next()) {
	// long id = set.getLong(1);
	// Document document = indexDocuments.get(id);
	// if (document == null) {
	// document = new Document(set.getLong(1), set.getString(2));
	// indexDocuments.put(id, document);
	// }
	// for (SpatialScoreTriple data : ranking) {
	// if (data.getDocid() == id) {
	// document.getSpatialIndexDocumentMetaData().addSpatialScore(data.getGeometry(), data.getScore());
	// }
	// }
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return new ArrayList<>(indexDocuments.values());
	// }

	/**
	 * Used by spatial indexes
	 * 
	 * @param dFPs
	 */
	// public void storePersistently(SpatialIndexDocumentMetaData... dFPs) {
	// if (dFPs == null) {
	// return;
	// } else {
	// if (dbTablesManager.getConnector().tableExists("locations")) {
	// String sql = "Insert into locations(docid, geometry) values (?,?);";
	// try {
	// PreparedStatement statement = dbTablesManager.getConnection().prepareStatement(sql);
	// for (SpatialIndexDocumentMetaData docLoc : dFPs) {
	// List<Geometry> geometries = docLoc.getGeometries();
	// for (Geometry geometry : geometries) {
	// statement.setLong(1, docLoc.getDocid());
	// statement.setObject(2, GeometryConverter.convertJTStoPostGis(geometry));
	// statement.executeUpdate();
	// }
	// }
	// } catch (SQLException | NoSuchObjectException e) {
	// e.printStackTrace();
	// }
	// } else {
	// System.err.println("IndexDocumentProvider::storePersistently::could not insert, locations does not exist.");
	// }
	// }
	//
	// }

	// private String getWhereClause(List<SpatialScoreTriple> dFPs) {
	//
	// if (dFPs == null || dFPs.size() == 0) {
	// return ";";
	// }
	// int counter = 0;
	// String whereClause = "where ";
	// for (SpatialScoreTriple dFP : dFPs) {
	// if (++counter == dFPs.size()) {
	// whereClause += "d.id=" + dFP.getDocid() + ";";
	// } else {
	// whereClause += "d.id=" + dFP.getDocid() + " OR ";
	// }
	// }
	// return whereClause;
	// }
	//
	// private String getWhereClause(String tableName, Long... ids) {
	// if (ids == null || ids.length == 0) {
	// return ";";
	// }
	// int counter = 0;
	// String whereClause = " where ";
	// for (Long id : ids) {
	// if (++counter == ids.length) {
	// whereClause += tableName + ".id=" + id + ";";
	// } else {
	// whereClause += tableName + ".id=" + id + " OR ";
	// }
	// }
	// return whereClause;
	// }
	//
	// private Map<Long, SpatialIndexDocumentMetaData> retrieveDocuments(String sql) {
	// Map<Long, SpatialIndexDocumentMetaData> docLocs = new HashMap<>();
	// try {
	// Statement statement = dbTablesManager.getConnection().createStatement();
	// ResultSet set = statement.executeQuery(sql);
	// while (set.next()) {
	// long id = set.getLong(1);
	// SpatialIndexDocumentMetaData docLoc = docLocs.get(id);
	// if (docLoc == null) {
	// docLoc = new SpatialIndexDocumentMetaData(set.getLong(1));
	// docLocs.put(id, docLoc);
	// }
	// docLoc.addGeometry(GeometryConverter.convertPostGisToJTS((PGgeometry) set.getObject(2)));
	// }
	// statement.close();
	// } catch (SQLException | NoSuchObjectException e) {
	// e.printStackTrace();
	// }
	// return docLocs;
	// }
	//
	// /**
	// * If ids is null or empty, all documents are retrieved.
	// *
	// * @param ids
	// * @return
	// */
	// public List<SimpleIndexDocument> getDocTermLocationKeyValues(Long... ids) {
	// Map<Long /* doc id */, SimpleIndexDocument> documents = new HashMap<Long, SimpleIndexDocument>();
	//
	// try {
	// PreparedStatement statement = dbTablesManager.getConnection().prepareStatement(
	// "Select td.termid, td.docid, l.geometry from term_docs td inner join locations l on td.docid = l.docid"
	// + getWhereClause("td", ids));
	//
	// ResultSet set = statement.executeQuery();
	// while (set.next()) {
	// String term = set.getString(1);
	// Long docid = set.getLong(2);
	// Geometry documentFootPrint = GeometryConverter.convertPostGisToJTS((PGgeometry) set.getObject(0));
	//
	// SimpleIndexDocument document = documents.get(docid);
	// if (document == null) {
	// document = new SimpleIndexDocument(docid, new HashSet<>(), new HashSet<>());
	// documents.put(docid, document);
	// }
	// document.addTerm(term);
	// document.addDocumentFootprint(documentFootPrint);
	//
	// }
	//
	// } catch (SQLException | NoSuchObjectException e) {
	// e.printStackTrace();
	// }
	//
	// return new ArrayList<>(documents.values());
	// }

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
		String[] d = { "To do is to be. To be is to do.", "To be or not to be. I am what I am.", "I think therefore I am. Do be do be do.",
				"Do do do, da da da. Let it be, let it be." };

		DBDataManager dbDataManager = DBInitializer.initTestTextDB(new MockTextTokenizer(), DBInitializer.getTestDBManager(), d);
		ArrayList<Term> terms = dbDataManager.getTerms(); 
		ArrayList<Document> documents = dbDataManager.getDocuments();
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

	public static ITextIndex getTestIndex() {
		DBDataManager dbDataManager = new DBDataManager(DBInitializer.getTestDBManager(), null, 1);
		
		
		HashMap<Term, List<Document>> documents = DBDataManager.createIndexableDocuments();
		 
		ArrayList<TermDocs> termDocs = dbDataManager.getTermDocs();
		 
		 
		Map<TermDocsIdentifier, TermDocs> termDocsMeta = new HashMap<>();
		
		for(TermDocs t: termDocs){
			termDocsMeta.put(t.getId(), t);
		} 
		RAMTextOnlyIndex ramTextOnlyIndex = new RAMTextOnlyIndex(new HashMap<>(), new TextIndexMetaData(termDocsMeta, dbDataManager.getOverallTextIndexMetaData()), null);
		ramTextOnlyIndex.addDocuments(documents);
		return ramTextOnlyIndex;
	}

 
}
