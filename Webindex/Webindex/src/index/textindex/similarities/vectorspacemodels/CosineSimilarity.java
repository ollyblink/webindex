package index.textindex.similarities.vectorspacemodels;

import index.textindex.similarities.ITextSimilarity;
import index.textindex.similarities.tfidfweighting.DocTFIDFTypes;
import index.textindex.similarities.tfidfweighting.QueryIDFTypes;
import index.textindex.similarities.tfidfweighting.TFWeightingStrategy;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocumentValues;
import index.textindex.utils.TextIndexDocumentMetaData;
import index.utils.IndexDocument;
import index.utils.Ranking;
import index.utils.Score;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.HashMap;

public final class CosineSimilarity implements ITextSimilarity {

	private TFWeightingStrategy queryTf;
	private QueryIDFTypes queryIdf;
	private DocTFIDFTypes docTfidf;

	public CosineSimilarity(TFWeightingStrategy queryTf, QueryIDFTypes queryIdf, DocTFIDFTypes docTfidf) {
		this.queryTf = queryTf;
		this.queryIdf = queryIdf;
		this.docTfidf = docTfidf;
	}

	@Override
	public Ranking calculateSimilarity(TextIndexQuery query, HashMap<Term, Integer> queryTermFreqs, ArrayList<IndexDocument> documents,
			boolean isIntersected) {

		int maxFreq = getMaxFreq(queryTermFreqs);
		ArrayList<Score> scores = calculateCosineSimilarity(queryTermFreqs, maxFreq, documents);

		Ranking ranking = new Ranking(scores);
		 
		return ranking;
	}

	public static int getMaxFreq(HashMap<Term, Integer> termFreqs) {
		int maxFreq = Integer.MIN_VALUE;
		for (Term term : termFreqs.keySet()) {
			int freq = termFreqs.get(term);
			if (maxFreq < freq) {
				maxFreq = freq;
			}
		}
		return maxFreq;
	}

	private ArrayList<Score> calculateCosineSimilarity(HashMap<Term, Integer> queryTerms, int maxFreq, ArrayList<IndexDocument> documents) {
		HashMap<String, Integer> indexedTerms = new HashMap<String, Integer>();
		for (Term term : queryTerms.keySet()) {
			indexedTerms.put(term.getIndexedTerm(), queryTerms.get(term));
		}
		
		ArrayList<Score> scores = new ArrayList<Score>();
		for (IndexDocument document : documents) {
			float sumWeight = 0f;
			for (String queryTerm : indexedTerms.keySet()) {
				TermDocumentValues values = document.getTextIndexDocumentMetaData().get(queryTerm);
				if (values != null) {
					float docTfIdf = getDocTfIdf(values);
					float queryTfIdf = queryTf.tf(indexedTerms.get(queryTerm), maxFreq) * getQueryIdf(values);
					sumWeight += docTfIdf * queryTfIdf;
				}
			}
			scores.add(new Score(document.getId(), (sumWeight / getVectorNorm(document))));
		}
		
		return scores;
	}

	private float getVectorNorm(IndexDocument document) {
		TextIndexDocumentMetaData meta = document.getTextIndexDocumentMetaData();
		switch (docTfidf) {
		case DOC_TFIDF1:
			return meta.getDocVectorNorm1();
		case DOC_TFIDF2:
			return meta.getDocVectorNorm2();
		case DOC_TFIDF3:
		default:
			return meta.getDocVectorNorm3();
		}
	}

	private float getDocTfIdf(TermDocumentValues values) {
		switch (docTfidf) {
		case DOC_TFIDF1:
			return values.getDocTfIdf1();
		case DOC_TFIDF2:
			return values.getDocTfIdf2();
		case DOC_TFIDF3:
		default:
			return values.getDocTfIdf3();
		}
	}

	private float getQueryIdf(TermDocumentValues values) {

		switch (queryIdf) {
		case TERM_IDF1:
			return values.getTermIdf1();
		case TERM_IDF2:
		default:
			return values.getTermIdf2();

		}
	}

}
