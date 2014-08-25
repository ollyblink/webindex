package index.spatialindex.similarities.pointsimilarities;

import index.spatialindex.similarities.ISpatialRelationship;
import index.spatialindex.utils.SpatialScoreTriple;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public class InRelationship implements ISpatialRelationship {

	@Override
	public List<SpatialScoreTriple> calculateSimilarity(final List<? extends Geometry> queryFootPrints,
			final List<SpatialScoreTriple> documentFootPrints) {
		List<SpatialScoreTriple> results = new ArrayList<SpatialScoreTriple>();
		for (Geometry qFP : queryFootPrints) {
			for (SpatialScoreTriple dFP : documentFootPrints) {
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
