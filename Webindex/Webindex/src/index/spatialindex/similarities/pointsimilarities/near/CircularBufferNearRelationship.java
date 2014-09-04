package index.spatialindex.similarities.pointsimilarities.near;

import index.spatialindex.utils.SpatialDocument;
import index.utils.Score;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class CircularBufferNearRelationship extends AbstractNearRelationship {

	/** describes the sice of the buffer in terms of the */
	private float scalingFactor;

	public CircularBufferNearRelationship(float multiplicationFactor, float scalingFactor) {
		super(multiplicationFactor);
		this.scalingFactor = scalingFactor;
	}

	@Override
	protected void calculateSimilarity(ArrayList<Score> results, Geometry qFP, SpatialDocument dFP) {
		Point ne = getNorthEast(qFP.getCoordinates());
		double distance = ne.distance(qFP.getCentroid());
		Geometry buffer = qFP.buffer(distance / scalingFactor);
		if (buffer.contains(dFP.getDocumentFootprint())) {
			super.checkLargestScore(results, dFP, 1f);
		}
	}

}
