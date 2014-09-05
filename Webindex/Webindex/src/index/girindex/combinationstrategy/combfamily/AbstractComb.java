package index.girindex.combinationstrategy.combfamily;

import index.girindex.combinationstrategy.AbstractCombinationStrategy;
import index.girindex.combinationstrategy.utils.Normalizer;

import java.util.ArrayList;

import rest.dao.RESTScore;
import rest.dao.Ranking;

public  abstract class AbstractComb extends AbstractCombinationStrategy {

	protected void normalize(ArrayList<Ranking> rankingList) {
		// Score normalization
		for (Ranking ranking : rankingList) {
			ArrayList<RESTScore> normalizeMinMax = Normalizer.normalizeMinMax(ranking.getResults());
			ranking.setResults(normalizeMinMax);
		}
	}

	@Override
	protected ArrayList<Ranking> applyPreCalculations(Ranking finalRanking,  Ranking... rankings) {
		ArrayList<Ranking> rankingList = new ArrayList<>();
		for (Ranking ranking : rankings) {  
			rankingList.add(ranking);
		} 
		normalize(rankingList);
		return rankingList;
	}

	
 
	 
}
