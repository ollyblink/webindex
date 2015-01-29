package index.spatialindex.utils.geolocating.georeferencing;

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
public class LocationFinder {
	private List<IPlaceExtractor> extractors;

	public LocationFinder(List<IPlaceExtractor> extractors) {
		this.extractors = extractors;
	}

	/**
	 * Default
	 */
	public LocationFinder() {
		// Add default extractors
		List<IPlaceExtractor> extractors = new ArrayList<IPlaceExtractor>();
		extractors.add(new YPMPlaceExtractor("ypm.xml"));
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
