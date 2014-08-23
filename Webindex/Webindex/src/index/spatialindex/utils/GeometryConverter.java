package index.spatialindex.utils;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;
import org.postgis.LinearRing;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

public class GeometryConverter {

	public static Geometry convertPostGisToJTS(PGgeometry pgGeom) throws NoSuchObjectException {
		org.postgis.Geometry pgGeometry = pgGeom.getGeometry();
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(Type.DOUBLE), pgGeometry.getSrid());
		List<Coordinate> coordinates = new ArrayList<Coordinate>();

		for (int i = 0; i < pgGeometry.numPoints(); ++i) {
			Point point = pgGeometry.getPoint(i);
			coordinates.add(new Coordinate(point.x, point.y));
		}
		switch (pgGeometry.getType()) {
		case org.postgis.Geometry.POINT:
			return factory.createPoint(coordinates.get(0));
		case org.postgis.Geometry.POLYGON:
			Coordinate[] coords = new Coordinate[coordinates.size()];
			for(int i = 0; i < coordinates.size();++i){
				coords[i] = coordinates.get(i);
			}
			return factory.createPolygon(coords);
		default:
			throw new NoSuchObjectException("Was not a point nor a polygon.");
		}
	}

	public static PGgeometry convertJTStoPostGis(Geometry jtsGeometry) throws NoSuchObjectException {
		org.postgis.Geometry geom = null;
		switch (jtsGeometry.getClass().getSimpleName()) {
		case "Point":
			geom = new Point(jtsGeometry.getCoordinates()[0].x, jtsGeometry.getCoordinates()[0].y);
			geom.setSrid(jtsGeometry.getSRID());
			return new PGgeometry(geom);
		case "Polygon":
			Point[] points = new Point[5];
			if (points.length != jtsGeometry.getNumPoints()) {
				throw new NoSuchObjectException("Polygon had not 5 coordinates");
			}
			for (int i = 0; i < jtsGeometry.getNumPoints(); ++i) {
				Coordinate coord = jtsGeometry.getCoordinates()[i];
				points[i] = new Point(coord.x, coord.y);
			}
			geom = new Polygon(new LinearRing[] { new LinearRing(points) });
			geom.setSrid(jtsGeometry.getSRID());
			return new PGgeometry(geom);
		default:
			throw new NoSuchObjectException("Was not a point nor a polygon.");
		}
	}

}
