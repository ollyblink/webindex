package testutils;
import org.objectweb.asm.Type;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;


public class SwissProvider {
	  
	public static Coordinate[] getSwissCoordinates() {
		Coordinate[] swissCoords = new Coordinate[5];
		double minY = 45.818;
		double maxY = 47.8084;
		double minX = 5.95587;
		double maxX = 10.492;
		swissCoords[0] = new Coordinate(minX, minY);
		swissCoords[1] = new Coordinate(maxX, minY);
		swissCoords[2] = new Coordinate(maxX, maxY);
		swissCoords[3] = new Coordinate(minX, maxY);
		swissCoords[4] = new Coordinate(minX, minY);
		return swissCoords;
	}

	public static GeometryFactory getPolygonFactory() { 
		return new GeometryFactory(new PrecisionModel(Type.DOUBLE), 4326);
	}
	
	public static Polygon getSwitzerlandMBR(){
		return getPolygonFactory().createPolygon(getSwissCoordinates());
	}
}
