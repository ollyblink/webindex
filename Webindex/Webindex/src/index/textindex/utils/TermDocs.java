package index.textindex.utils;

import index.utils.identifers.TermDocsIdentifier;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TermDocs {

	private TermDocsIdentifier id;
	private int fij;
	private float docTf1;
	private float docTf23;
	private float docTermTfidf1;
	private float docTermTfidf2;
	private float docTermTfidf3;

	public TermDocs(String termid, Long docid, int fij, float docTf1, float docTf23, float docTermTfidf1, float docTermTfidf2, float docTermTfidf3) {
		this.id = new TermDocsIdentifier(termid, docid);
		this.fij = fij;
		this.docTf1 = docTf1;
		this.docTf23 = docTf23;
		this.docTermTfidf1 = docTermTfidf1;
		this.docTermTfidf2 = docTermTfidf2;
		this.docTermTfidf3 = docTermTfidf3;
	}

	public TermDocsIdentifier getId() {
		return id;
	}

	public void setId(TermDocsIdentifier id) {
		this.id = id;
	}

	public int getFij() {
		return fij;
	}

	public void setFij(int fij) {
		this.fij = fij;
	}

	public float getDocTf1() {
		return docTf1;
	}

	public void setDocTf1(float docTf1) {
		this.docTf1 = docTf1;
	}

	public float getDocTf23() {
		return docTf23;
	}

	public void setDocTf23(float docTf23) {
		this.docTf23 = docTf23;
	}

	public float getDocTermTfidf1() {
		return docTermTfidf1;
	}

	public void setDocTermTfidf1(float docTermTfidf1) {
		this.docTermTfidf1 = docTermTfidf1;
	}

	public float getDocTermTfidf2() {
		return docTermTfidf2;
	}

	public void setDocTermTfidf2(float docTermTfidf2) {
		this.docTermTfidf2 = docTermTfidf2;
	}

	public float getDocTermTfidf3() {
		return docTermTfidf3;
	}

	public void setDocTermTfidf3(float docTermTfidf3) {
		this.docTermTfidf3 = docTermTfidf3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		TermDocs other = (TermDocs) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TermDocs [id=" + id + ", fij=" + fij + ", docTf1=" + docTf1 + ", docTf23=" + docTf23 + ", docTermTfidf1=" + docTermTfidf1
				+ ", docTermTfidf2=" + docTermTfidf2 + ", docTermTfidf3=" + docTermTfidf3 + "]";
	}

}
