package index.spatialindex.utils.placeextractor;


import index.spatialindex.utils.placeextractor.PEFactory.PEType;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;




/**
 * Queries first for places with Yahoo! Placemaker. If nothing was found,
 * GeoNames is queried instead.
 * 
 * @author Oliver Zihler
 * @version 0.1
 * 
 */
public final class LocationFinder {
	/** First extractor to query */
	private IPlaceExtractor placeMaker;
	/** Second extractor to query */
	private IPlaceExtractor geoNames;

	/**
	 * Constructor
	 * 
	 * @param countryCode
	 * @param userName
	 * @param xmlPath
	 * @param stringRules
	 * @param boundsRules
	 */
	public LocationFinder(String countryCode, String userName, String xmlPath, List<String> stringRules, Polygon boundsRules) { 
		this.placeMaker = PEFactory.createPlaceExtractor(PEType.YPM,  userName, xmlPath);
		this.geoNames = PEFactory.createPlaceExtractor(PEType.GN, userName, xmlPath);
	}

	/**
	 * Finds the location according to the specified query
	 * 
	 * @param query
	 *            place name to find
	 * @return MBRs of locations found
	 */
	public List<? extends Geometry> findLocation(String query) {
		List<? extends Geometry> locations = null;
		locations = placeMaker.extract(query);
//		System.out.println("LocationFinder:findLocation(): locations found with YPM: " +locations.size());
		if (locations.size() == 0) {
			locations = geoNames.extract(query);
		}
		return locations;
	}

}
