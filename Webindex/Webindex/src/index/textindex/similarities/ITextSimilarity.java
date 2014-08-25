package index.textindex.similarities;

import java.util.ArrayList;
import java.util.HashMap;

import index.textindex.utils.Term;
import index.utils.IndexDocument;
import index.utils.Ranking;
import index.utils.query.TextIndexQuery;

public interface ITextSimilarity {
	/**
	 * Calculates a float score between a query and indexed documents. If isIntersected is enabled, only documents containing all the terms are being
	 * evaluated. Else, all the documents that contain at least one of the query terms are evaluated.
	 * 
	 * @return
	 */
	public Ranking calculateSimilarity(TextIndexQuery query, HashMap<Term, Integer> queryTermFreqs, ArrayList<IndexDocument> documents, boolean isIntersected);
}