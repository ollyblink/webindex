package index.girindex.combinationstrategy.combfamily;

import index.utils.Document;
import index.utils.Score;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Geometry;

public class CombSum extends AbstractComb {

	@Override
	protected Score calculateCombinedScore(ArrayList<Score> scoresOfDoc) {
		float sum = 0f;
		Document document = null;
		Geometry docGeom = null;
		for (Score score : scoresOfDoc) {
			sum += score.getScore(); 
			if (document == null) {
				document = score.getDocument();
			}
			if (docGeom == null) {
				docGeom = score.getGeometry();
			}
		} 
		return new Score(document, sum, docGeom);
	}

}
