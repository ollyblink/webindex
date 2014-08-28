package index.spatialindex.similarities.pointsimilarities;

import index.spatialindex.similarities.ISpatialRelationship;
import index.spatialindex.utils.SpatialDocument;
import index.utils.Score;
import index.utils.SpatialScore;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public class InRelationship implements ISpatialRelationship {

	@Override
	public ArrayList<? extends Score> calculateSimilarity(final List<? extends Geometry> queryFootPrints, final List<SpatialDocument> documentFootPrints) {
		ArrayList<SpatialScore> results = new ArrayList<SpatialScore>();
		for (Geometry qFP : queryFootPrints) {
			for (SpatialDocument dFP : documentFootPrints) {
				if (qFP.contains(dFP.getDocumentFootprint())) { 
					results.add(new SpatialScore(dFP.getDocid().getDocId(), dFP.getDocumentFootprint(), 1f)); 
				} 
			}
		}

		return results;
	}
}
