package index.textindex.similarities.vectorspacemodels;

import index.textindex.ITextIndex;
import index.textindex.similarities.AbstractTextSimilarity;
import index.textindex.similarities.tfidfweighting.DocTFIDFTypes;
import index.textindex.similarities.tfidfweighting.QueryIDFTypes;
import index.textindex.similarities.tfidfweighting.TFWeightingStrategy;
import index.textindex.utils.QueryFormatter;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocumentValues;
import index.textindex.utils.TextIndexDocumentMetaData;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.IndexDocument;
import index.utils.Ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public final class CosineSimilarity extends AbstractTextSimilarity {

	private TFWeightingStrategy queryTf;
	private QueryIDFTypes queryIdf;
	private DocTFIDFTypes docTfidf;

	public CosineSimilarity(ITextIndex index, ITextTokenizer queryTokenizer, TFWeightingStrategy queryTf, QueryIDFTypes queryIdf, DocTFIDFTypes docTfidf) {
		super(index, queryTokenizer);
		this.queryTf = queryTf;
		this.queryIdf = queryIdf;
		this.docTfidf = docTfidf;
	}

	@Override
	protected Ranking calculateSimilarity(String query, ArrayList<String> indexedTerms, boolean isIntersected) { 
		ArrayList<IndexDocument> documents = index.getDocTermKeyValues(indexedTerms, isIntersected); 
		HashMap<Term, Integer> termFreqs = queryTokenizer.transform(query);
		int maxFreq = getMaxFreq(termFreqs);
		calculateCosineSimilarity(termFreqs, maxFreq, documents); 
		Collections.sort(documents);
		return new Ranking(QueryFormatter.format(query, isIntersected), documents);
	}

	public static int getMaxFreq(HashMap<Term, Integer> termFreqs) {
		int maxFreq = Integer.MIN_VALUE;
		for(Term term: termFreqs.keySet()){
			int freq = termFreqs.get(term);
			if(maxFreq < freq){
				maxFreq = freq;
			}
		}
		return maxFreq;
	}
	
	private void calculateCosineSimilarity(HashMap<Term, Integer> queryTerms, int maxFreq, ArrayList<IndexDocument> documents) {
		HashMap<String, Integer> indexedTerms = new HashMap<String,Integer>();
		for(Term term: queryTerms.keySet()){
			indexedTerms.put(term.getIndexedTerm(), queryTerms.get(term));
		} 
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
			document.getTextIndexDocumentMetaData().setSimilarity(sumWeight / getVectorNorm(document));
		}
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
