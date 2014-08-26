package testutils;

import index.spatialindex.implementations.SpatialOnlyIndex;
import index.spatialindex.utils.LocationProvider;
import index.spatialindex.utils.SpatialIndexDocumentMetaData;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.textindex.utils.texttransformation.MockTextTokenizer;

import java.util.ArrayList;
import java.util.List;

import utils.dbconnection.PGDBConnector;
import utils.dbcrud.DBDataManager;
import utils.dbcrud.DBTablesManager;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class DBInitializer {
	private static ITextTokenizer tokenizer;

	public static DBDataManager initTestTextDB(MockTextTokenizer tokenizer, DBTablesManager dbManager, String[] docs) {
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

		SpatialOnlyIndex index = new SpatialOnlyIndex(new Quadtree(), new DBDataManager(dbManager, tokenizer, 5000));
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
