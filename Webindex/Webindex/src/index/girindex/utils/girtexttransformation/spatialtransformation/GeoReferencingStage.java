package index.girindex.utils.girtexttransformation.spatialtransformation;

import index.girindex.utils.girtexttransformation.AbstractTransformationStage;
import index.girindex.utils.girtexttransformation.ExtractionRequest;
import index.spatialindex.utils.geolocating.georeferencing.LocationFinder;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public class GeoReferencingStage extends AbstractTransformationStage {
	private static final String YPM_XML = System.getProperty("user.dir") + "/files/ypm.xml";
	private static final String USERNAME = "ollyblink";

	private LocationFinder locationFinder;

	public GeoReferencingStage() {
		this.locationFinder = new LocationFinder(USERNAME, YPM_XML);
	}

	@Override
	public void handleRequest(ExtractionRequest request) {
		List<Geometry> foundGeometries = new ArrayList<>();
		this.beforeTransformation = new ArrayList<Geometry>();
		@SuppressWarnings("unchecked")
		List<String> locations = (List<String>) request.getTransformationStage(precursor.getClass().getSimpleName());
		for (String location : locations) {
			foundGeometries.addAll(locationFinder.findLocation(location));
		}
		this.afterTransformation = foundGeometries;

		for (Geometry location : foundGeometries) {
			System.out.println(location);
		}
		super.handleRequest(request);
	}

	public static void main(String[] args) throws ClassNotFoundException {
		List<String> locations = new ArrayList<String>();
		locations.add("New York City,New York,United States");
		locations.add("New York,United States");
		locations.add("New Jersey,United States");
		locations.add("London,Greater London,United Kingdom");
		locations.add("Switzerland");
		locations.add("New York");
		locations.add("London");
		locations.add("Kloten, Switzerland");
		locations.add("New Jersey");
		
		GeoReferencingStage stage = new GeoReferencingStage();
		stage.setPrecursor(new GeoTaggingStage());
		ExtractionRequest request = new ExtractionRequest("");
		request.addTransformationStage(new GeoTaggingStage().getClass().getSimpleName(), locations);
		stage.handleRequest(request);
	}
}
