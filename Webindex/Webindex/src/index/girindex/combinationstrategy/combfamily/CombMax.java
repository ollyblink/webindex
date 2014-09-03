package index.girindex.combinationstrategy.combfamily;

import index.utils.Document;
import index.utils.Score;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Geometry;

public class CombMax extends AbstractComb {

	@Override
	protected Score calculateCombinedScore(ArrayList<Score> scoresOfDoc) {
		float max = Float.MIN_VALUE;
		Document document = null;
		Geometry docGeom = null;
		for (Score score : scoresOfDoc) {
			if (max < score.getScore()) {
				max = score.getScore();
			}
			if (document == null) {
				document = score.getDocument();
			}
			if (docGeom == null) {
				docGeom = score.getGeometry();
			}
		}

		return new Score(document, max, docGeom);
	}

}
