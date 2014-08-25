package testutils;

import index.spatialindex.implementations.SpatialOnlyIndex;
import index.spatialindex.utils.DBDataProviderTest;
import index.spatialindex.utils.LocationProvider;
import index.spatialindex.utils.SpatialIndexDocumentMetaData;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.textindex.utils.texttransformation.MockTextTokenizer;
import index.utils.DBDataProvider;
import index.utils.DBManager;
import index.utils.dbconnection.PGDBConnector;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class DBInitializer {
	private static ITextTokenizer tokenizer;

	public static DBDataProvider initTestTextDB(MockTextTokenizer tokenizer, DBManager dbManager, String[] docs) {
		DBInitializer.tokenizer = tokenizer;

		List<String> documents = new ArrayList<String>();

		for (String d : docs) {
			documents.add(d);
		}
		DBDataProvider dbDataProvider = new DBDataProvider(dbManager, tokenizer, DBDataProviderTest.QUEUE_SIZE);
		dbDataProvider.addDocuments(documents);
		return dbDataProvider;
	}

	public static DBManager getTestDBManager() {
		String host = "localhost";
		String port = "5432";
		String database = "girindex_test";
		String user = "postgres";
		String password = "32qjivkd";

		DBManager dbManager = new DBManager(new PGDBConnector(host, port, database, user, password));
		dbManager.dropTables();
		dbManager.initializeDB();

		return dbManager;
	}

	public static void tearDownTestDB(DBManager dbManager) {
		dbManager.dropTables();
		dbManager.closeConnection();
	}

	public static SpatialOnlyIndex initSpatialTestDB(DBManager dbManager, String[] docs) {

		SpatialOnlyIndex index = new SpatialOnlyIndex(new Quadtree(), new DBDataProvider(dbManager, tokenizer, DBDataProviderTest.QUEUE_SIZE));
		SpatialIndexDocumentMetaData[] docLocs = new SpatialIndexDocumentMetaData[docs.length];
		for (int i = 0; i < docs.length; ++i) {
			docLocs[i] = new SpatialIndexDocumentMetaData(i + 1);
			List<? extends Geometry> geometries = LocationProvider.INSTANCE.retrieveLocations(docs[i]);
			for (Geometry g : geometries) {
				docLocs[i].addGeometry(g);
			}

		}
		index.addLocations(docLocs);
		return index;
	}
}
