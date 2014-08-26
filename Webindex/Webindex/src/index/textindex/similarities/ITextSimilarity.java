package index.textindex.similarities;

import index.textindex.utils.Term;
import index.utils.Document;
import index.utils.Ranking;
import index.utils.indexmetadata.TextIndexMetaData;
import index.utils.query.TextIndexQuery;

import java.util.HashMap;
import java.util.List;

public interface ITextSimilarity {
	/**
	 * Calculates a float score between a query and indexed documents. If isIntersected is enabled, only documents containing all the terms are being
	 * evaluated. Else, all the documents that contain at least one of the query terms are evaluated.
	 * 
	 * @return
	 */
	public Ranking calculateSimilarity(TextIndexQuery query, HashMap<Term, Integer> queryTermFreqs, HashMap<Term, List<Document>> relevantDocuments, TextIndexMetaData metaData, boolean isIntersected);
}