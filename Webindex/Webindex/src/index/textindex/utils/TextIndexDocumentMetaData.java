package index.textindex.utils;

import index.utils.ISimilarityProvider;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TextIndexDocumentMetaData implements Comparable<TextIndexDocumentMetaData>, ISimilarityProvider {

	private int sizeInBytes;
	private int indexedNrOfWords;
	private int rawNrOfWords;
	private float docVectorNorm1;
	private float docVectorNorm2;
	private float docVectorNorm3;
	private Map<String, TermDocumentValues> termToDocVals;
	private Float similarity = 0f;
	private long docid;
	
	/**
	 * just for xmlrootelement
	 */
	public TextIndexDocumentMetaData(){
		
	}

	public TextIndexDocumentMetaData(long docid, int sizeInBytes, int indexedNrOfWords, int rawNrOfWords, float docVectorNorm1, float docVectorNorm2, float docVectorNorm3) {
		this(docid);
		this.sizeInBytes = sizeInBytes;
		this.indexedNrOfWords = indexedNrOfWords;
		this.rawNrOfWords = rawNrOfWords;
		this.docVectorNorm1 = docVectorNorm1;
		this.docVectorNorm2 = docVectorNorm2;
		this.docVectorNorm3 = docVectorNorm3;
		this.termToDocVals = new HashMap<String, TermDocumentValues>();
	}

	public TextIndexDocumentMetaData(long docid) {
		this.docid = docid;
	}

	public Map<String, TermDocumentValues> getTermToDocVals() {
		return termToDocVals;
	}

	public void setTermToDocVals(Map<String, TermDocumentValues> termToDocVals) {
		this.termToDocVals = termToDocVals;
	}

	public long getDocid() {
		return docid;
	}

	public void setDocid(long docid) {
		this.docid = docid;
	}

	public int getSizeInBytes() {
		return sizeInBytes;
	}

	public void setSizeInBytes(int sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}

	public int getIndexedNrOfWords() {
		return indexedNrOfWords;
	}

	public void setIndexedNrOfWords(int indexedNrOfWords) {
		this.indexedNrOfWords = indexedNrOfWords;
	}

	public int getRawNrOfWords() {
		return rawNrOfWords;
	}

	public void setRawNrOfWords(int rawNrOfWords) {
		this.rawNrOfWords = rawNrOfWords;
	}

	public float getDocVectorNorm1() {
		return docVectorNorm1;
	}

	public void setDocVectorNorm1(float docVectorNorm1) {
		this.docVectorNorm1 = docVectorNorm1;
	}

	public float getDocVectorNorm2() {
		return docVectorNorm2;
	}

	public void setDocVectorNorm2(float docVectorNorm2) {
		this.docVectorNorm2 = docVectorNorm2;
	}

	public float getDocVectorNorm3() {
		return docVectorNorm3;
	}

	public void setDocVectorNorm3(float docVectorNorm3) {
		this.docVectorNorm3 = docVectorNorm3;
	}

	public Map<String, TermDocumentValues> getTermDocValues() {
		return termToDocVals;
	}

	public void setTermDocValues(HashMap<String, TermDocumentValues> termToDocVals) {
		this.termToDocVals = termToDocVals;
	}
	
	@Override
	public Float getSimilarity() {
		return similarity;
	}

	public void setSimilarity(Float similarity) {
		this.similarity = similarity;
	}

	@Override
	public int compareTo(TextIndexDocumentMetaData iD) {
		return -(new Float(similarity).compareTo(iD.similarity));
	}

	public void addTermDocValues(String term, TermDocumentValues values) {
		this.termToDocVals.put(term, values);
	}

	public TermDocumentValues get(String term) {
		return termToDocVals.get(term);
	}
}
