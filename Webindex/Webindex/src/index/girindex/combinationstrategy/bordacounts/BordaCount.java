package index.girindex.combinationstrategy.bordacounts;

import index.girindex.combinationstrategy.AbstractCombinationStrategy;
import index.utils.Document;
import index.utils.GeometryWrapper;

import java.util.ArrayList;
import java.util.Collections;

import rest.dao.RESTScore;
import rest.dao.Ranking;

public class BordaCount extends AbstractCombinationStrategy {

	@Override
	protected ArrayList<Ranking> applyPreCalculations(Ranking finalRanking,  Ranking... rankings) {
		int maxCount = Integer.MIN_VALUE;
		
		ArrayList<Ranking> rankingList = new ArrayList<>();
		for (Ranking ranking : rankings) { 
			int rankSize = ranking.getResults().size();
			if (maxCount < rankSize) {
				maxCount = rankSize;
			}
			rankingList.add(ranking);
		}
		   
		// Assign score according to maxScore to the rankings.
		for (Ranking ranking : rankingList) { 
			ArrayList<RESTScore> actualResults = ranking.getResults();
			// make sure they are sorted...
			Collections.sort(actualResults);
			// Assign borda count to each score
			float startScore = maxCount;
			for (RESTScore score : actualResults) {
				score.setScore(startScore);
				--startScore;
			}
		}
 
		return rankingList;
	}

	@Override
	protected RESTScore calculateCombinedScore(ArrayList<RESTScore> scoresOfDoc) {
		if (scoresOfDoc != null) {
			float finalScore = 0f;
			Document document = null;
			GeometryWrapper docGeom = null;
			for (RESTScore score : scoresOfDoc) {
				finalScore  += score.getScore();
				if (document == null) {
					document = score.getDocument();
				}
				if(docGeom == null){
					docGeom = score.getGeometry();
				}
			} 
			 
			return new RESTScore(document, finalScore,docGeom);
		} else {
			return new RESTScore();
		}
	}

}
