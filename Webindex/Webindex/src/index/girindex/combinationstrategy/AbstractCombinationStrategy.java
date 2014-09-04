package index.girindex.combinationstrategy;

import index.utils.Document;
import index.utils.query.GIRQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import rest.dao.RESTGIRQueryMetaData;
import rest.dao.RESTScore;
import rest.dao.Ranking;

public abstract class AbstractCombinationStrategy implements ICombinationStrategy {

	@Override
	public Ranking combineScores(GIRQuery query, Ranking... rankings) {
		if (rankings == null || rankings.length == 0) {
			return new Ranking();
		} else {
			Ranking finalRanking = new Ranking();

			ArrayList<Ranking> rankingList = applyPreCalculations(finalRanking, rankings);

			Map<Document, ArrayList<RESTScore>> scoresPerDoc = getScoresPerDoc(rankingList);
			ArrayList<RESTScore> finalScores = new ArrayList<>();
			for (Document doc : scoresPerDoc.keySet()) {
				ArrayList<RESTScore> scoresOfDoc = scoresPerDoc.get(doc);
				if (query.isIntersected()) {
					// System.out.println(doc.getId() + ": " +scoresOfDoc.size() +" != "+ rankings.length);
					if (scoresOfDoc.size() != rankings.length) {
						continue; // Bouncer.. continue if intersected and not enough scores
					}
				}
				RESTScore combinedScore = calculateCombinedScore(scoresOfDoc);

				finalScores.add(combinedScore);
			}

			Collections.sort(finalScores);

			RESTGIRQueryMetaData girQueryMetaData = new RESTGIRQueryMetaData();
			girQueryMetaData.setCombinationStrategy(getClass().getSimpleName());
			girQueryMetaData.setIntersected(query.isIntersected());
			girQueryMetaData.setScores(finalScores);
			finalRanking.setGirQueryMetaData(girQueryMetaData);

			return finalRanking;
		}
	}

	protected abstract ArrayList<Ranking> applyPreCalculations(Ranking finalRanking, Ranking... rankings);

	/**
	 * Hook method, to be implemented by subclasses...
	 * 
	 * @param scoresOfDoc
	 * @return
	 */
	protected abstract RESTScore calculateCombinedScore(ArrayList<RESTScore> scoresOfDoc);

	protected Map<Document, ArrayList<RESTScore>> getScoresPerDoc(ArrayList<Ranking> rankingList) {
		Map<Document, ArrayList<RESTScore> /* All scores of a doc */> scoresPerDoc = new HashMap<>();
		for (Ranking ranking : rankingList) {
			ArrayList<RESTScore> ranks = ranking.getResults();
			for (RESTScore score : ranks) {
				Document doc = score.getDocument();
				ArrayList<RESTScore> scores = scoresPerDoc.get(doc);
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
