package index.spatialindex.utils.geolocating.georeferencing;

import java.util.ArrayList;
import java.util.List;

import utils.dbconnection.AbstractDBConnector;
import utils.dbconnection.PGDBConnector;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Queries first for places with Yahoo! Placemaker. If nothing was found, GeoNames is queried instead.
 * 
 * @author Oliver Zihler
 * @version 0.1
 * 
 */
public class LocationFinder {
	List<IPlaceExtractor> extractors = new ArrayList<IPlaceExtractor>();

	private final String YPM_XML = "ypm.xml";
	private final String USERNAME = "ollyblink";

	private static final String host = "geocomp-res.geo.uzh.ch";
	private static final String port = "5432";
	private static final String database = "girindex2";
	private static final String user = "gcscript";
	private static final String password = "gcmdp8057";

	private String testhost = "localhost";
	private String testport = "5432";
	private String testdatabase = "girindex";
	private String testuser = "postgres";
	private String testpassword = "postgres";
	private AbstractDBConnector db;

	public String getHost() {
		return host;
	}

	public String getPort() {
		return port;
	}

	public String getDatabase() {
		return database;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * Constructor
	 * 
	 * @param countryCode
	 * @param userName
	 * @param xmlPath
	 * @param stringRules
	 * @param boundsRules
	 */
	public LocationFinder(AbstractDBConnector db) {
		this.db = db;
		initExtractors(db);
	}

	private void initExtractors(AbstractDBConnector db) {
		extractors.add(new YPMPlaceExtractor(YPM_XML));
		// extractors.add(new GNPlaceExtractor(USERNAME));
		extractors.add(new HikrGazetteerPlaceExtractor(db));
	}

	public LocationFinder() {
		this(new PGDBConnector(host, port, database, user, password));
	
	}

	public LocationFinder(boolean isTest) {
		if (isTest) {
			this.db = new PGDBConnector(testhost, testport, testdatabase, testuser, testpassword);
		} else {
			this.db = new PGDBConnector(host, port, database, user, password);
		}
		initExtractors(db);
	}

	/**
	 * Finds the location according to the specified query
	 * 
	 * @param query
	 *            place name to find
	 * @return MBRs of locations found
	 */
	public ArrayList<Geometry> findLocation(String query) {

		ArrayList<Geometry> locations = new ArrayList<>();
		for (IPlaceExtractor p : extractors) {
			locations.addAll(p.extract(query));
		}
		return locations;
	}

	//
	/**
	 * Finds the location according to the specified query
	 * 
	 * @param query
	 *            place name to find
	 * @return MBRs of locations found
	 */
	public ArrayList<Geometry> findMBR(String query) {
		ArrayList<Geometry> locations = new ArrayList<>();
		locations.addAll(extractors.get(0).extract(query));
		return locations;
	}
}
