package index.spatialindex.utils;

import static org.junit.Assert.assertTrue;
import index.spatialindex.utils.geolocating.georeferencing.IPlaceExtractor;
import index.spatialindex.utils.geolocating.georeferencing.LocationFinder;
import index.spatialindex.utils.geolocating.georeferencing.YPMPlaceExtractor;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class LocationProviderTest {

	@Test
	public void testCountries() {

		// Create location finder
		LocationFinder finder = new LocationFinder();
		List<Geometry> germany = finder.findLocation("Germany");

		for (Geometry g : germany) {
			Coordinate[] coordinates = g.getCoordinates();
			for (Coordinate c : coordinates) {
				assertTrue((c.x > 5 || c.x < 16) && (c.y > 47 || c.y < 56));
			}
		}

		List<Geometry> switzerland = finder.findLocation("Switzerland");

		for (Geometry sw : switzerland) {
			Coordinate[] coordinates = sw.getCoordinates();
			for (Coordinate c : coordinates) {
				assertTrue((c.y > 45.68 || c.y < 47.93) && (c.x > 5.71 || c.x < 10.69));
			}
		}
	}

}
