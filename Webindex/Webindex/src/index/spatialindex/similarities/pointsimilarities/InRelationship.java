package index.spatialindex.similarities.pointsimilarities;

import index.spatialindex.similarities.ISpatialRelationship;
import index.utils.SpatialScore;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public class InRelationship implements ISpatialRelationship {

	@Override
	public List<SpatialScore> calculateSimilarity(final List<? extends Geometry> queryFootPrints,
			final List<SpatialScore> documentFootPrints) {
		List<SpatialScore> results = new ArrayList<SpatialScore>();
		for (Geometry qFP : queryFootPrints) {
			for (SpatialScore dFP : documentFootPrints) {
				if (qFP.contains(dFP.getGeometry())) {
					// DEBUG:: System.out.println(dFP.getDocid()+"::"+dFP.getGeometry() +" is inside " +qFP);
					dFP.setScore(1f);
					results.add(dFP);
				}
			}
		}

		return results;
	}
}
