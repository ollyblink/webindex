package index.spatialindex.utils.geolocating.georeferencing;

import index.spatialindex.utils.geolocating.georeferencing.PEFactory.PEType;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Queries first for places with Yahoo! Placemaker. If nothing was found, GeoNames is queried instead.
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
	public LocationFinder(String userName, String xmlPath) {
		this.placeMaker = PEFactory.createPlaceExtractor(PEType.YPM, userName, xmlPath);
		this.geoNames = PEFactory.createPlaceExtractor(PEType.GN, userName, xmlPath);
	}

	/**
	 * Finds the location according to the specified query
	 * 
	 * @param query
	 *            place name to find
	 * @return MBRs of locations found
	 */
	public List<Geometry> findLocation(String query) {
		List<Geometry> locations = new ArrayList<>();
		locations.addAll(placeMaker.extract(query));
		locations.addAll(geoNames.extract(query));
		return locations;
	}

}
