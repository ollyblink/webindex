package index.textindex.similarities.tfidfweighting;

public class Formula1TFStrategy implements TFWeightingStrategy {

	 

	@Override
	public float tf(int freq,int maxFreq) {
		float ratio = ((float)freq)/((float)maxFreq);
		ratio *= 0.5;
		ratio += 0.5;
		return ratio;
	}
 
	
	

}
