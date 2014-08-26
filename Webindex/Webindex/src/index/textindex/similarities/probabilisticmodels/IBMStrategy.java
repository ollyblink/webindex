package index.textindex.similarities.probabilisticmodels;
 
import index.textindex.utils.TermDocs;
import index.utils.Document;
import index.utils.indexmetadata.OverallTextIndexMetaData;


public interface IBMStrategy {

	public float calculateSimilarity(float value, Document document, TermDocs termDocMetaData, OverallTextIndexMetaData indexMetaData);

}
