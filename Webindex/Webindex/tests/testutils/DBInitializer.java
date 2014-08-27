package testutils;

import index.spatialindex.implementations.SpatialOnlyIndex;
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
		DBDataManager dbDataProvider = new DBDataManager(dbManager, tokenizer, 5000);
		dbDataProvider.addDocuments(documents);
		return dbDataProvider;
	}

	public static DBTablesManager getTestDBManager() {
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

	public static void tearDownTestDB(DBDataManager dbManager) {
		dbManager.dropTables();
		dbManager.closeConnection();
	}

	public static SpatialOnlyIndex initSpatialTestDB(DBTablesManager dbManager, String[] docs) {

		SpatialOnlyIndex index = null;
//		new SpatialOnlyIndex(new Quadtree(), new DBDataManager(dbManager, tokenizer, 5000));
//		SpatialDocument[] docLocs = new SpatialDocument[docs.length];
//		for (int i = 0; i < docs.length; ++i) {
//			docLocs[i] = new SpatialDocument(i + 1);
//			List<? extends Geometry> geometries = LocationProvider.INSTANCE.retrieveLocations(docs[i]);
//			for (Geometry g : geometries) {
//				docLocs[i].addGeometry(g);
//			}
//
//		}
//		index.addDocumentFootprint(docLocs);
		return index;
	}
}
