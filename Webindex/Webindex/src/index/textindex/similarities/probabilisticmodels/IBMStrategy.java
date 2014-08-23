package index.textindex.similarities.probabilisticmodels;

import index.textindex.utils.TextIndexMetaData;
import index.textindex.utils.TermDocumentValues;
import index.utils.IndexDocument;

public interface IBMStrategy {

	public float calculateSimilarity(float value, TermDocumentValues values, IndexDocument document, TextIndexMetaData metaData);

}
