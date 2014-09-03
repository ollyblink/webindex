package index.textindex.similarities;

import index.textindex.similarities.probabilisticmodels.BM1;
import index.textindex.similarities.probabilisticmodels.BM11;
import index.textindex.similarities.probabilisticmodels.BM15;
import index.textindex.similarities.probabilisticmodels.BM25;
import index.textindex.similarities.probabilisticmodels.BestMatch;
import index.textindex.similarities.tfidfweighting.DocTFIDFTypes;
import index.textindex.similarities.tfidfweighting.Formula1TFStrategy;
import index.textindex.similarities.tfidfweighting.Formula2TFStrategy;
import index.textindex.similarities.tfidfweighting.Formula3TFStrategy;
import index.textindex.similarities.tfidfweighting.QueryIDFTypes;
import index.textindex.similarities.vectorspacemodels.CosineSimilarity;
import index.textindex.utils.Term;
import index.utils.Document;
import index.utils.Score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractTextSimilarity implements ITextSimilarity {
	protected Map<Long, Score> scoreMap = new HashMap<Long, Score>();

	protected void updateScore(Document document, float weight) {
		Score score = scoreMap.get(document.getId().getId());
		if (score == null) {
			score = new Score(document, 0f, null);
			scoreMap.put(document.getId().getId(), score);
		}
		float newWeight = score.getScore() + weight;
		score.setScore(newWeight);
	}
//	public HashMap<Score, Document> getFinalScoreMap(HashMap<Term, List<Document>> relevantDocuments, ArrayList<Score> scoreList) {
//		Set<Document> docSet = new HashSet<>();
//		for(List<Document> docs:relevantDocuments.values()){
//			docSet.addAll(docs);
//		}
//		
//		HashMap<Score, Document> finalScore = new HashMap<Score, Document>();
//		for(Score score: scoreList){
//			for(Document doc: docSet){
//				if(doc.getId().getId().equals(score.getDocument())){
//					finalScore.put(score, doc);
//				}
//			}
//		}
//		return finalScore;
//	}
	public static ITextSimilarity getSimilarity(String similarity) {
		switch (similarity) {
		case "bm1":
			return new BestMatch(new BM1());
		case "bm11":
			return new BestMatch(new BM11());
		case "bm15":
			return new BestMatch(new BM15());
		case "bm25":
			return new BestMatch(new BM25());
		case "cosine1": //TODO check!!
			return new CosineSimilarity(new Formula1TFStrategy(), QueryIDFTypes.TERM_IDF1, DocTFIDFTypes.DOC_TFIDF1);
		case "cosine2"://TODO check!!
			return new CosineSimilarity(new Formula2TFStrategy(), QueryIDFTypes.TERM_IDF2, DocTFIDFTypes.DOC_TFIDF2);
		case "cosine3":
		default:
			return new CosineSimilarity(new Formula3TFStrategy(), QueryIDFTypes.TERM_IDF1, DocTFIDFTypes.DOC_TFIDF3);
		}
	}
}
