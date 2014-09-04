package index.textindex.similarities.probabilisticmodels;

import index.textindex.similarities.AbstractTextSimilarity;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocs;
import index.utils.Document;
import index.utils.Score;
import index.utils.identifers.TermDocsIdentifier;
import index.utils.indexmetadata.TextIndexMetaData;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import rest.dao.Ranking;

/**
 * Calculates similarity values accordings to BM formulas. See Modern Information Retrieval Ed. 2, page 106. The implementation needs one of the IBMStrategies (BM1, BM15, BM11, or BM25) as an input strategy.
 * 
 * @author rsp
 *
 */
public class BestMatch extends AbstractTextSimilarity {

	private IBMStrategy bmStrategy;

	public BestMatch(IBMStrategy bmStrategy) {
		this.bmStrategy = bmStrategy;
	}

	@Override
	public ArrayList<Score> calculateSimilarity(TextIndexQuery query, HashMap<Term, Integer> queryTermFreqs, HashMap<Term, List<Document>> relevantDocuments, TextIndexMetaData metaData) {

		int N = metaData.getOverallIndexMetaData().getN();

		for (Term queryTerm : queryTermFreqs.keySet()) {
			List<Document> relevantDocumentsForTerm = relevantDocuments.get(queryTerm);

			if (relevantDocumentsForTerm != null) {
				for (Document document : relevantDocumentsForTerm) {

					float ni = findNi(queryTerm, relevantDocuments);

					float value = getLog(ni, N);
					TermDocs termDocs = metaData.getTermDocRelationship().get(new TermDocsIdentifier(queryTerm.getIndexedTerm().getTermId(), document.getId().getId()));
					float score = bmStrategy.calculateSimilarity(value, document, termDocs, metaData.getOverallIndexMetaData());

					updateScore(document, score);
				}
			}
		}

		ArrayList<Score> scoreList = new ArrayList<Score>(scoreMap.values());  

		return scoreList;

	}

	private float findNi(Term queryTerm, HashMap<Term, List<Document>> relevantDocuments) {
		for (Term term : relevantDocuments.keySet()) {
			if (term.equals(queryTerm)) {
				return term.getNi();
			}
		}
		return 0f;
	}

	private float getLog(float ni, float N) {
		return (float) ((N - ni + 0.5) / (ni + 0.5));
	}

}
