package index.textindex.similarities.tfidfweighting;

public interface IDFWeightingStrategy {

	float idf(long docFreq, long numDocs);

}
