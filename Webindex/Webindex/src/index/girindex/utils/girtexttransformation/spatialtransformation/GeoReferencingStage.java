package index.girindex.utils.girtexttransformation.spatialtransformation;

import index.girindex.utils.girtexttransformation.AbstractTransformationStage;
import index.girindex.utils.girtexttransformation.ExtractionRequest;
import index.spatialindex.utils.geolocating.georeferencing.LocationFinder;
import index.textindex.utils.informationextractiontools.ITextInformationExtractor;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public class GeoReferencingStage extends AbstractTransformationStage {
	

	

	public GeoReferencingStage(boolean isShowTransformationEnabled ) {
		super(isShowTransformationEnabled);
	}

	@Override
	public void handleRequest(ExtractionRequest request) {
		List<Geometry> foundGeometries = new ArrayList<>();
		this.beforeTransformation = new ArrayList<Geometry>();
		@SuppressWarnings("unchecked")
		List<String> locations = (List<String>) request.getTransformationStage(precursor.getClass().getSimpleName());
		for (String location : locations) {
			foundGeometries.addAll(LocationFinder.INSTANCE.findLocation(location));
		}
		this.afterTransformation = foundGeometries;
 
		request.addTransformationStage(getClass().getSimpleName(), foundGeometries);
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
		
		GeoReferencingStage stage = new GeoReferencingStage(true);
		stage.setPrecursor(new GeoTaggingStage(true));
		ExtractionRequest request = new ExtractionRequest("");
		request.addTransformationStage(new GeoTaggingStage(true).getClass().getSimpleName(), locations);
		stage.handleRequest(request);
	}
}
