package index.spatialindex.utils.placeextractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.objectweb.asm.Type;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * This class combines all the modules developed in this package so that a query and/or metadata of an image can be processed and the corresponding
 * locations are returned as <code>CoordinatePair</code>s
 * 
 * @author Oliver Zihler
 * 
 */
public class YPMPlaceExtractor implements IPlaceExtractor {
	/** URL for querying YPM */
	private static final String placeMakerUrl = "http://wherein.yahooapis.com/v1/document";
	/** Query method */
	private static final String queryMethod = "POST";
	/** User APP ID */
	private static final String APP_ID = "jeffreymcmanus";
	/** Type of document */
	private static final String DOCUMENT_TYPE = "text/plain";
	/** Parameters needed to query YPM */
	HashMap<String, String> placeMakerParameters;

	/** Where the xml is stored */
	private String xmlPath;
	/** the list of place names to validate */
	private List<String> currentPlaceNames;

	/**
	 * The constructor
	 * 
	 * @param xmlPath
	 *            the xml path
	 * @param stringRules
	 *            the place names to occur
	 * @param countryCode
	 *            the country to query
	 * @param boundsRules
	 *            the bounds within a location has to be
	 */
	public YPMPlaceExtractor(String xmlPath) {
		this.xmlPath = xmlPath;
		// Setting up the parameters for placemaker
		placeMakerParameters = new HashMap<String, String>();
		placeMakerParameters.put("appid", APP_ID);
		placeMakerParameters.put("documentType", DOCUMENT_TYPE);
		placeMakerParameters.put("documentContent", "");

		this.currentPlaceNames = new ArrayList<String>();
	}

	@Override
	public ArrayList<Geometry> extract(String placeNametext) {
		placeMakerParameters.remove("documentContent");
		// System.out.println(placeNametext);
		placeMakerParameters.put("documentContent", placeNametext);
		// System.out.println("Looking for "+placeNametext +" with YPM");
		YPMQuery.download(placeMakerUrl, xmlPath, queryMethod, placeMakerParameters);

		List<YPMPlace> validatedPlaces = YPMXMLParser.extractCandidatePlaces(xmlPath);
		ArrayList<Geometry> rectangleList = new ArrayList<Geometry>();
		for (YPMPlace place : validatedPlaces) {
			this.currentPlaceNames.add(place.getObjectName());
			Coordinate[] coords = new Coordinate[5];
			double lonSW = place.getSouthWest().getOrdinate(Coordinate.X);
			double latSW = place.getSouthWest().getOrdinate(Coordinate.Y);
			double lonNE = place.getNorthEast().getOrdinate(Coordinate.X);
			double latNE = place.getNorthEast().getOrdinate(Coordinate.Y);
			
			coords[0] = new Coordinate(lonSW, latSW);
			coords[1] = new Coordinate(lonNE, latSW);
			coords[2] = new Coordinate(lonNE, latNE);
			coords[3] = new Coordinate(lonSW, latNE);
			coords[4] = new Coordinate(lonSW, latSW);
			rectangleList.add(new GeometryFactory(new PrecisionModel(Type.DOUBLE), 4326).createPolygon(coords));
		}

		return rectangleList;
	}

	/**
	 * @return the list of place names that could be extracted from YPM (before validation).
	 */
	public List<String> getCurrentPlaceNamesFound() {
		return currentPlaceNames;
	}

}
