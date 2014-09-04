package index.textindex.similarities.booleanmodels;

import index.textindex.similarities.ITextSimilarity;
import index.textindex.utils.Term;
import index.utils.Document;
import index.utils.Score;
import index.utils.indexmetadata.TextIndexMetaData;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleBooleanSimilarity implements ITextSimilarity {

	@Override
	public ArrayList<Score> calculateSimilarity(TextIndexQuery query, HashMap<Term, Integer> queryTermFreqs, HashMap<Term, List<Document>> relevantDocuments, TextIndexMetaData metaData) {
		Set<Document> relevantDocs = new HashSet<Document>();

		for (Term queryTerm : relevantDocuments.keySet()) {
			// System.out.println(queryTerm + ", " +actualTerm);
			List<Document> list = relevantDocuments.get(queryTerm);
			if (list != null) {
				relevantDocs.addAll(list);
			}
		}

		ArrayList<Score> scoreList = new ArrayList<Score>();
		for (Document doc : relevantDocs) {
			scoreList.add(new Score(doc, 1f, null));
		}
	 

		return scoreList;
	}

}
