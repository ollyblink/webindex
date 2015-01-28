package main;

import db.AbstractDBConnector;
import db.PGDBConnector;
import db.TwitterStreamDBCreatorAndWriter;

/**
 * Setting up the database tables in the Postgresql DB
 * 
 * @author rsp
 *
 */
public class InitializeDBMain {
    public static void main(String[] args) {
	String host = "localhost";
	String port = "5432";
	String database = "postgres";
	String user = "postgres";
	String password = "";

	AbstractDBConnector postgresConnector = new PGDBConnector(host, port, database, user, password);

	TwitterStreamDBCreatorAndWriter indexCreator = new TwitterStreamDBCreatorAndWriter(postgresConnector, false);

	indexCreator.dropTables();

	indexCreator.initializeTable();
	indexCreator.close();
    }
}
