package index.textindex.utils;

import index.utils.identifers.TermIdentifier;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Term {
	 
	private TermIdentifier indexedTerm;
	private ArrayList<String> originalTerms;
	private int ni;
	private float termIdf1;
	private float termIdf2;
	
	public Term(String indexedTerm, ArrayList<String> originalTerms){
		this(indexedTerm);
		this.originalTerms = originalTerms;
	}
	
	public Term(String indexedTerm){ 
		this.indexedTerm = new TermIdentifier(indexedTerm);
	}

	public Term(String indexedTerm, ArrayList<String> originalTerms, int ni, float termIdf1, float termIdf2) {

		this(indexedTerm, originalTerms);
		this.originalTerms = originalTerms;
		this.ni = ni;
		this.termIdf1 = termIdf1;
		this.termIdf2 = termIdf2;
	}

	public TermIdentifier getIndexedTerm() {
		return indexedTerm;
	}

	public void setIndexedTerm(String indexedTerm) {
		this.indexedTerm = new TermIdentifier(indexedTerm);
	}

	public ArrayList<String> getOriginalTerms() {
		return originalTerms;
	}

	public void setOriginalTerms(ArrayList<String> originalTerms) {
		this.originalTerms = originalTerms;
	}

	public int getNi() {
		return ni;
	}

	public void setNi(int ni) {
		this.ni = ni;
	}

	public float getTermIdf1() {
		return termIdf1;
	}

	public void setTermIdf1(float termIdf1) {
		this.termIdf1 = termIdf1;
	}

	public float getTermIdf2() {
		return termIdf2;
	}

	public void setTermIdf2(float termIdf2) {
		this.termIdf2 = termIdf2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((indexedTerm == null) ? 0 : indexedTerm.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		if (indexedTerm == null) {
			if (other.indexedTerm != null)
				return false;
		} else if (!indexedTerm.equals(other.indexedTerm))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Term [indexedTerm=" + indexedTerm + ", originalTerms=" + originalTerms + ", ni=" + ni + ", termIdf1=" + termIdf1 + ", termIdf2="
				+ termIdf2 + "]";
	}

	 
  

	
}
