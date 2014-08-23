package index.textindex.similarities.tfidfweighting;

public class DefaultIDFStrategy implements IDFWeightingStrategy {
	@Override
	public float idf(long docFreq, long numDocs) {
		float idf = (float) (Math.log(((float)numDocs)/((float)docFreq))/Math.log(2) ); 
		return idf;
	}
}
