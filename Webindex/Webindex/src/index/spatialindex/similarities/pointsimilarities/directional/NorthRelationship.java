package index.spatialindex.similarities.pointsimilarities.directional;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

public class NorthRelationship extends AbstractDirectionalRelation {

	public NorthRelationship(float multiplicationFactor) {
		super(multiplicationFactor);
	}

	@Override
	protected Point getUpperRightOfNewQueryPolygon(Point c, Point ne) {
		return ne;
	}

	@Override
	protected Point getLowerLeftOfNewQueryPolygon(Point c, Point sw) {
		Point a = gF.createPoint(new Coordinate(sw.getX(), c.getY()));
		return a;
	}

	@Override
	protected boolean checkIfBelowLine(Point dfpCentroid, Point qfpCentroid) {
		return dfpCentroid.getY() < qfpCentroid.getY();
	}

	@Override
	protected Point createZeroPoint(Point dfpCentroid, Point qfpCentroid) {
		Point zeroY = gF.createPoint(new Coordinate(qfpCentroid.getX(), dfpCentroid.getY()));
		return zeroY;
	}

}
