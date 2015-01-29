package testutils;

import index.spatialindex.utils.geolocating.georeferencing.IPlaceExtractor;
import index.spatialindex.utils.geolocating.georeferencing.LocationFinder;
import index.spatialindex.utils.geolocating.georeferencing.YPMPlaceExtractor;
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
		// Create location finder
		LocationFinder locationFinder = new LocationFinder();
		DBDataManager dbDataProvider = new DBDataManager(dbManager, tokenizer, locationFinder, false);
		dbDataProvider.addDocuments(documents);
		return dbDataProvider;
	}

	public static DBTablesManager initDB() {
		String host = "localhost";
		String port = "5432";
		String database = "girindex";
		String user = "postgres";
		String password = "postgres";
		// Add all extractors

		DBTablesManager dbManager = new DBTablesManager(new PGDBConnector(host, port, database, user, password));
		dbManager.dropTables();
		dbManager.initializeDBTables();

		return dbManager;
	}

	public static void tearDownTestDB(DBTablesManager dbManager) {
		dbManager.dropTables();
	}

}
