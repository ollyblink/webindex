package index.girindex.combinationstrategy.combfamily;

import index.utils.Document;
import index.utils.GeometryWrapper;

import java.util.ArrayList;

import rest.dao.RESTScore;

public class CombMin extends AbstractComb {

	@Override
	protected RESTScore calculateCombinedScore(ArrayList<RESTScore> scoresOfDoc) {
		float min = Float.MAX_VALUE;
		Document document = null;
		GeometryWrapper docGeom = null;
		for (RESTScore score : scoresOfDoc) {
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

		return new RESTScore(document, min,docGeom);
	}

}
