package index.textindex.similarities.vectorspacemodels;

import index.textindex.similarities.AbstractTextSimilarity;
import index.textindex.similarities.tfidfweighting.DocTFIDFTypes;
import index.textindex.similarities.tfidfweighting.QueryIDFTypes;
import index.textindex.similarities.tfidfweighting.TFWeightingStrategy;
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
import java.util.Map;

public final class CosineSimilarity extends AbstractTextSimilarity {

	private TFWeightingStrategy queryTf;
	private QueryIDFTypes queryIdf;
	private DocTFIDFTypes docTfidf;

	public CosineSimilarity(TFWeightingStrategy queryTf, QueryIDFTypes queryIdf, DocTFIDFTypes docTfidf) {
		this.queryTf = queryTf;
		this.queryIdf = queryIdf;
		this.docTfidf = docTfidf;
	}

	@Override
	public ArrayList<Score> calculateSimilarity(TextIndexQuery query, HashMap<Term, Integer> queryTermFreqs, HashMap<Term, List<Document>> relevantDocuments, TextIndexMetaData metaData) {

		ArrayList<Score> scoreList = calculateCosineSimilarity(queryTermFreqs, getMaxFreq(queryTermFreqs), relevantDocuments, metaData);

		Collections.sort(scoreList);
		return scoreList;
	}

	private ArrayList<Score> calculateCosineSimilarity(HashMap<Term, Integer> queryTermFreqs, int maxFreq, HashMap<Term, List<Document>> relevantDocuments, TextIndexMetaData metaData) {

		Map<Long, Float> vectorNormsPerDoc = new HashMap<Long, Float>();

		for (Term queryTerm : queryTermFreqs.keySet()) {
			Term actualTerm = findTerm(relevantDocuments, queryTerm);
			// System.out.println(queryTerm + ", " +actualTerm);
			List<Document> list = relevantDocuments.get(actualTerm);
			if (list != null) {
				for (Document document : list) { 
					TermDocsIdentifier id = new TermDocsIdentifier(queryTerm.getIndexedTerm().getTermId(), document.getId().getId());
					TermDocs termDocs = metaData.getTermDocRelationship().get(id);
					float docTfIdf = getDocTfIdf(termDocs); 
					int freq = queryTermFreqs.get(queryTerm); 
					float queryTfIdf = queryTf.tf(freq, maxFreq) * getQueryIdf(actualTerm);
					float weight = docTfIdf * queryTfIdf;
					updateScore(document, weight);

					Float vectorNorm = vectorNormsPerDoc.get(document.getId().getId());
					if (vectorNorm == null) {
						vectorNorm = getVectorNorm(document);
						vectorNormsPerDoc.put(document.getId().getId(), vectorNorm);
					}
				}
			}
		}

		// Normalize all scores by their vector norm
		for (Long id : scoreMap.keySet()) {
			Score score = scoreMap.get(id);
			score.setScore(score.getScore() / vectorNormsPerDoc.get(id));
		}
		ArrayList<Score> scoreList = new ArrayList<Score>(scoreMap.values());
		Collections.sort(scoreList);

		return scoreList;
	}

	private Term findTerm(HashMap<Term, List<Document>> relevantDocuments, Term queryTerm) {
		for (Term term : relevantDocuments.keySet()) {
			if (term.equals(queryTerm)) {
				return term;
			}
		}
		return null;
	}

	private float getVectorNorm(Document document) {
		switch (docTfidf) {
		case DOC_TFIDF1:
			return document.getDocVectorNorm1();
		case DOC_TFIDF2:
			return document.getDocVectorNorm2();
		case DOC_TFIDF3:
		default:
			return document.getDocVectorNorm3();
		}
	}

	private float getDocTfIdf(TermDocs tds) {
		switch (docTfidf) {
		case DOC_TFIDF1:
			return tds.getDocTermTfidf1();
		case DOC_TFIDF2:
			return tds.getDocTermTfidf2();
		case DOC_TFIDF3:
		default:
			return tds.getDocTermTfidf3();
		}
	}

	private float getQueryIdf(Term term) {
		switch (queryIdf) {
		case TERM_IDF1:
			return term.getTermIdf1();
		case TERM_IDF2:
		default:
			return term.getTermIdf2();
		}
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

}
