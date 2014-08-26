package index.utils.identifers;


public final class TermDocsIdentifier{
	private String termid;
	private Long docid;
	
	
	public TermDocsIdentifier(String termid, Long docid) {
		this.termid = termid;
		this.docid = docid;
	}
	public String getTermid() {
		return termid;
	}
	public void setTermid(String termid) {
		this.termid = termid;
	}
	public Long getDocid() {
		return docid;
	}
	public void setDocid(Long docid) {
		this.docid = docid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docid == null) ? 0 : docid.hashCode());
		result = prime * result + ((termid == null) ? 0 : termid.hashCode());
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
		TermDocsIdentifier other = (TermDocsIdentifier) obj;
		if (docid == null) {
			if (other.docid != null)
				return false;
		} else if (!docid.equals(other.docid))
			return false;
		if (termid == null) {
			if (other.termid != null)
				return false;
		} else if (!termid.equals(other.termid))
			return false;
		return true;
	}
	
	 
	
}