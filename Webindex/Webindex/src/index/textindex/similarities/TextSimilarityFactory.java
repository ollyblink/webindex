package index.textindex.similarities;

import index.textindex.similarities.booleanmodels.SimpleBooleanSimilarity;
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
import index.textindex.similarities.vectorspacemodels.SimpleTFIDFSimilarity;

public class TextSimilarityFactory {

	private TextSimilarityFactory() {

	}

	public static ITextSimilarity create(String similarity) {
		switch (similarity) {
		case "simpleboolean":
			return new SimpleBooleanSimilarity();
		case "tfidf1":
			return new SimpleTFIDFSimilarity(DocTFIDFTypes.DOC_TFIDF1);
		case "tfidf2":
			return new SimpleTFIDFSimilarity(DocTFIDFTypes.DOC_TFIDF2);
		case "tfidf3":
			return new SimpleTFIDFSimilarity(DocTFIDFTypes.DOC_TFIDF3);
		case "bm1":
			return new BestMatch(new BM1());
		case "bm11":
			return new BestMatch(new BM11());
		case "bm15":
			return new BestMatch(new BM15());
		case "bm25":
			return new BestMatch(new BM25());
		case "cosine1": // TODO check!!
			return new CosineSimilarity(new Formula1TFStrategy(), QueryIDFTypes.TERM_IDF1, DocTFIDFTypes.DOC_TFIDF1);
		case "cosine2":// TODO check!!
			return new CosineSimilarity(new Formula2TFStrategy(), QueryIDFTypes.TERM_IDF2, DocTFIDFTypes.DOC_TFIDF2);
		case "cosine3":
		default:
			return new CosineSimilarity(new Formula3TFStrategy(), QueryIDFTypes.TERM_IDF1, DocTFIDFTypes.DOC_TFIDF3);
		}
	}
}
