package index.girindex.combinationstrategy.combfamily;

import index.girindex.combinationstrategy.ICombinationStrategy;
import index.girindex.combinationstrategy.utils.Normalizer;
import index.utils.Document;
import index.utils.Ranking;
import index.utils.RankingMetaData;
import index.utils.Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractComb implements ICombinationStrategy {

	@Override
	public Ranking combineScores(boolean isIntersected, Ranking... rankings) {
		if (rankings == null || rankings.length == 0) {
			return new Ranking();
		} else {

			ArrayList<Ranking> rankingList = new ArrayList<>();
			for(Ranking ranking: rankings){
				rankingList.add(ranking);
			}
			RankingMetaData meta = new RankingMetaData(getClass().getSimpleName(), rankingList);
			
			normalize(rankings);
			 
			Map<Document, ArrayList<Score>> scoresPerDoc = getScoresPerDoc(rankings);
			ArrayList<Score> finalRanking = new ArrayList<>();
			for (Document doc : scoresPerDoc.keySet()) {
				ArrayList<Score> scoresOfDoc = scoresPerDoc.get(doc);
				System.out.println("Is intersected? "+isIntersected);
				if (isIntersected) {
					if (scoresOfDoc.size() != rankings.length) {
						continue; // Bouncer.. continue if intersected and not enough scores
					}
				}
				Score combinedScore = calculateCombinedScore(scoresOfDoc);
				
				finalRanking.add(combinedScore);
			} 
			 
			Collections.sort(finalRanking);
			return new Ranking(finalRanking,meta);
		}
	}

	/**
	 * Hook method, to be implemented by subclasses...
	 * 
	 * @param scoresOfDoc
	 * @return
	 */
	protected abstract Score calculateCombinedScore(ArrayList<Score> scoresOfDoc);

	protected void normalize(Ranking... rankings) {
		// Score normalization
		for (Ranking ranking : rankings) {
			ArrayList<Score> normalizeMinMax = Normalizer.normalizeMinMax(ranking.getResults());  
			ranking.setResults(normalizeMinMax);
		}
	}

	protected Map<Document, ArrayList<Score>> getScoresPerDoc(Ranking... rankings) {
		Map<Document, ArrayList<Score> /* All scores of a doc */> scoresPerDoc = new HashMap<>();
		for (Ranking ranking : rankings) {

			ArrayList<Score> ranks = ranking.getResults() ;

			for (Score score : ranks) {
				Document doc = score.getDocument();
				ArrayList<Score> scores = scoresPerDoc.get(doc);
				if (scores == null) {
					scores = new ArrayList<>();
					scoresPerDoc.put(doc, scores);
				}
				scores.add(score);
			}
		}
		return scoresPerDoc;
	}

}
