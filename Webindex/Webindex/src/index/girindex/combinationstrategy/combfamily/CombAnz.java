package index.girindex.combinationstrategy.combfamily;

import index.utils.Document;
import index.utils.GeometryWrapper;

import java.util.ArrayList;

import rest.dao.RESTScore;

public class CombAnz extends AbstractComb {

	@Override
	protected RESTScore calculateCombinedScore(ArrayList<RESTScore> scoresOfDoc) {
		float sum = 0f;
		Document document = null;
		GeometryWrapper docGeom = null;
		
		for (RESTScore score : scoresOfDoc) {
			sum += score.getScore();
			if (document == null) {
				document = score.getDocument();
			}
			if(docGeom == null){
				docGeom = score.getGeometry();
			}
		}

		float finalScore = sum /= scoresOfDoc.size();
		return new RESTScore(document, finalScore,docGeom);
	}

}
