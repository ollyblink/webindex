package index.textindex.similarities.probabilisticmodels;

import index.textindex.utils.TextIndexMetaData;
import index.textindex.utils.TermDocumentValues;
import index.utils.IndexDocument;

public class BM15 extends AbstractBMStrategy {

	private float K1;

	
	public BM15(float K1) {
		this.K1 = K1;
	}


	@Override
	public float calculateSimilarity(float value, TermDocumentValues values, IndexDocument document, TextIndexMetaData metaData) {
		float fij = values.getFij();
		return (((K1 + 1f)*fij)/(K1 +fij))*log(value);
	}

 

}
