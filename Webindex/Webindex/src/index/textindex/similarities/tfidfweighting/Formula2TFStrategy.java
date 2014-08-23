package index.textindex.similarities.tfidfweighting;

public class Formula2TFStrategy implements TFWeightingStrategy{

	
	@Override
	public float tf(int freq, int maxFreq) {
		return 1;
	}
 

}
