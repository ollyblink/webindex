package utils.dbconnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents a database abstraction. The aim is to encapsulate all the ways on how to create a connection etc. so that the user can solely focus on the creation, querying and updating of database tables.
 * 
 * @author MSc. Oliver F. M. Zihler
 * @version 0.1
 * @see <code>MySQLDBConnector</code>
 */
public abstract class AbstractDBConnector {

	/** The host of the database, e.g. localhost */
	protected String host;
	/** The port, e.g. 5432 */
	protected String port;
	/** The name of the database that holds the various tables */
	protected String database;
	/** The username to log onto the db */
	protected String user;
	/** The password to log onto the db */
	protected String password;
	/** A connection to query the database */
	protected Connection connection;
	/** The driver needed to specify the used database */
	protected final String driver;

	/**
	 * Constructor Generates a new connection to a database and lets you query it.
	 * 
	 * @param host
	 *            the host of the database, e.g. localhost
	 * @param port
	 *            the port, e.g. 5432
	 * @param database
	 *            the name of the database that holds the various tables
	 * @param user
	 *            the username to log onto the db
	 * @param password
	 *            the password to log onto the db
	 */
	public AbstractDBConnector(String driver, String host, String port, String database, String user, String password) {
		this.driver = driver;
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;

		loadJDBCDriver();
		openConnection();
	}

	/**
	 * close the connection. This has to be done after the database had been used.
	 */
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * loading the JDBC driver
	 */
	private void loadJDBCDriver() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public boolean tableExists(String indexTableName) {

		try {
			DatabaseMetaData meta = this.connection.getMetaData();
			ResultSet res = meta.getTables(null, null, null, new String[] { "TABLE" });

			while (res.next()) { 
 				if (res.getString("TABLE_NAME").equalsIgnoreCase(indexTableName)) {
					return true; // This table exists
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * opening the connection. This has to be done before the database can be used.
	 */
	protected void openConnection() {
		try {
			connection = DriverManager.getConnection(getUrl(), user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	/**
	 * 
	 * @return the URL to this database. Hook method implemented in the subclasses.
	 */
	protected abstract String getUrl();

	public Connection getConnection() {
		return connection;
	}
}