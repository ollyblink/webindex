package index.spatialindex.utils.placeextractor;

import java.util.List;

import com.vividsolutions.jts.geom.Polygon;



/**
 * Factory used to instantiate the needed place extractor.
 * 
 * @author Oliver Zihler
 * @version 0.1
 * 
 */
public class PEFactory {
	public enum PEType {
		YPM, GN
	}

	private PEFactory() {
	}

	/**
	 * Creates a new place extractor according to the specified rules and type.
	 * @param type
	 * @param countryCode
	 * @param userName
	 * @param xmlPath
	 * @param boundsRules
	 * @param stringRules
	 * @return
	 */
	public static IPlaceExtractor createPlaceExtractor(PEType type, String userName, String xmlPath) {
		switch (type) {
			case GN :
				return new GNPlaceExtractor(userName);
			case YPM :
			default :
				return new YPMPlaceExtractor(xmlPath);
		}
	}
}
