package index.spatialindex.similarities;

import index.spatialindex.utils.SpatialDocument;
import index.utils.Score;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public abstract class AbstractSpatialRelationship implements ISpatialRelationship {

	@Override
	public ArrayList<Score> calculateSimilarity(final List<? extends Geometry> queryFootPrints, final List<SpatialDocument> documentFootPrints) {
		ArrayList<Score> results = new ArrayList<Score>();
		for (Geometry qFP : queryFootPrints) {
			for (SpatialDocument dFP : documentFootPrints) {
				calculateSimilarity(results, qFP, dFP);
			}
		}

		return results;
	}

	/**
	 * This method is used to only add the geometry with the maximal score to the ranking. There can always be only one geometry for a document that contributes to the score
	 * 
	 * @param results
	 * @param dFP
	 * @param scoreValue
	 */
	protected void checkLargestScore(ArrayList<Score> results, SpatialDocument dFP, Float scoreValue) {
		Score score = new Score(dFP.getDocument(), scoreValue, dFP.getDocumentFootprint());
		boolean found = false;
		for (Score s : results) {
			if (s.getDocument().equals(score.getDocument())) {
				s.setGeometry(score.getGeometry());
				s.setScore(scoreValue);
				found = true;
				break;
			}
		}
		if (!found) {
			results.add(score);
		}

	}

	protected abstract void calculateSimilarity(ArrayList<Score> results, Geometry qFP, SpatialDocument dFP);


}
