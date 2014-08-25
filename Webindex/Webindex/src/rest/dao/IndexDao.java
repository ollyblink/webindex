package rest.dao;

import index.textindex.implementations.DBTextOnlyIndex;
import index.textindex.utils.texttransformation.GermanTextTokenizer;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.DBDataProvider;
import index.utils.DBManager;
import index.utils.Ranking;
import index.utils.dbconnection.AbstractDBConnector;
import index.utils.dbconnection.PGDBConnector;
import index.utils.query.TextIndexQuery;

public enum IndexDao {
	INSTANCE;

	private DBTextOnlyIndex index;
	private DBManager dbManager;
	private DBDataProvider dbDataProvider;
	private ITextTokenizer tokenizer;
	private static final int QUEUE_SIZE = 5002;

	private IndexDao() {
		String host = "localhost";
		String port = "5432";
		String database = "girindex";
		String user = "postgres";
		String password = "32qjivkd";

		AbstractDBConnector db = new PGDBConnector(host, port, database, user, password);
		this.dbManager = new DBManager(db);
		this.tokenizer = new GermanTextTokenizer();
		this.dbDataProvider = new DBDataProvider(dbManager, tokenizer, QUEUE_SIZE);
		this.index = new DBTextOnlyIndex(dbDataProvider, tokenizer);
	}

	public void initDB() {
		dbManager.initializeDB();
	}

	public void addDocument(String pureText) {
		dbDataProvider.addForIndexation(pureText);
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

	

}
