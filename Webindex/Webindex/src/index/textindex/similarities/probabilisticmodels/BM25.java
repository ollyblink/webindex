package index.textindex.similarities.probabilisticmodels;

import index.textindex.utils.TextIndexMetaData;
import index.textindex.utils.TermDocumentValues;
import index.utils.IndexDocument;

public class BM25 extends AbstractBMStrategy {

	private float K1;
	private float b;

	public BM25(float k1, float b) {
		K1 = k1;
		this.b = b;
	}

	@Override
	public float calculateSimilarity(float value, TermDocumentValues values, IndexDocument document, TextIndexMetaData metaData) {
		float fij = values.getFij();
		float inside = (1-b) + b*(document.getTextIndexDocumentMetaData().getIndexedNrOfWords()/metaData.getAverageDocLengthIndexedNrOfTerms());
		float denominator = (K1*inside)+fij;
		float numberator = (K1+1)*fij; 
		float Bij = denominator/numberator;
		return Bij*log(value); 
	}

}
