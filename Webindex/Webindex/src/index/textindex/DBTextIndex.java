package index.textindex;

import index.textindex.utils.TextIndexMetaData;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocumentValues;
import index.textindex.utils.TextIndexDocumentMetaData;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.DBManager;
import index.utils.IndexDocument;
import index.utils.dbconnection.AbstractDBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DBTextIndex implements ITextIndex {

	private final class DocumentConsumer implements Runnable {

		boolean isUpdated;
		private BlockingQueue<String> documentQueue;
		private DBTextIndex dbIndex;

		public DocumentConsumer(DBTextIndex dbIndex, BlockingQueue<String> documentQueue) {
			this.dbIndex = dbIndex;
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
						dbIndex.addDocument(documentToIndex);
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
			dbIndex.updateMetadataA();
			System.out.println("Finished first part of meta data.");
			System.out.println("Updating term idfs.");
			dbIndex.updateTermIdfs();
			System.out.println("Finished term idfs.");
			System.out.println("Updating term_doc tfidfs.");
			dbIndex.updateTermOccursInDocumentTFIDFs();
			System.out.println("Finished term_doc tfidfs.");
			System.out.println("Updating document vector norms.");
			dbIndex.updateDocumentVectorNorms();
			System.out.println("Finished document vector norms.");
			System.out.println("Updating last part of meta data.");
			dbIndex.updateMetadataB();
			System.out.println("Finished last part of meta data.");
			isUpdated = true;
			System.out.println("finished updating index.");
		}

	}

	private DBManager dbManager;
	private ITextTokenizer tokenizer;
	private BlockingQueue<String> documentQueue;
	private DocumentConsumer consumer;
	private boolean indexIsRunning;

	public DBTextIndex(DBManager dbManager, ITextTokenizer tokenizer, int queueSize) {
		this.dbManager = dbManager;
		this.tokenizer = tokenizer;
		this.indexIsRunning = true;
		this.documentQueue = new ArrayBlockingQueue<String>(queueSize);
		this.consumer = new DocumentConsumer(this, documentQueue);
		new Thread(consumer).start();
		dbManager.initializeDB();
	}

	@Override
	public int getNrOfDocs() {
		return getCount("select count(*) from documents");
	}

	private int getCount(String sql) {
		try {
			Statement statement = dbManager.getConnection().createStatement();
			ResultSet set = statement.executeQuery(sql);

			while (set.next()) {
				return set.getInt(1);
			}
			statement.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return 0;
	}

	@Override
	public Set<String> getAllTerms() {
		Set<String> indexSet = new HashSet<String>();
		try {
			Statement statement = dbManager.getConnection().createStatement();
			ResultSet set = statement.executeQuery("select id from terms;");

			while (set.next()) {
				indexSet.add(set.getString(1));
			}
			statement.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return indexSet;
	}

	@Override
	public Set<IndexDocument> getAllDocuments() {
		Set<IndexDocument> docs = new HashSet<IndexDocument>();
		try {
			Statement statement = dbManager.getConnection().createStatement();
			ResultSet set = statement.executeQuery("select * from documents;");

			while (set.next()) {
				IndexDocument document = new IndexDocument(set.getLong(1), set.getString(2));
				document.setTextIndexDocumentMetaData(new TextIndexDocumentMetaData(set.getLong(1), set.getInt(3), set.getInt(4), set.getInt(5), set.getFloat(6), set.getFloat(7), set.getFloat(8)));
				docs.add(document);
			}
			statement.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return docs;
	}

	@Override
	public void close() {
		this.indexIsRunning = false;
		dbManager.closeConnection();
	}

	@Override
	public void addDocuments(final List<String> texts) {

		for (String text : texts) {
			addDocument(text);
		}
		updateMetadataA();
		updateTermIdfs();
		updateTermOccursInDocumentTFIDFs();
		updateDocumentVectorNorms();
		updateMetadataB();
	}

	private void updateDocumentVectorNorms() {
		String sql = "Update documents d set " + "doc_vectornorm1 = (select sqrt(sum(power(doc_term_tfidf1,2))) from term_docs where d.id=docid group by docid ),"
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
			Statement s = dbManager.getConnection().createStatement();
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
		Statement s = dbManager.getConnection().createStatement();
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
			Statement s = dbManager.getConnection().createStatement();
			String updateIDFs = "Update terms set term_idf1 = log(2, (" + N + "/ni)), term_idf2 = log(2,(1+(" + N + "/ni)));";
			s.execute(updateIDFs);
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addDocument(final String text) {
		String transformedText = text.replace("�", "'");

		HashMap<Term, Integer> indexTerms = tokenizer.transform(transformedText);
		try {
			PreparedStatement termInsertion = dbManager.getConnection().prepareStatement("insert into terms(id) values(?);");
			PreparedStatement originalTermInsertion = dbManager.getConnection().prepareStatement("insert into original_terms values(?,?);");
			PreparedStatement documentInsertion = dbManager.getConnection().prepareStatement("insert into documents(fulltext,size_in_bytes,raw_nr_of_words,indexed_nr_of_terms) values(?,?,?,?);");
			PreparedStatement termInDocInsertion = dbManager.getConnection().prepareStatement("insert into term_docs(termid, docid, fij, doc_tf1, doc_tf2_3) values (?,?,?,?,?)");

			if (!alreadyIndexed("select count(id) from documents where fulltext like '%" + text + "%';")) {
				documentInsertion.setString(1, text);
				documentInsertion.setInt(2, getSizeInBytes(text));
				documentInsertion.setInt(3, text.toLowerCase().replace(".", " ").replace(",", " ").replace(";", " ").toLowerCase().split("\\s+").length);
				documentInsertion.setInt(4, indexTerms.size());
				documentInsertion.execute();
			}

			for (Term indexTerm : indexTerms.keySet()) {
				if (!alreadyIndexed("select count(id) from terms where id = '" + indexTerm.getIndexedTerm() + "';")) {
					termInsertion.setString(1, indexTerm.getIndexedTerm());
					termInsertion.execute();
				}

				if (!alreadyIndexed("select count(original_term) from original_terms where original_term='" + indexTerm.getOriginalTerm() + "';")) {
					originalTermInsertion.setString(1, indexTerm.getOriginalTerm());
					originalTermInsertion.setString(2, indexTerm.getIndexedTerm());
					originalTermInsertion.execute();
				}

				Statement statement = dbManager.getConnection().createStatement();
				ResultSet lastDocId = statement.executeQuery("select id from documents where fulltext like '%" + text + "%';");
				long docId = 0;
				while (lastDocId.next()) {
					docId = lastDocId.getLong(1);
				}
				statement.close();
				if (!alreadyIndexed("select count(*) from term_docs where termid = '" + indexTerm.getIndexedTerm() + "' AND docid='" + docId + "';")) {
					int freq = indexTerms.get(indexTerm);
					termInDocInsertion.setString(1, indexTerm.getIndexedTerm());
					termInDocInsertion.setLong(2, docId);
					termInDocInsertion.setInt(3, freq);
					termInDocInsertion.setFloat(4, freq);
					termInDocInsertion.setFloat(5, (float) (1 + (Math.log(freq) / Math.log(2))));
					termInDocInsertion.execute();
				}
				updateTermNi(indexTerm.getIndexedTerm());
			}

		} catch (SQLException e) {
			// e.printStackTrace();
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
	private void updateTermNi(String indexTerm) throws SQLException {
		Statement statement;
		statement = dbManager.getConnection().createStatement();

		String updateNSql = "Update terms t set ni=(select count(*) from terms t inner join term_docs td on t.id = td.termid inner join documents d on d.id = td.docid  where t.id='" + indexTerm
				+ "') where t.id='" + indexTerm + "';";
		if (indexTerm.contains("''")) {
			updateNSql = updateNSql.replace("''", "%").replace("id=", "id like");
		}
		statement.executeUpdate(updateNSql);
		statement.close();
	}

	public void updateMetadataA() {
		try {
			PreparedStatement metadataSql = null;
			String avgDocLengthSizeInBytesSQL = "(select avg(size_in_bytes) from documents)";
			String avgDocLengthRawNrOfWords = "(select avg(raw_nr_of_words) from documents)";
			String avgDocLengthIndexedNrOfTerms = "(select avg(indexed_nr_of_terms) from documents)";

			String N = "(select count(id) from documents)";
			if (!alreadyIndexed("select count(*) from metadata;")) {
				metadataSql = dbManager.getConnection().prepareStatement(
						"insert into metadata(" + "avg_doc_length_size_in_bytes, " + "avg_doc_length_raw_nr_of_words," + "avg_doc_length_indexed_nr_of_words," + "N" + ") values("
								+ avgDocLengthSizeInBytesSQL + "," + avgDocLengthRawNrOfWords + "," + avgDocLengthIndexedNrOfTerms + ", " + N + ");");

			} else {
				metadataSql = dbManager.getConnection().prepareStatement(
						"update metadata set " + "avg_doc_length_size_in_bytes = " + avgDocLengthSizeInBytesSQL + ", " + "avg_doc_length_raw_nr_of_words = " + avgDocLengthRawNrOfWords + ","
								+ "avg_doc_length_indexed_nr_of_words = " + avgDocLengthIndexedNrOfTerms + "," + "N = " + N + " where id=1");

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

			PreparedStatement metadataSql = dbManager.getConnection().prepareStatement(
					"update metadata set " + "avg_doc_length_vectornorm1 = " + docVectorNorm1 + "," + "avg_doc_length_vectornorm2 = " + docVectorNorm2 + "," + "avg_doc_length_vectornorm3 = "
							+ docVectorNorm3);
			metadataSql.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean alreadyIndexed(String sql) {
		try {
			Statement statement = dbManager.getConnection().createStatement();
			ResultSet lastDocId = statement.executeQuery(sql);
			int count = 0;
			while (lastDocId.next()) {
				count = lastDocId.getInt(1);
			}
			statement.close();
			return count > 0;
		} catch (SQLException e) {
			// e.printStackTrace();
			return false;
		}
	}

	public ITextTokenizer getTokenizer() {
		return tokenizer;
	}

	@Override
	public HashMap<String, Integer> getNrOfDocsWithTerms(List<String> terms) {
		HashMap<String, Integer> termFreqs = new HashMap<String, Integer>();
		String sql = "select id, ni from terms where ";
		for (int i = 0; i < terms.size() - 1; ++i) {
			sql += "id = '" + terms.get(i) + "' OR ";
		}
		if (terms.size() > 0) {
			sql += "id = '" + terms.get(terms.size() - 1) + "'";
		}
		try {
			Statement statement = dbManager.getConnection().createStatement();
			ResultSet tFSet = statement.executeQuery(sql);

			while (tFSet.next()) {
				termFreqs.put(tFSet.getString(1), tFSet.getInt(2));
			}
			statement.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return termFreqs;
	}

	@Override
	public HashMap<Long, HashMap<String, Integer>> getFijsOfAllTermsInDocuments(List<IndexDocument> docIds) {
		String sql = "Select td.docid, td.termid, td.fij from term_docs td where ";

		for (int i = 0; i < docIds.size() - 1; ++i) {
			sql += "td.docid = " + docIds.get(i).getId() + " OR ";
		}
		if (docIds.size() > 0) {
			sql += "td.docid = " + docIds.get(docIds.size() - 1).getId();
		}
		return queryIndexForFijs(sql);
	}

	@Override
	public HashMap<Long, HashMap<String, Integer>> getFijs(Map<Long, List<String>> docTerms) {
		return queryIndexForFijs(getSQLForFijs(docTerms));
	}

	private HashMap<Long, HashMap<String, Integer>> queryIndexForFijs(String sql) {
		HashMap<Long, HashMap<String, Integer>> result = new HashMap<Long, HashMap<String, Integer>>();
		try {
			Statement statement = dbManager.getConnection().createStatement();
			// System.out.println(sql);
			ResultSet set = statement.executeQuery(sql);

			while (set.next()) {
				Long doc = set.getLong(1);
				HashMap<String, Integer> terms = result.get(doc);
				if (terms == null) {
					terms = new HashMap<String, Integer>();
					result.put(doc, terms);
				}
				terms.put(set.getString(2), set.getInt(3));
			}
			statement.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return result;
	}

	private String getSQLForFijs(Map<Long, List<String>> docTerms) {
		String sql = "Select td.docid, td.termid, td.fij from term_docs td where ";
		int counter = 1;
		for (Long docId : docTerms.keySet()) {

			List<String> terms = docTerms.get(docId);
			sql += "(td.docid = " + docId + (terms.size() > 0 ? " AND (" : "");

			for (int j = 0; j < terms.size() - 1; ++j) {
				sql += "td.termid = '" + terms.get(j) + "' OR ";
			}
			if (terms.size() > 0) {
				sql += "td.termid = '" + terms.get(terms.size() - 1) + "')" + (counter != docTerms.size() ? ") OR" : ")");
			}
			counter++;
		}
		return sql;
	}

	@Override
	public HashMap<Long, String> getDocumentsTexts(List<Long> docids) {
		String sql = "select id, fulltext from documents where ";
		for (int i = 0; i < docids.size() - 1; ++i) {
			sql += "id = " + docids.get(i) + " OR ";
		}
		if (docids.size() > 0) {
			sql += "id = " + docids.get(docids.size() - 1) + ";";
		}
		HashMap<Long, String> results = new HashMap<Long, String>();
		try {
			Statement statement = dbManager.getConnection().createStatement();
			ResultSet set = statement.executeQuery(sql);

			while (set.next()) {
				results.put(set.getLong(1), set.getString(2));
			}
			statement.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return results;
	}

	@Override
	public HashMap<String, List<IndexDocument>> getDocumentsForTerms(List<String> terms) {
		String sql = "select t.id, d.id, d.fulltext, d.size_in_bytes, d.raw_nr_of_words, d.indexed_nr_of_terms, d.doc_vectornorm1, d.doc_vectornorm2, d.doc_vectornorm2 from "
				+ "terms t inner join term_docs td on t.id = td.termid inner join documents d on d.id = td.docid where ";

		for (int i = 0; i < terms.size() - 1; ++i) {
			sql += "t.id = '" + terms.get(i) + "' OR ";
		}
		if (terms.size() > 0) {
			sql += "t.id = '" + terms.get(terms.size() - 1) + "';";
		}

		HashMap<String, List<IndexDocument>> results = new HashMap<String, List<IndexDocument>>();
		// System.out.println(sql);
		try {
			Statement statement = dbManager.getConnection().createStatement();
			ResultSet set = statement.executeQuery(sql);

			while (set.next()) {
				String term = set.getString(1);
				List<IndexDocument> docs = results.get(term);
				if (docs == null) {
					docs = new ArrayList<IndexDocument>();
					results.put(term, docs);
				}
				IndexDocument document = new IndexDocument(set.getLong(2), set.getString(3));
				document.setTextIndexDocumentMetaData(new TextIndexDocumentMetaData(set.getLong(2), set.getInt(4), set.getInt(5), set.getInt(6), set.getFloat(7), set.getFloat(8), set.getFloat(9)));
				docs.add(document);
			}
			statement.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return results;
	}

	@Override
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
			Statement statement = dbManager.getConnection().createStatement();
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

	private String getOrSql(boolean isWithView, List<String> queryTerms) {
		String termid = (isWithView ? "termid" : "id");
		String orSql = "";
		for (int i = 0; i < queryTerms.size() - 1; ++i) {

			orSql += "t." + getTermDescription2(termid, queryTerms.get(i)) + " OR ";
		}
		if (queryTerms.size() > 0) {
			orSql += "t." + getTermDescription2(termid, queryTerms.get(queryTerms.size() - 1));
		}
		return orSql;
	}

	private String getTermDescription2(String termid, String term) {
		if (term.contains("''")) {
			String term2 = term.replace("''", "%");
			return termid + " like '" + term2 + "'";
		} else {
			return termid + "='" + term + "'";
		}
	}

	private String getDocsCountsFromSql(boolean isIntersected) {
		if (isIntersected) {
			return ", docscounts";
		} else {
			return "";
		}
	}

	private String getDocsCountsWhereSql(boolean isIntersected, int count) {
		if (isIntersected) {
			return "and docscounts.id=d.id and docscounts.counter >= " + count;
		} else {
			return "";
		}
	}

	@Override
	public ArrayList<IndexDocument> getDocTermKeyValues(List<String> queryTerms, boolean isIntersected) {

		String sql = "";
		if (isIntersected) {
			sql = "with docscounts(id, counter) as(select t.docid, count(t.docid) " + "from term_docs t " + "where (" + getOrSql(true, queryTerms) + ") " + "group by t.docid)\n";
		}

		sql += "select * from terms t inner join term_docs td on t.id = td.termid inner join documents d on d.id = td.docId " + getDocsCountsFromSql(isIntersected) + " " + "where ("
				+ getOrSql(false, queryTerms) + ") " + getDocsCountsWhereSql(isIntersected, queryTerms.size()) + ";";

		ArrayList<IndexDocument> results = new ArrayList<>();

		try {
			Statement statement = dbManager.getConnection().createStatement();
			ResultSet set = statement.executeQuery(sql);

			while (set.next()) {
				String term = set.getString(1);
				IndexDocument document = new IndexDocument(set.getLong(13));
				boolean found = false;
				for (int i = 0; i < results.size(); ++i) {
					if (results.get(i).equals(document)) {
						document = results.get(i);
						found = true;
						break;
					}
				}
				if (!found) {
					document = new IndexDocument(document.getId(), set.getString(14));
					document.setTextIndexDocumentMetaData(new TextIndexDocumentMetaData(document.getId(), set.getInt(15), set.getInt(16), set.getInt(17), set.getFloat(18), set.getFloat(19), set
							.getFloat(20)));
					results.add(document);
				}
				TermDocumentValues td = new TermDocumentValues(term, set.getInt(2), set.getFloat(3), set.getFloat(4), set.getInt(7), set.getFloat(8), set.getFloat(9), set.getFloat(10),
						set.getFloat(11), set.getFloat(12));
				// query for the original terms.
				String origTermsSql = "Select ot.original_term from original_terms ot inner join terms t on ot.termid = t.id inner join term_docs td on t.id = td.termid where td.docid="
						+ document.getId() + " and " + getTermDescription(term) + ";";
				Statement s = dbManager.getConnection().createStatement();
				ResultSet res = s.executeQuery(origTermsSql);
				while (res.next()) {
					td.addOriginalTerm(res.getString(1));
				}

				document.getTextIndexDocumentMetaData().addTermDocValues(term, td);

			}
			statement.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return results;
	}

	private String getTermDescription(String term) {
		if (term.contains("''")) {
			String term2 = term.replace("''", "%");
			return "t.id like '" + term2 + "'";
		} else {
			return "t.id='" + term + "'";
		}
	}

	@Override
	public TextIndexMetaData getMetaData() {
		TextIndexMetaData m = null;
		try {
			Statement statement = dbManager.getConnection().createStatement();
			ResultSet set = statement.executeQuery("Select * from metadata where id=1");
			while (set.next()) {
				m = new TextIndexMetaData(set.getFloat(2), set.getFloat(3), set.getFloat(4), set.getFloat(5), set.getFloat(6), set.getFloat(7), set.getInt(8));
			}
			statement.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return m;

	}

	public void addForIndexation(String pureText) {
		try {
			documentQueue.put(pureText);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// TODO ? it's the other way round compared to how I did it.
	// @Override
	// public void queryIndex(List<String> queryTerms, boolean isIntersected, ITextSimilarity similarityAlgorithm) {
	// for(String queryTerm:queryTerms){
	// similarityAlgorithm.calculateSimilarity(queryTerm, isIntersected);
	// }
	// }

}
