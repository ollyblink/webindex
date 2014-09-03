package testutils;

import index.textindex.utils.informationextractiontools.ITextInformationExtractor;
import index.textindex.utils.informationextractiontools.MockTextInformationExtractor;

import java.util.ArrayList;
import java.util.List;

import utils.dbconnection.PGDBConnector;
import utils.dbcrud.DBDataManager;
import utils.dbcrud.DBTablesManager;

public class DBInitializer {
	private static ITextInformationExtractor tokenizer;

	public static DBDataManager initTestTextDB(MockTextInformationExtractor tokenizer, DBTablesManager dbManager, String[] docs) {
		DBInitializer.tokenizer = tokenizer;

		List<String> documents = new ArrayList<String>();

		for (String d : docs) {
			documents.add(d);
		}
		DBDataManager dbDataProvider = new DBDataManager(dbManager, tokenizer, false);
		dbDataProvider.addDocuments(documents);
		return dbDataProvider;
	}

	public static DBTablesManager initDB() {
		String host = "localhost";
		String port = "5432";
		String database = "girindex_test";
		String user = "postgres";
		String password = "32qjivkd";

		DBTablesManager dbManager = new DBTablesManager(new PGDBConnector(host, port, database, user, password));
		dbManager.dropTables();
		dbManager.initializeDBTables();

		return dbManager;
	}

	public static void tearDownTestDB(DBTablesManager dbManager) {
		dbManager.dropTables();
		// dbManager.closeConnection();
	}

	 
}
