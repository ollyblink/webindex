package index.spatialindex.similarities.pointsimilarities;

import index.spatialindex.similarities.AbstractSpatialRelationship;
import index.spatialindex.utils.SpatialDocument;
import index.utils.Score;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;

public class BufferedNearRelationship extends AbstractSpatialRelationship {

	private static final double SCALING_FACTOR = 20;
	protected GeometricShapeFactory gF;

	public BufferedNearRelationship() {
		this.gF = new GeometricShapeFactory();
	}

	@Override
	public ArrayList<Geometry> preCalculateQueryFootprint(ArrayList<Geometry> queryFootPrints) {
		ArrayList<Geometry> alteredGeoms = new ArrayList<Geometry>();
		for (Geometry geom : queryFootPrints) {
			if (geom.getClass().equals("Polygon")) {
				Point ne = getNorthEast(geom.getCoordinates());
				gF.setCentre(geom.getCentroid().getCoordinate());
				gF.setSize(geom.distance(ne));
				
				Polygon circle = gF.createCircle();
				circle.setSRID(geom.getSRID());
				
				alteredGeoms.add(circle);
			}
		}
		return alteredGeoms;
	}

	private Point getNorthEast(Coordinate[] cs) {

		Coordinate maxCoord = cs[0];

		for (int i = 1; i < cs.length; ++i) {
			if (cs[i].x > maxCoord.x && cs[i].y > maxCoord.y) {
				maxCoord = cs[i];
			}
		}
		return new GeometryFactory().createPoint(maxCoord);
	}

	@Override
	protected void calculateSimilarity(ArrayList<Score> results, Geometry qFP, SpatialDocument dFP) {
		Point ne = getNorthEast(qFP.getCoordinates());
		double distance = ne.distance(qFP.getCentroid());
		Geometry buffer = qFP.buffer(distance/SCALING_FACTOR);
		if(buffer.contains(dFP.getDocumentFootprint())){
			super.checkLargestScore(results, dFP, 1f);
		}
	}

}
