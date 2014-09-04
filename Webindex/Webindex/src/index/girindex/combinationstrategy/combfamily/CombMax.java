package index.girindex.combinationstrategy.combfamily;

import index.utils.Document;
import index.utils.GeometryWrapper;

import java.util.ArrayList;

import rest.dao.RESTScore;

public class CombMax extends AbstractComb {

	@Override
	protected RESTScore calculateCombinedScore(ArrayList<RESTScore> scoresOfDoc) {
		float max = Float.MIN_VALUE;
		Document document = null;
		GeometryWrapper docGeom = null;
		for (RESTScore score : scoresOfDoc) {
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

		return new RESTScore(document, max, docGeom);
	}

}
