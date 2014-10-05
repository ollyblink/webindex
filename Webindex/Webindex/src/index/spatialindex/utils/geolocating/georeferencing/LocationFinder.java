package index.spatialindex.utils.geolocating.georeferencing;

import java.util.ArrayList;
import java.util.List;

import utils.dbconnection.PGDBConnector;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Queries first for places with Yahoo! Placemaker. If nothing was found, GeoNames is queried instead.
 * 
 * @author Oliver Zihler
 * @version 0.1
 * 
 */
public enum LocationFinder {
	INSTANCE;

	List<IPlaceExtractor> extractors = new ArrayList<IPlaceExtractor>();

	private final String YPM_XML = "ypm.xml";
	private final String USERNAME = "ollyblink";
	 private   String host = "localhost";
	 private   String port = "5432";
	 private   String database = "girindex";
	 private   String user = "postgres";
	 private   String password = "postgres";
//	String host = "geocomp-res.geo.uzh.ch";
//	String port = "5432";
//	String database = "girindex2";
//	String user = "gcscript";
//	String password = "gcmdp8057";

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
		reinitializeList(host);
	}

	private void reinitializeList(String host) {
		extractors.clear();

		extractors.add(new YPMPlaceExtractor(YPM_XML));
//		extractors.add(new GNPlaceExtractor(USERNAME));
		extractors.add(new HikrGazetteerPlaceExtractor(host,port,database,user,password));
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
		reinitializeList(host);
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
		reinitializeList(host);
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
		reinitializeList(host);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		reinitializeList(host);
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
	private LocationFinder() {
		extractors.add(new YPMPlaceExtractor(YPM_XML));
//		extractors.add(new GNPlaceExtractor(USERNAME));
		extractors.add(new HikrGazetteerPlaceExtractor(host,port,database,user,password));
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
