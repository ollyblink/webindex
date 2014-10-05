package index.textindex.similarities.tfidfweighting;


public class Formula3TFStrategy implements TFWeightingStrategy{
	
	

	@Override
	public float tf(int freq,int maxFreq) {
		float tf = (float) (1.0f +  (Math.log(freq)/Math.log(2)) ); 
		return tf;
	} 
}
