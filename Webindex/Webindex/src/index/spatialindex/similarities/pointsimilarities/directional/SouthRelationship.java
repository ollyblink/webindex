package index.spatialindex.similarities.pointsimilarities.directional;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

public class SouthRelationship extends AbstractDirectionalRelation {

	public SouthRelationship(float multiplicationFactor) {
		super(multiplicationFactor);
	}

	@Override
	protected Point getUpperRightOfNewQueryPolygon(Point c, Point ne) {
		Point a = gF.createPoint(new Coordinate(ne.getX(), c.getY()));
		return a;
	}

	@Override
	protected Point getLowerLeftOfNewQueryPolygon(Point c, Point sw) {
		return sw;
	}

	@Override
	protected boolean checkIfBelowLine(Point dfpCentroid, Point qfpCentroid) {
		return dfpCentroid.getY() > qfpCentroid.getY();
	}

	@Override
	protected Point createZeroPoint(Point dfpCentroid, Point qfpCentroid) {
		Point zeroY = gF.createPoint(new Coordinate(qfpCentroid.getX(), dfpCentroid.getY()));
		return zeroY;
	}

}
