package index.textindex.similarities.probabilisticmodels;

import index.textindex.similarities.ITextSimilarity;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocumentValues;
import index.textindex.utils.TextIndexMetaData;
import index.utils.IndexDocument;
import index.utils.Ranking;
import index.utils.Score;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Calculates similarity values accordings to BM formulas. See Modern Information Retrieval Ed. 2, page 106. The implementation needs one of the
 * IBMStrategies (BM1, BM15, BM11, or BM25) as an input strategy.
 * 
 * @author rsp
 *
 */
public class BestMatch implements ITextSimilarity {

	private TextIndexMetaData metaData;
	private float N;
	private IBMStrategy bmStrategy;

	public BestMatch(TextIndexMetaData metaData, IBMStrategy bmStrategy) { 
		this.bmStrategy = bmStrategy;
		this.metaData = metaData;
		N = metaData.getN();
	}

	@Override
	public Ranking calculateSimilarity(TextIndexQuery query, HashMap<Term, Integer> queryTermFreqs, ArrayList<IndexDocument> documents, boolean isIntersected) { 
		ArrayList<Score> scores = new ArrayList<Score>();
	 	for (IndexDocument document : documents) {
			float sumWeight = 0f;
			for (Term queryTerm : queryTermFreqs.keySet()) {
				TermDocumentValues values = document.getTextIndexDocumentMetaData().get(queryTerm.getIndexedTerm());
				if (values != null) {
					float ni = values.getNi();
					float value = getLog(ni);
					sumWeight += bmStrategy.calculateSimilarity(value, values, document, metaData);
				}
				scores.add(new Score(document.getId(), sumWeight));
			} 
			
		}
		Collections.sort(scores);
		return new Ranking(scores); 
		 
	}


	private float getLog(float ni) {
		return (float) ((N - ni + 0.5) / (ni + 0.5));
	}

}
