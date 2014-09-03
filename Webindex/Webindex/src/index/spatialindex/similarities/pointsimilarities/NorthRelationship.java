package index.spatialindex.similarities.pointsimilarities;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class NorthRelationship extends AbstractDirectionalRelation{
	@Override
	public ArrayList<Geometry> preCalculateQueryFootprint(ArrayList<Geometry> queryFootPrints) {
		ArrayList<Geometry> alteredGeoms = new ArrayList<Geometry>();
		for (Geometry geom : queryFootPrints) {
			if (geom.getClass().equals("Polygon")) {
				Polygon polygon = (Polygon) geom;
				Point c = polygon.getCentroid();
				Coordinate[] coordinates = polygon.getCoordinates();
				Point ne = getNorthEast(coordinates);
				Point sw = getSouthWest(coordinates,c.getY());

				Point a = gF.createPoint(new Coordinate((sw.getX() - (c.getX() - sw.getX())), (c.getY())));
				Point b = gF.createPoint(new Coordinate((ne.getX() + (ne.getX() - c.getX())), (ne.getY() + (ne.getY() - c.getY()))));

				Coordinate[] newQueryPolygon = new Coordinate[5];
				newQueryPolygon[0] = a.getCoordinate();
				newQueryPolygon[1] = new Coordinate(b.getCoordinate().x, a.getCoordinate().y);
				newQueryPolygon[2] = b.getCoordinate();
				newQueryPolygon[3] = new Coordinate(a.getCoordinate().x, b.getCoordinate().y);
				newQueryPolygon[4] = a.getCoordinate();
				Polygon newQFP = gF.createPolygon(newQueryPolygon); 
				alteredGeoms.add(newQFP);
			}
		}
		return alteredGeoms;
	}
	
	

	@Override
	protected boolean checkIfBelowLine(Point dfpCentroid, Point qfpCentroid) {
		return dfpCentroid.getY() < qfpCentroid.getY();
	}



	@Override
	protected Point getSouthWest(Coordinate[] cs, double centroidY) {

		Coordinate minCoord = cs[0];

		for (int i = 1; i < cs.length; ++i) {
			if (cs[i].x < minCoord.x ) {
				minCoord = cs[i];
			}
		}
		return new GeometryFactory().createPoint(new Coordinate(minCoord.x, centroidY));
	}
  
	@Override
	protected Point getNorthEast(Coordinate[] cs) {

		Coordinate maxCoord = cs[0];

		for (int i = 1; i < cs.length; ++i) {
			if (cs[i].x > maxCoord.x && cs[i].y > maxCoord.y) {
				maxCoord = cs[i];
			}
		}
		return new GeometryFactory().createPoint(maxCoord);
	}
}
