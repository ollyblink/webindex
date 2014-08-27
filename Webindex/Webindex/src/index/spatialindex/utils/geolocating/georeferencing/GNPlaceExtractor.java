package index.spatialindex.utils.geolocating.georeferencing;

import java.util.ArrayList;
import java.util.List;

import org.geonames.InsufficientStyleException;
import org.geonames.Style;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;
import org.objectweb.asm.Type;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * This class queries GeoNames to retrieve locations for place queries.
 * 
 * @author Oliver Zihler
 * @version 0.1
 * 
 */
public class GNPlaceExtractor implements IPlaceExtractor {
	/** Limits the number of returned places */
	private static final int NUMBER_OF_MAX_ROWS = 20;
	/** The earths approximated radius for calculations of the approximated MBR */
	private static final double APPROXIMATED_EARTH_RADIUS = 6378137;
	/** The slope of the linear regression to approximate the MBR */
	private static final double SLOPE = 318.491;
	/** The constant of the linear regression to approximate the MBR */
	private static final double CONSTANT = 62685293.12;
	/** The user name for GeoNames */
	public static String DEFAULT_USER_NAME = "ollyblink";

	// /** Limits the country to the specified code */
	// private String countryCode;

	/**
	 * Constructor
	 * 
	 * @param countryCode
	 *            the specified country
	 * @param userName
	 *            the user name for GeoNames
	 */
	public GNPlaceExtractor(String userName) {
		WebService.setUserName(userName);
	}

	@Override
	public ArrayList<Geometry> extract(String placeNametext) {
		ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
		searchCriteria.setQ(placeNametext);

		// try {
		// searchCriteria.setCountryCode(countryCode);
		// } catch (InvalidParameterException e1) {
		// e1.printStackTrace();
		// }
		searchCriteria.setMaxRows(NUMBER_OF_MAX_ROWS);
		searchCriteria.setStyle(Style.FULL);
		ArrayList<Geometry> list = new ArrayList<Geometry>();

		ToponymSearchResult searchResult = null;

		try {
			searchResult = WebService.search(searchCriteria);
		} catch (Exception e) {
			System.out.println("Couldn't find the location " + placeNametext);
			e.printStackTrace();
			return list;
		}

		// System.out.println("Number of returned codes: "+searchResult.getTotalResultsCount());

		for (Toponym toponym : searchResult.getToponyms()) {
			if (!toponym.getName().toLowerCase().contains(placeNametext.toLowerCase())) {
				continue;
			}
			// try {
			double lat = toponym.getLatitude();
			double lon = toponym.getLongitude();
			/*
			 * Below is some stuff I used in my master thesis to estimate the area from the population to get an MBR of the location... 
			 */
			// Integer pop = toponym.getPopulation();
			// if (pop == null)
			// continue;
			//
			// double area = getApproximatedAreaFromPopulation(pop);
			// double dist = getApproximatedRadiusFromArea(area);
			//
			// double latNE = getApproximatedNorthEastLatitude(lat, dist);
			// double lonNE = getApproximatedNorthEastLongitude(lon, lat, dist);
			// double latSW = getApproximatedSouthWestLatitude(lat, dist);
			// double lonSW = getApproximatedSouthWestLongitude(lon, lat, dist);

			Coordinate point = new Coordinate(lon, lat);
			list.add(new GeometryFactory(new PrecisionModel(Type.DOUBLE), 4326).createPoint(point));
			//
			// } catch (InsufficientStyleException e) {
			// e.printStackTrace();
			// }
		}

		return list;
	}

	/*
	 * Some linear regression calculations
	 */
	private double getApproximatedAreaFromPopulation(double population) {
		return ((SLOPE * (population)) + CONSTANT);
	}

	private double getApproximatedRadiusFromArea(double area) {
		return Math.sqrt((area / Math.PI));
	}

	private static double getApproximatedNorthEastLatitude(double latInDeg, double radius) {
		return latInDeg + ((180 / Math.PI) * (radius / APPROXIMATED_EARTH_RADIUS));
	}

	private static double getApproximatedNorthEastLongitude(double lonInDeg, double latInDeg, double radius) {
		return lonInDeg + (((180 / Math.PI) * (radius / APPROXIMATED_EARTH_RADIUS)) / Math.cos(Math.toRadians(latInDeg)));
	}

	private static double getApproximatedSouthWestLatitude(double latInDeg, double radius) {
		return latInDeg - ((180 / Math.PI) * (radius / APPROXIMATED_EARTH_RADIUS));
	}

	private static double getApproximatedSouthWestLongitude(double lonInDeg, double latInDeg, double radius) {
		return lonInDeg - (((180 / Math.PI) * (radius / APPROXIMATED_EARTH_RADIUS)) / Math.cos(Math.toRadians(latInDeg)));
	}
}
