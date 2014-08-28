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
	private final String host = "localhost";
	private final String port = "5432";
	private final String database = "girindex";
	private final String user = "postgres";
	private final String password = "32qjivkd";
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
		 extractors.add(new GNPlaceExtractor(USERNAME));
		 extractors.add(new HikrGazetteerPlaceExtractor(new PGDBConnector(host, port, database, user, password)));
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
		for(IPlaceExtractor p: extractors){
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
