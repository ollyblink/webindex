package index.textindex.similarities.tfidfweighting;


public interface TFWeightingStrategy {
 
	/** 
	 * @param freq fij/fiq
	 * @param maxFreq the maximal frequency of a term occuring in a document or query. It iss only used if the tf-calculation needs a maximum frequency value as e.g. formual 1 query term weight in Modern Information retrieval Table 3.6, page 74.
	 * @return
	 */
	public float tf(int freq, int maxFreq);
  

}
