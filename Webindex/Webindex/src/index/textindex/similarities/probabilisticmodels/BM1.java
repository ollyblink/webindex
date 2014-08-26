package index.textindex.similarities.probabilisticmodels;

import index.textindex.utils.TermDocs;
import index.utils.Document;
import index.utils.indexmetadata.OverallTextIndexMetaData;


public class BM1 extends AbstractBMStrategy {

	@Override
	public float calculateSimilarity(float value, Document document, TermDocs termDocMetaData, OverallTextIndexMetaData indexMetaData) {
		return log(value);
	}
 

	

}
