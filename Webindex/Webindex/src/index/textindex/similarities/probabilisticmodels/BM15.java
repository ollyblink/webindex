package index.textindex.similarities.probabilisticmodels;

import index.textindex.utils.TermDocs;
import index.utils.Document;
import index.utils.indexmetadata.OverallTextIndexMetaData;

public class BM15 extends AbstractBMStrategy {

	private float K1;

	public BM15(float K1) {
		this.K1 = K1;
	}

	public BM15() {
		this.K1 = 1.2f;
	}

	@Override
	public float calculateSimilarity(float value, Document document, TermDocs termDocMetaData, OverallTextIndexMetaData indexMetaData) {
		float fij = termDocMetaData.getFij();
		return (((K1 + 1f) * fij) / (K1 + fij)) * log(value);
	}

}
