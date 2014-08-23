package index.utils;

import index.spatialindex.spatialindeximplementations.SpatialOnlyIndex;
import index.spatialindex.utils.SpatialIndexDocumentMetaData;
import index.spatialindex.utils.IndexDocumentProvider;
import index.spatialindex.utils.LocationProvider;
import index.textindex.DBTextIndex;
import index.textindex.utils.texttransformation.MockTextTokenizer;
import index.utils.dbconnection.AbstractDBConnector;
import index.utils.dbconnection.PGDBConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class DBManager {

	public static final String[] sqls = { "create table terms (id varchar(100) not null primary key, ni integer, term_idf1 float, term_idf2 float);",

	"create index term_index1 on terms(id);", "create index term_index2 on terms(ni);", "create index term_index3 on terms(term_idf1);", "create index term_index4 on terms(term_idf2);",

	"create table documents(id bigserial not null primary key,fulltext text,size_in_bytes integer,raw_nr_of_words integer,indexed_nr_of_terms integer,doc_vectornorm1 float,doc_vectornorm2 float,doc_vectornorm3 float);",

	"create index doc_index1 on documents(id);", "create index doc_index2 on documents(size_in_bytes);", "create index doc_index3 on documents(raw_nr_of_words);", "create index doc_index4 on documents(indexed_nr_of_terms);", "create index doc_index6 on documents(doc_vectornorm1);", "create index doc_index7 on documents(doc_vectornorm2);", "create index doc_index8 on documents(doc_vectornorm3);",

	"create table term_docs(" + "termid varchar(100) not null references Terms(id), " + "docid bigserial not null references documents(id), " + "fij integer," + "doc_tf1 float," + "doc_tf2_3 float," + "doc_term_tfidf1 float," + "doc_term_tfidf2 float," + "doc_term_tfidf3 float," + "primary key(termid, docid)" + ");",

	"create index td_index1 on term_docs(termid);", "create index td_index2 on term_docs(docid);", "create index td_index3 on term_docs(fij);", "create index td_index4 on term_docs(doc_tf1);", "create index td_index5 on term_docs(doc_tf2_3);", "create index td_index6 on term_docs(doc_term_tfidf1);", "create index td_index7 on term_docs(doc_term_tfidf2);", "create index td_index8 on term_docs(doc_term_tfidf3);",

	"create table metadata(" + "id bigserial primary key," + "avg_doc_length_size_in_bytes float," + "avg_doc_length_raw_nr_of_words float," + "avg_doc_length_indexed_nr_of_words float," + "avg_doc_length_vectornorm1 float," + "avg_doc_length_vectornorm2 float," + "avg_doc_length_vectornorm3 float," + "N integer" + ");",

	"create table original_terms(original_term varchar(100) not null primary key, termid varchar(100) not null references terms(id));", "create index orig_term_index on original_terms(original_term);",

	"create table locations(docid bigserial not null references documents(id)); Select ADDGEOMETRYCOLUMN('locations','geometry',4326,'GEOMETRY',2); Create Index locations_spatial_index on locations using GIST(geometry); Alter table locations add primary key(docid, geometry)" };

	public static final String dropTables = "drop table if exists terms, documents, term_docs, metadata, original_terms, locations cascade;";

	private AbstractDBConnector db;

	public DBManager(AbstractDBConnector db) {
		this.db = db;
	}

	/**
	 * initializes the database tables. Returns true on successful creation of tables, false if either an SQLException occurred or the tables already exist.
	 * 
	 * @return
	 */
	public final boolean initializeDB() {
		// for (String sql : sqls) {
		// System.err.println(sql);
		// }
		if (!db.tableExists("terms")) {
			try {
				Statement statement = db.getConnection().createStatement();
				for (String sql : sqls) {
					statement.execute(sql);
				}
				statement.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			// System.err.println("tables have already been initialised.");
			return false;
		}
	}

	public final void dropTables() {
		if (db.tableExists("terms")) {
			try {
				Statement statement = db.getConnection().createStatement();
				statement.execute(dropTables);
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Connection getConnection() {
		return db.getConnection();
	}

	public void closeConnection() {
		db.closeConnection();
	}

	public static DBTextIndex initTestTextDB(MockTextTokenizer tokenizer, DBManager dbManager, String[] docs) {
		DBTextIndex index = new DBTextIndex(dbManager, tokenizer, 4000); 
		List<String> documents = new ArrayList<String>();
		
		for(String d:docs){
			documents.add(d);
		}  
		index.addDocuments(documents);
		return index;
	}

	public static DBManager getTestDBManager() {
		String host = "localhost";
		String port = "5432";
		String database = "girindex_test";
		String user = "postgres";
		String password = "32qjivkd";

		DBManager dbManager =  new DBManager(new PGDBConnector(host, port, database, user, password));
		dbManager.dropTables();
		dbManager.initializeDB();
		
		return dbManager;
	}
	
	public static void tearDownTestDB(DBManager dbManager){
		dbManager.dropTables();
		dbManager.closeConnection();
	}

	public static SpatialOnlyIndex initSpatialTestDB(DBManager dbManager, String[] docs) {
		 
		SpatialOnlyIndex index = new SpatialOnlyIndex(new Quadtree(), new IndexDocumentProvider(dbManager));
		SpatialIndexDocumentMetaData[] docLocs = new SpatialIndexDocumentMetaData[docs.length];
		for(int i = 0; i< docs.length;++i){
			docLocs[i] = new SpatialIndexDocumentMetaData(i+1);
			List<? extends Geometry> geometries =  LocationProvider.INSTANCE.retrieveLocations(docs[i]); 
			for(Geometry g: geometries){
				docLocs[i].addGeometry(g);
			}
			
		}
		index.addLocations(docLocs);
		return index;
	}
	
	public AbstractDBConnector getConnector(){
		return db;
	}
}
