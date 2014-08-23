package index.spatialindex.similarities.pointsimilarities;

import index.spatialindex.similarities.ISpatialRelationship;
import index.spatialindex.utils.SpatialScoreTriple;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public class InRelationship implements ISpatialRelationship {

	@Override
	public void calculateSimilarity(List<? extends Geometry> queryFootPrints, List<SpatialScoreTriple> documentFootPrints) {
		for (Geometry qFP : queryFootPrints) {
			for (SpatialScoreTriple dFP : documentFootPrints) {
				if (qFP.contains(dFP.getGeometry())) {
					dFP.setScore(1f);
				}
			}
		} 
	}
}
