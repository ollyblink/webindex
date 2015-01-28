package db; 
/**
 * Implements a JDBC Connection to a PostgreSQL database.
 * 
 * @author Oliver Zihler
 * @version 0.1
 * @see <code>MySqlDBConnector</code>
 * @see <code>AbstractDBConnector</code>
 * 
 */
public class PGDBConnector extends AbstractDBConnector{ 
	/** The driver for a PostgreSQL connection */
	private static final String PGDRIVER = "org.postgresql.Driver";
	
	/**
	 * Constructor 
	 * Generates a new connection to a database and lets you query it.
	 * 
	 * @param host the host of the database, e.g. localhost
	 * @param port the port, e.g. 5432
	 * @param database the name of the database that holds the various tables
	 * @param user the username to log onto the db
	 * @param password the password to log onto the db
	 */
	public PGDBConnector(String host, String port, String database, String user, String password) {
		super(PGDRIVER, host, port, database, user, password);
	}

	@Override
	protected String getUrl() { 
		return ("jdbc:postgresql:" + (host != null ? ("//" + host) + (port != null ? ":" + port : "") + "/" : "") + database);
	}

}