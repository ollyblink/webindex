package index.spatialindex.utils.geolocating.georeferencing;

import index.spatialindex.utils.geolocating.georeferencing.PEFactory.PEType;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.awt.PointShapeFactory.X;
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
	

	/** First extractor to query */
	private IPlaceExtractor placeMaker;
	/** Second extractor to query */
	private IPlaceExtractor geoNames;
	
	private final String YPM_XML = System.getProperty("user.dir") + "/files/ypm.xml";
	private final String USERNAME = "ollyblink";
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
		this.placeMaker = PEFactory.createPlaceExtractor(PEType.YPM, USERNAME, YPM_XML);
		this.geoNames = PEFactory.createPlaceExtractor(PEType.GN, USERNAME, YPM_XML);
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
		locations.addAll(placeMaker.extract(query));
		locations.addAll(geoNames.extract(query));
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
		locations.addAll(placeMaker.extract(query)); 
		return locations;
	}
}
