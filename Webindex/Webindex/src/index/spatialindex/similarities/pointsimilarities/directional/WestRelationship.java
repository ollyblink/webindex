package index.spatialindex.similarities.pointsimilarities.directional;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

public class WestRelationship extends AbstractDirectionalRelation {

	public WestRelationship(float multiplicationFactor) {
		super(multiplicationFactor);
	}
	@Override
	protected Point getUpperRightOfNewQueryPolygon(Point c, Point ne) {
		Point a = gF.createPoint(new Coordinate(c.getX(), ne.getY()));
		return a;
	}

	@Override
	protected boolean checkIfBelowLine(Point dfpCentroid, Point qfpCentroid) {
		return dfpCentroid.getX() > qfpCentroid.getX();
	}

	@Override
	protected Point getLowerLeftOfNewQueryPolygon(Point c, Point sw) {
		return sw;
	}

	@Override
	protected Point createZeroPoint(Point dfpCentroid, Point qfpCentroid) {
		Point zeroY = gF.createPoint(new Coordinate(dfpCentroid.getX(), qfpCentroid.getY()));
		return zeroY;
	}


}