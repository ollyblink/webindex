package index.textindex.utils;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * implements a row of the database table term_docs
 * 
 * @author rsp
 *
 */
@XmlRootElement
public class TermDocumentValues  {
	 

	private List<String> originalTerms;
	private String indexedTerm;
	private int ni;
	private float termIdf1;
	private float termIdf2; 
	private int fij;
	private float docTf1;
	private float docTf23;
	private float docTfIdf1;
	private float docTfIdf2;
	private float docTfIdf3;
	
 
	public TermDocumentValues(String indexedTerm, int ni, float termIdf1, float termIdf2,  int fij, float docTf1, float docTf23, float docTfIdf1, float docTfIdf2, float docTfIdf3) {
		this.indexedTerm = indexedTerm;
		originalTerms = new ArrayList<String>();
		this.ni = ni;
		this.termIdf1 = termIdf1;
		this.termIdf2 = termIdf2; 
		this.fij = fij;
		this.docTf1 = docTf1;
		this.docTf23 = docTf23;
		this.docTfIdf1 = docTfIdf1;
		this.docTfIdf2 = docTfIdf2;
		this.docTfIdf3 = docTfIdf3; 
	}
	
	public TermDocumentValues(){
		
	} 
	public List<String> getOriginalTerms() {
		return originalTerms;
	}

	public void setOriginalTerms(List<String> originalTerms) {
		this.originalTerms = originalTerms;
	}

	public String getIndexedTerm() {
		return indexedTerm;
	}

	public void setIndexedTerm(String indexedTerm) {
		this.indexedTerm = indexedTerm;
	}

	public void setNi(int ni) {
		this.ni = ni;
	}

	public void setTermIdf1(float termIdf1) {
		this.termIdf1 = termIdf1;
	}

	public void setTermIdf2(float termIdf2) {
		this.termIdf2 = termIdf2;
	}

	 

	public void setFij(int fij) {
		this.fij = fij;
	}

	public void setDocTf1(float docTf1) {
		this.docTf1 = docTf1;
	}

	public void setDocTf2(float docTf23) {
		this.docTf23 = docTf23;
	}

	public void setDocTfIdf1(float docTfIdf1) {
		this.docTfIdf1 = docTfIdf1;
	}

	public void setDocTfIdf2(float docTfIdf2) {
		this.docTfIdf2 = docTfIdf2;
	}

	public void setDocTfIdf3(float docTfIdf3) {
		this.docTfIdf3 = docTfIdf3;
	}

 
	public int getNi() {
		return ni;
	}

	public float getTermIdf1() {
		return termIdf1;
	}

	public float getTermIdf2() {
		return termIdf2;
	}

 

	public int getFij() {
		return fij;
	}

	public float getDocTf1() {
		return docTf1;
	}

	public float getDocTf23() {
		return docTf23;
	}

	public float getDocTfIdf1() {
		return docTfIdf1;
	}

	public float getDocTfIdf2() {
		return docTfIdf2;
	}

	public float getDocTfIdf3() {
		return docTfIdf3;
	}
 

	public void setDocTf23(float docTf23) {
		this.docTf23 = docTf23;
	}

	public void addOriginalTerm(String originalTerm) {
		this.originalTerms.add(originalTerm);
	}
 
 
	
	
}
