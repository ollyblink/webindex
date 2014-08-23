package index.textindex.utils;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Term {
	private String indexedTerm;
	private String originalTerm;
	 
	public Term(){
		
	}
	public Term(String indexedTerm, String originalTerm) {
		this.indexedTerm = indexedTerm;
		this.originalTerm = originalTerm;
	}
	public String getIndexedTerm() {
		return indexedTerm;
	}
	public void setIndexedTerm(String indexedTerm) {
		this.indexedTerm = indexedTerm;
	}
	public String getOriginalTerm() {
		return originalTerm;
	}
	public void setOriginalTerm(String originalTerm) {
		this.originalTerm = originalTerm;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((indexedTerm == null) ? 0 : indexedTerm.hashCode());
		result = prime * result + ((originalTerm == null) ? 0 : originalTerm.hashCode());
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
		if (originalTerm == null) {
			if (other.originalTerm != null)
				return false;
		} else if (!originalTerm.equals(other.originalTerm))
			return false;
		return true;
	}
 
	
	
}
