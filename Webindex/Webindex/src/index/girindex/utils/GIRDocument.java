package index.girindex.utils;

import index.spatialindex.utils.SpatialDocument;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocs;
import index.utils.Document;

import java.util.List;

/**
 * All information needed to add a complete document to a GIR Index
 * @author rsp
 *
 */
public class GIRDocument {
	private Document textDocument;
	private List<Term> termsInTextDocument;
	private List<TermDocs> termDocsRelationship;
	private SpatialDocument spatialDocument;

	public GIRDocument(Document textDocument, List<Term> termsInTextDocument, SpatialDocument spatialDocument) {
		if(textDocument.getId().getId() != spatialDocument.getDocid().getDocId()){
			throw new IllegalArgumentException("text and spatial document need to have the same docid");
		}else if(textDocument == null || termsInTextDocument == null || spatialDocument == null){
			throw new NullPointerException("GIRDocument arguments must not be null!");
		}
		this.textDocument = textDocument;
		this.termsInTextDocument = termsInTextDocument;
		this.spatialDocument = spatialDocument;
	}

	public Document getTextDocument() {
		return textDocument;
	}

	public void setTextDocument(Document textDocument) {
		this.textDocument = textDocument;
	}

	public List<Term> getTermsInTextDocument() {
		return termsInTextDocument;
	}

	public void setTermsInTextDocument(List<Term> termsInTextDocument) {
		this.termsInTextDocument = termsInTextDocument;
	}

	public SpatialDocument getSpatialDocument() {
		return spatialDocument;
	}

	public void setSpatialDocument(SpatialDocument spatialDocument) {
		this.spatialDocument = spatialDocument;
	}

}
