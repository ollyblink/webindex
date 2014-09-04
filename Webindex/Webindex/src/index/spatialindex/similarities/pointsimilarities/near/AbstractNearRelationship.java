package index.spatialindex.similarities.pointsimilarities.near;

import index.spatialindex.similarities.AbstractSpatialRelationship;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;

public abstract class AbstractNearRelationship extends AbstractSpatialRelationship {
	protected GeometricShapeFactory gF;
	/** Factor used to increase the search radius for the circle. If 1, the circle is the Minimum bounding circle around the MBR */
	protected float multiplicationFactor;

	public AbstractNearRelationship(float multiplicationFactor) {
		this.gF = new GeometricShapeFactory();
		this.multiplicationFactor = multiplicationFactor;
	}
	@Override
	public ArrayList<Geometry> preCalculateQueryFootprint(ArrayList<Geometry> queryFootPrints) {
		ArrayList<Geometry> alteredGeoms = new ArrayList<Geometry>();
		for (Geometry geom : queryFootPrints) {
			if (geom.getClass().equals("Polygon")) {
				Point ne = getNorthEast(geom.getCoordinates());
				gF.setCentre(geom.getCentroid().getCoordinate());
				gF.setSize(geom.distance(ne)*multiplicationFactor);
				
				Polygon circle = gF.createCircle();
				circle.setSRID(geom.getSRID());
				
				alteredGeoms.add(circle);
			}
		}
		return alteredGeoms;
	}

	 
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
