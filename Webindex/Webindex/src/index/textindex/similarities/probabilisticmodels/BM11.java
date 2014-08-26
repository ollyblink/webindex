package index.textindex.similarities.probabilisticmodels;

import index.textindex.utils.TermDocs;
import index.utils.Document;
import index.utils.indexmetadata.OverallTextIndexMetaData;


public class BM11 extends AbstractBMStrategy {

	private float K1;
	

	public BM11(float K1) {
		this.K1 = K1;
	}


	@Override
	public float calculateSimilarity(float value, Document document, TermDocs termDocMetaData, OverallTextIndexMetaData indexMetaData) {
		float fij = termDocMetaData.getFij();
		return (((K1 + 1)*fij)/(((K1*document.getIndexedNrOfWords())/indexMetaData.getAvgDocLengthIndexedNrOfWords())+fij))*log(value);
	}

 

}
