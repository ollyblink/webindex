package index.utils.documenttransformation.spatialtransformation;

import index.spatialindex.utils.geolocating.georeferencing.LocationFinder;
import index.utils.documenttransformation.AbstractTransformationStage;
import index.utils.documenttransformation.ExtractionRequest;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public class GeoReferencingStage extends AbstractTransformationStage {
	

	

	private LocationFinder locationFinder;

	public GeoReferencingStage(LocationFinder locationFinder, boolean isShowTransformationEnabled) {
		super(isShowTransformationEnabled);
		this.locationFinder = locationFinder;
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
 
		request.addTransformationStage(getClass().getSimpleName(), foundGeometries);
		super.handleRequest(request);
	}

	 
}
