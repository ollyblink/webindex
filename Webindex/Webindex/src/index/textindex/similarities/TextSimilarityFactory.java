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
import index.textindex.utils.TextIndexMetaData;

public class TextSimilarityFactory {

	private TextSimilarityFactory() {

	}

	public static ITextSimilarity getSimilarity(String type, TextIndexMetaData metaData) {
		switch (type) {
		case "bm1":
			return new BestMatch(metaData, new BM1());
		case "bm11":
			return new BestMatch(metaData, new BM11(1.2f));

		case "bm15":
			return new BestMatch(metaData, new BM15(1.2f));

		case "bm25":
			return new BestMatch(metaData, new BM25(1.2f, 0.75f));

		case "cosine1":
			return new CosineSimilarity(new Formula1TFStrategy(), QueryIDFTypes.TERM_IDF1, DocTFIDFTypes.DOC_TFIDF1);

		case "cosine2":
			return new CosineSimilarity(new Formula2TFStrategy(), QueryIDFTypes.TERM_IDF2, DocTFIDFTypes.DOC_TFIDF2);

		case "cosine3":
		default:
			return new CosineSimilarity(new Formula3TFStrategy(), QueryIDFTypes.TERM_IDF1, DocTFIDFTypes.DOC_TFIDF3);

		}
	}
}
