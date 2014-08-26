package rest.dao;

import index.textindex.implementations.ITextIndex;
import index.textindex.utils.texttransformation.GermanTextTokenizer;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.textindex.utils.texttransformation.MockTextTokenizer;
import index.utils.Ranking;
import index.utils.query.TextIndexQuery;
import testutils.DBInitializer;
import utils.dbconnection.AbstractDBConnector;
import utils.dbconnection.PGDBConnector;
import utils.dbcrud.DBDataManager;
import utils.dbcrud.DBTablesManager;

public enum IndexDao {
	INSTANCE;

	private ITextIndex index;
	private DBDataManager dbDataProvider;
	private ITextTokenizer tokenizer;
	private static final int QUEUE_SIZE = 5002;

	private IndexDao() {
		String host = "localhost";
		String port = "5432";
		String database = "girindex_test";
		String user = "postgres";
		String password = "32qjivkd";

		AbstractDBConnector db = new PGDBConnector(host, port, database, user, password);
		DBTablesManager dbManager = new DBTablesManager(db);
		this.tokenizer = new GermanTextTokenizer();
		this.dbDataProvider = new DBDataManager(dbManager, tokenizer, QUEUE_SIZE);
		this.index = null;
	}

	public void initDB() {
		dbDataProvider.initializeDBTables();
	}

	public void addDocument(String pureText) {
		dbDataProvider.addDocumentDeferred(pureText);
	}

	public Ranking submitQuery(String type, String query, String intersected) {
		if (query == null || query.equals("undefined") || query.trim().length() == 0) {
			return new Ranking();
		}

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

		return index.queryIndex(new TextIndexQuery(query, type, isIntersected));
	}

	public static void main(String[] args) {
		MockTextTokenizer tokenizer = new MockTextTokenizer();
		String[] docs = { "To do is to be. To be is to do.", "To be or not to be. I am what I am.", "I think therefore I am. Do be do be do.",
				"Do do do, da da da. Let it be, let it be." };
		
		DBDataManager dbManager =DBInitializer.initTestTextDB(tokenizer, DBInitializer.getTestDBManager(), docs);
//		DBInitializer.tearDownTestDB(dbManager);
		
	}

}
