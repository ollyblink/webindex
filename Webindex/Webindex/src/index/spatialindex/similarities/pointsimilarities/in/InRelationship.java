package index.spatialindex.similarities.pointsimilarities.in;

import index.spatialindex.similarities.AbstractSpatialRelationship;
import index.spatialindex.utils.SpatialDocument;
import index.utils.Score;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Geometry;

public class InRelationship extends AbstractSpatialRelationship {

	@Override
	protected void calculateSimilarity(ArrayList<Score> results, Geometry qFP, SpatialDocument dFP) {
		if (qFP.contains(dFP.getDocumentFootprint())) {
			checkLargestScore(results, dFP, 1f);
		}
	}

	@Override
	public ArrayList<Geometry> preCalculateQueryFootprint(ArrayList<Geometry> queryFootPrints) {
		return queryFootPrints; //Nothing to do here.
	}

}
