package rest.dao;

import index.textindex.implementations.DBTextOnlyIndex;
import index.textindex.similarities.ITextSimilarity;
import index.textindex.similarities.probabilisticmodels.BM1;
import index.textindex.similarities.probabilisticmodels.BM11;
import index.textindex.similarities.probabilisticmodels.BM15;
import index.textindex.similarities.probabilisticmodels.BM25;
import index.textindex.similarities.probabilisticmodels.BestMatch;
import index.textindex.similarities.tfidfweighting.DocTFIDFTypes;
import index.textindex.similarities.tfidfweighting.Formula1TFStrategy;
import index.textindex.similarities.tfidfweighting.Formula2TFStrategy;
import index.textindex.similarities.tfidfweighting.Formula3TFStrategy;
import index.textindex.similarities.tfidfweighting.QueryIDFTypes;
import index.textindex.similarities.vectorspacemodels.CosineSimilarity;
import index.textindex.utils.texttransformation.GermanTextTokenizer;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.DBManager;
import index.utils.Ranking;
import index.utils.dbconnection.AbstractDBConnector;
import index.utils.dbconnection.PGDBConnector;

public enum IndexDao {
	INSTANCE;

	private DBTextOnlyIndex index;
	private DBManager dbManager;
	private static final int QUEUE_SIZE = 5002;

	private IndexDao() {
		String host = "localhost";
		String port = "5432";
		String database = "girindex";
		String user = "postgres";
		String password = "32qjivkd";

		AbstractDBConnector db = new PGDBConnector(host, port, database, user, password);
		dbManager = new DBManager(db);
		ITextTokenizer tokenizer = new GermanTextTokenizer();

		index = new DBTextOnlyIndex(dbManager, tokenizer, QUEUE_SIZE);
	}

	public void initDB() {
		dbManager.initializeDB();
	}

	public void addDocument(String pureText) {
//		System.out.println("IndexDao::addDocument:indexing: "+ pureText);
		index.addForIndexation(pureText);
		// currentDocs.add(pureText);
		// if (currentDocs.size() % 1 == 0) {
		// System.out.println("Indexing " + currentDocs.size() + " docs.");
		// toIndex.addAll(currentDocs);
		// index.addDocuments(toIndex);
		// currentDocs.clear();
		// }
	}

	public Ranking submitQuery(String type, String query, String intersected) {
		if (query == null || query.equals("undefined") || query.trim().length() == 0) {
			return new Ranking();
		}
		ITextSimilarity sim = getSimilarity(type);
		boolean isIntersected = false;
		switch (intersected) {
		case "intersection":
			isIntersected = true;
			break;
		case "union":
		default:
			isIntersected = false;
			break;
		}

		Ranking ranking = sim.calculateSimilarity(query, isIntersected);

		return ranking;
	}

	private ITextSimilarity getSimilarity(String type) {
		switch (type) {
		case "bm1":
			return new BestMatch(index, index.getTokenizer(), new BM1());
		case "bm11":
			return new BestMatch(index, index.getTokenizer(), new BM11(1.2f));

		case "bm15":
			return new BestMatch(index, index.getTokenizer(), new BM15(1.2f));

		case "bm25":
			return new BestMatch(index, index.getTokenizer(), new BM25(1.2f, 0.75f));

		case "cosine1":
			return new CosineSimilarity(index, index.getTokenizer(), new Formula1TFStrategy(), QueryIDFTypes.TERM_IDF1, DocTFIDFTypes.DOC_TFIDF1);

		case "cosine2":
			return new CosineSimilarity(index, index.getTokenizer(), new Formula2TFStrategy(), QueryIDFTypes.TERM_IDF2, DocTFIDFTypes.DOC_TFIDF2);

		case "cosine3":
		default:
			return new CosineSimilarity(index, index.getTokenizer(), new Formula3TFStrategy(), QueryIDFTypes.TERM_IDF1, DocTFIDFTypes.DOC_TFIDF3);

		}
	}

}
