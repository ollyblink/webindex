package index.spatialindex.similarities.pointsimilarities;

import index.spatialindex.similarities.AbstractSpatialRelationship;
import index.spatialindex.utils.SpatialDocument;
import index.utils.Score;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public abstract class AbstractDirectionalRelation extends AbstractSpatialRelationship {

	protected GeometryFactory gF;

	public AbstractDirectionalRelation() {
		this.gF = new GeometryFactory();
	}

	@Override
	protected void calculateSimilarity(ArrayList<Score> results, Geometry qFP, SpatialDocument dFP) { 
		Point dfpCentroid = dFP.getDocumentFootprint().getCentroid();

		Point qfpCentroid = qFP.getCentroid();
		if(checkIfBelowLine(dfpCentroid, qfpCentroid)) {
			return;
		}
		Point zeroY = gF.createPoint(new Coordinate(qfpCentroid.getX(), dfpCentroid.getY()));

		double adjacent = qfpCentroid.distance(zeroY);
		double hypothenuse = qfpCentroid.distance(dfpCentroid);

		double alpha = Math.acos(adjacent / hypothenuse);

		float directionalScore = 1 - (float) ((alpha / Math.toRadians(45)));
		if (directionalScore > 0f) { 
			checkLargestScore(results, dFP, directionalScore);
		}
	}

	protected abstract boolean checkIfBelowLine(Point dfpCentroid, Point qfpCentroid);

	protected abstract Point getSouthWest(Coordinate[] cs, double centroidY);

	protected abstract Point getNorthEast(Coordinate[] cs);

	

}
