package index.textindex.similarities.vectorspacemodels;

import index.textindex.similarities.AbstractTextSimilarity;
import index.textindex.similarities.tfidfweighting.DocTFIDFTypes;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleTFIDFSimilarity extends AbstractTextSimilarity {
 
	private DocTFIDFTypes docTfidf;

	public SimpleTFIDFSimilarity(DocTFIDFTypes docTfidf) { 
		this.docTfidf = docTfidf;
	}

	@Override
	public ArrayList<Score> calculateSimilarity(TextIndexQuery query, HashMap<Term, Integer> queryTermFreqs, HashMap<Term, List<Document>> relevantDocuments, TextIndexMetaData metaData) {
  
		Set<Score> scores = new HashSet<Score>();
		for (Term queryTerm : queryTermFreqs.keySet()) {
		 
			List<Document> list = relevantDocuments.get(queryTerm);
			if (list != null) {
				for (Document document : list) {
					TermDocsIdentifier id = new TermDocsIdentifier(queryTerm.getIndexedTerm().getTermId(), document.getId().getId());
					TermDocs termDocs = metaData.getTermDocRelationship().get(id);
					float docTfIdf = getDocTfIdf(termDocs);
					scores.add(new Score(document, docTfIdf, null));
				}
			}
		}

		ArrayList<Score> scoreList = new ArrayList<Score>();
		scoreList.addAll(scores);
		Collections.sort(scoreList);
		return scoreList;
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

}
