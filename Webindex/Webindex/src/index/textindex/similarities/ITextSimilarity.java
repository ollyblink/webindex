package index.textindex.similarities;

import index.utils.Ranking;

public interface ITextSimilarity {
	/**
	 * Calculates a float score between a query and indexed documents. If isIntersected is enabled, only documents containing all the terms are being evaluated. Else, all the documents that contain at least one of the query terms are evaluated.
	 * @param query
	 * @param isIntersected
	 * @return
	 */
	public Ranking calculateSimilarity(String query, boolean isIntersected);
}
