package index.textindex.similarities.probabilisticmodels;

import index.textindex.ITextIndex;
import index.textindex.similarities.AbstractTextSimilarity;
import index.textindex.utils.TextIndexMetaData;
import index.textindex.utils.QueryFormatter;
import index.textindex.utils.TermDocumentValues;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.IndexDocument;
import index.utils.Ranking;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Calculates similarity values accordings to BM formulas. See Modern Information Retrieval Ed. 2, page 106. The implementation needs one of the IBMStrategies (BM1, BM15, BM11, or BM25) as an input strategy.
 * 
 * @author rsp
 *
 */
public class BestMatch extends AbstractTextSimilarity {

	private TextIndexMetaData metaData;
	private float N;
	private IBMStrategy bmStrategy;

	public BestMatch(ITextIndex index, ITextTokenizer queryTokenizer, IBMStrategy bmStrategy) {
		super(index, queryTokenizer);
		this.bmStrategy = bmStrategy;
		metaData = index.getMetaData();
		N = metaData.getN();
	}

	@Override
	protected Ranking calculateSimilarity(String query, ArrayList<String> indexedTerms, boolean isIntersected) {
		ArrayList<IndexDocument> documents = index.getDocTermKeyValues(indexedTerms, isIntersected);
		for (IndexDocument document : documents) {
			float sumWeight = 0f;
			for (String queryTerm : indexedTerms) {
				TermDocumentValues values = document.getTextIndexDocumentMetaData().get(queryTerm);
				if (values != null) {
					float ni = values.getNi();
					float value = getLog(ni);
					sumWeight += bmStrategy.calculateSimilarity(value, values, document, metaData);
				}
				document.getTextIndexDocumentMetaData().setSimilarity(sumWeight);
			}
		}
		Collections.sort(documents);
		return new Ranking(QueryFormatter.format(query, isIntersected), documents);
	}

	private float getLog(float ni) {
		return (float) ((N - ni + 0.5) / (ni + 0.5));
	}

}
