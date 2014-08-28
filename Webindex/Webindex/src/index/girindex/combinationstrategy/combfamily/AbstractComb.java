package index.girindex.combinationstrategy.combfamily;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import index.girindex.combinationstrategy.ICombinationStrategy;
import index.girindex.combinationstrategy.utils.Normalizer;
import index.utils.Ranking;
import index.utils.Score;

public abstract class AbstractComb implements ICombinationStrategy {

	@Override
	public Ranking combineScores(boolean isIntersected, Ranking... rankings) {
		if (rankings == null || rankings.length == 0) {
			return new Ranking();
		} else {
			normalize(rankings);
			Map<Long, ArrayList<Score>> scoresPerDoc = getScoresPerDoc(rankings);

			// Finally, the actual score calculation
			ArrayList<Score> finalRanking = new ArrayList<Score>();
			for (Long docid : scoresPerDoc.keySet()) {
				ArrayList<Score> scoresOfDoc = scoresPerDoc.get(docid);
				if (isIntersected) {
					if (scoresOfDoc.size() != rankings.length) {
						continue; // Bouncer.. continue if intersected and not enough scores
					}
				}
				finalRanking.add(calculateCombinedScore(scoresOfDoc));
			}

			// Sort descending
			Collections.sort(finalRanking);

			return new Ranking(finalRanking);
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

	protected Map<Long, ArrayList<Score>> getScoresPerDoc(Ranking... rankings) {
		Map<Long/* docid */, ArrayList<Score> /* All scores of a doc */> scoresPerDoc = new HashMap<Long, ArrayList<Score>>();
		for (Ranking ranking : rankings) {
			ArrayList<Score> ranks = ranking.getResults();
			for (Score score : ranks) { 
				ArrayList<Score> scores = scoresPerDoc.get(score.getDocid());
				if (scores == null) {
					scores = new ArrayList<>();
					scoresPerDoc.put(score.getDocid(), scores);
				} 
				scores.add(score);
 
			}
		}
		return scoresPerDoc;
	}

}
