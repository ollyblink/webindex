package index.textindex.similarities.probabilisticmodels;

import index.textindex.utils.TermDocs;
import index.utils.Document;
import index.utils.indexmetadata.OverallTextIndexMetaData;


public class BM25 extends AbstractBMStrategy {

	private float K1;
	private float b;

	public BM25(float k1, float b) {
		K1 = k1;
		this.b = b;
	}

	@Override
	public float calculateSimilarity(float value, Document document, TermDocs termDocMetaData, OverallTextIndexMetaData indexMetaData) {
		float fij = termDocMetaData.getFij();
		float inside = (1-b) + b*(document.getIndexedNrOfWords()/indexMetaData.getAvgDocLengthIndexedNrOfWords());
		float denominator = (K1*inside)+fij;
		float numberator = (K1+1)*fij; 
		float Bij = denominator/numberator;
		return Bij*log(value); 
	}

}
