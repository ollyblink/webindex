package index.utils.indexmetadata;

public class OverallTextIndexMetaData {
	private float avgDocLengthSizeInBytes;
	private float avgDocLengthRawNrOfWords;
	private float avgDocLengthIndexedNrOfWords;
	private float avgDocLengthVectorNorm1;
	private float avgDocLengthVectorNorm2;
	private float avgDocLengthVectorNorm3;
	private int N;
	 
	public OverallTextIndexMetaData(float avgDocLengthSizeInBytes, float avgDocLengthRawNrOfWords, float avgDocLengthIndexedNrOfWords,
			float avgDocLengthVectorNorm1, float avgDocLengthVectorNorm2, float avgDocLengthVectorNorm3, int n) {
		this.avgDocLengthSizeInBytes = avgDocLengthSizeInBytes;
		this.avgDocLengthRawNrOfWords = avgDocLengthRawNrOfWords;
		this.avgDocLengthIndexedNrOfWords = avgDocLengthIndexedNrOfWords;
		this.avgDocLengthVectorNorm1 = avgDocLengthVectorNorm1;
		this.avgDocLengthVectorNorm2 = avgDocLengthVectorNorm2;
		this.avgDocLengthVectorNorm3 = avgDocLengthVectorNorm3;
		N = n;
	}
	public float getAvgDocLengthSizeInBytes() {
		return avgDocLengthSizeInBytes;
	}
	public void setAvgDocLengthSizeInBytes(float avgDocLengthSizeInBytes) {
		this.avgDocLengthSizeInBytes = avgDocLengthSizeInBytes;
	}
	public float getAvgDocLengthRawNrOfWords() {
		return avgDocLengthRawNrOfWords;
	}
	public void setAvgDocLengthRawNrOfWords(float avgDocLengthRawNrOfWords) {
		this.avgDocLengthRawNrOfWords = avgDocLengthRawNrOfWords;
	}
	public float getAvgDocLengthIndexedNrOfWords() {
		return avgDocLengthIndexedNrOfWords;
	}
	public void setAvgDocLengthIndexedNrOfWords(float avgDocLengthIndexedNrOfWords) {
		this.avgDocLengthIndexedNrOfWords = avgDocLengthIndexedNrOfWords;
	}
	public float getAvgDocLengthVectorNorm1() {
		return avgDocLengthVectorNorm1;
	}
	public void setAvgDocLengthVectorNorm1(float avgDocLengthVectorNorm1) {
		this.avgDocLengthVectorNorm1 = avgDocLengthVectorNorm1;
	}
	public float getAvgDocLengthVectorNorm2() {
		return avgDocLengthVectorNorm2;
	}
	public void setAvgDocLengthVectorNorm2(float avgDocLengthVectorNorm2) {
		this.avgDocLengthVectorNorm2 = avgDocLengthVectorNorm2;
	}
	public float getAvgDocLengthVectorNorm3() {
		return avgDocLengthVectorNorm3;
	}
	public void setAvgDocLengthVectorNorm3(float avgDocLengthVectorNorm3) {
		this.avgDocLengthVectorNorm3 = avgDocLengthVectorNorm3;
	}
	public int getN() {
		return N;
	}
	public void setN(int n) {
		N = n;
	}
	
	
	
}
