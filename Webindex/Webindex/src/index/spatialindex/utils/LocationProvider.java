package index.spatialindex.utils;

import index.spatialindex.utils.placeextractor.IPlaceExtractor;
import index.spatialindex.utils.placeextractor.PEFactory;
import index.spatialindex.utils.placeextractor.PEFactory.PEType;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public enum LocationProvider {

	INSTANCE;

	private IPlaceExtractor placeExtractor;

	private LocationProvider() {
		String userName = "ollyblink";
		String xmlLocation = System.getProperty("user.dir") + "/src/files/yahooxml.xml";
		PEType type = PEType.YPM;
		placeExtractor = PEFactory.createPlaceExtractor(type, userName, xmlLocation);
	}

	public List<? extends Geometry> retrieveLocations(String location) {
		return placeExtractor.extract(location);
	}

}
