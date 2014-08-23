package index.textindex.similarities.probabilisticmodels;

import index.textindex.utils.TextIndexMetaData;
import index.textindex.utils.TermDocumentValues;
import index.utils.IndexDocument;

public class BM1 extends AbstractBMStrategy {

	@Override
	public float calculateSimilarity(float value, TermDocumentValues values, IndexDocument document, TextIndexMetaData metaData) {
		return log(value);
	}
 

	

}
