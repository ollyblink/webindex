package index.textindex.utils;

/**
 * Implements db table metadata
 * 
 * @author rsp
 *
 */
public class TextIndexMetaData {
	private float averageDocLengthSizeInBytes;
	private float averageDocLengthRawNrOfWords;
	private float averageDocLengthIndexedNrOfTerms;
	private float averageDocLengthVectorNorm1;
	private float averageDocLengthVectorNorm2;
	private float averageDocLengthVectorNorm3;
	private int N;

	public TextIndexMetaData(float averageDocLengthSizeInBytes, float averageDocLengthRawNrOfWords, float averageDocLengthIndexedNrOfTerms, float averageDocLengthVectorNorm1, float averageDocLengthVectorNorm2, float averageDocLengthVectorNorm3, int n) {
		this.averageDocLengthSizeInBytes = averageDocLengthSizeInBytes;
		this.averageDocLengthRawNrOfWords = averageDocLengthRawNrOfWords;
		this.averageDocLengthIndexedNrOfTerms = averageDocLengthIndexedNrOfTerms;
		this.averageDocLengthVectorNorm1 = averageDocLengthVectorNorm1;
		this.averageDocLengthVectorNorm2 = averageDocLengthVectorNorm2;
		this.averageDocLengthVectorNorm3 = averageDocLengthVectorNorm3;
		N = n;
	}

	public float getAverageDocLengthSizeInBytes() {
		return averageDocLengthSizeInBytes;
	}

	public void setAverageDocLengthSizeInBytes(float averageDocLengthSizeInBytes) {
		this.averageDocLengthSizeInBytes = averageDocLengthSizeInBytes;
	}

	public float getAverageDocLengthRawNrOfWords() {
		return averageDocLengthRawNrOfWords;
	}

	public void setAverageDocLengthRawNrOfWords(float averageDocLengthRawNrOfWords) {
		this.averageDocLengthRawNrOfWords = averageDocLengthRawNrOfWords;
	}

	public float getAverageDocLengthIndexedNrOfTerms() {
		return averageDocLengthIndexedNrOfTerms;
	}

	public void setAverageDocLengthIndexedNrOfTerms(float averageDocLengthIndexedNrOfTerms) {
		this.averageDocLengthIndexedNrOfTerms = averageDocLengthIndexedNrOfTerms;
	}

	public float getAverageDocLengthVectorNorm1() {
		return averageDocLengthVectorNorm1;
	}

	public void setAverageDocLengthVectorNorm1(float averageDocLengthVectorNorm1) {
		this.averageDocLengthVectorNorm1 = averageDocLengthVectorNorm1;
	}

	public float getAverageDocLengthVectorNorm2() {
		return averageDocLengthVectorNorm2;
	}

	public void setAverageDocLengthVectorNorm2(float averageDocLengthVectorNorm2) {
		this.averageDocLengthVectorNorm2 = averageDocLengthVectorNorm2;
	}

	public float getAverageDocLengthVectorNorm3() {
		return averageDocLengthVectorNorm3;
	}

	public void setAverageDocLengthVectorNorm3(float averageDocLengthVectorNorm3) {
		this.averageDocLengthVectorNorm3 = averageDocLengthVectorNorm3;
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

}
