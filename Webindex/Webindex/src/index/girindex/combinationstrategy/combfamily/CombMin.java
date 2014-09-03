package index.girindex.combinationstrategy.combfamily;

import index.utils.Document;
import index.utils.Score;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Geometry;

public class CombMin extends AbstractComb {

	@Override
	protected Score calculateCombinedScore(ArrayList<Score> scoresOfDoc) {
		float min = Float.MAX_VALUE;
		Document document = null;
		Geometry docGeom = null;
		for (Score score : scoresOfDoc) {
			if(min > score.getScore()){
				min = score.getScore();
			}
			if (document == null) {
				document = score.getDocument();
			}
			if (docGeom == null) {
				docGeom = score.getGeometry();
			}
		}

		return new Score(document, min,docGeom);
	}

}
