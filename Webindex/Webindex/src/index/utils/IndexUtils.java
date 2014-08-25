package index.utils;

import index.girindex.combinationstrategy.ICombinationStrategy;
import index.girindex.utils.ScoreTuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndexUtils {

	public static Ranking combineRankings(List<Ranking> rankings, String comparableScoreType) {
		if (rankings == null || rankings.size() == 0) {
			return new Ranking();
		}
		Ranking finalRanking = new Ranking();
		List<IndexDocument> ranks = new ArrayList<>(finalRanking.getResults().keySet());

		// create the final ranking by adding all documents and evaluate the maximum rank of duplicated documents
		for (Ranking ranking : rankings) {
			List<IndexDocument> rankedDocuments = new ArrayList<>(ranking.getResults().keySet());

			for (IndexDocument document : rankedDocuments) {

				if (ranks.contains(document)) {
					// get the max uf the two document's rankings
					IndexDocument toCompare = getDocument(ranks, document);
					if (toCompare != null) {
						if (toCompare.hasHigherScoreThan(document, comparableScoreType)) {
							document.setHigherScore(toCompare, comparableScoreType);
						}
					}
				} else {
					// Does not contain the document. just add it.
					ranks.add(document);
				}

			}
		}
		return finalRanking;
	}

	private static IndexDocument getDocument(List<IndexDocument> ranks, IndexDocument document) {
		for (IndexDocument rank : ranks) {
			if (rank.equals(document)) {
				return rank;
			}
		}
		return null;
	}

	/**
	 * Used for comparisons in {@link ICombinationStrategy}
	 * 
	 * @param scores
	 * @param document
	 * @param similarityScore
	 */
	public static void createScoreTuple(Map<IndexDocument, ScoreTuple> scores, IndexDocument document, float similarityScore, String type) {
		ScoreTuple tuple = scores.get(document);
		if (tuple == null) {
			tuple = new ScoreTuple();
			scores.put(document, tuple);
		}
		switch (type) {
		case "text":
			tuple.setTextScore(similarityScore);
			break;
		case "space":
			tuple.setSpatialScore(similarityScore);
			break;
		}

	}
}
