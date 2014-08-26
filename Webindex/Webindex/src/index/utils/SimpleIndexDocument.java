package index.utils;

import index.girindex.implementations.TextPrimarySpatialIndex;

import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Much simpler version of {@link Document}. Only provides the indexed terms (not original terms, may be stemmed and analyzed), the document's
 * identifier and the documents spatial footprints. To be used in {@link TextPrimarySpatialIndex}
 * 
 * @author rsp
 *
 */
public class SimpleIndexDocument {
	private Long docId;
	private Set<Geometry> documentFootPrints;
	private Set<String> indexTerms;
	 
	public SimpleIndexDocument(Long docId, Set<String> indexTerms, Set<Geometry> documentFootPrints) {
		this.docId = docId;
		this.documentFootPrints = documentFootPrints;
		this.indexTerms = indexTerms;
	}
	
	/* 
	 * convenvience methods
	 * 
	 */
	public void addTerm(String term){
		this.indexTerms.add(term);
	}
	
	public void addDocumentFootprint(Geometry documentFootPrint){
		this.documentFootPrints.add(documentFootPrint);
	}
	
	/*
	 * END convenience methods
	 */
	public Long getDocId() {
		return docId;
	}
	public Set<Geometry> getDocumentFootPrints() {
		return documentFootPrints;
	}
	public Set<String> getIndexTerms() {
		return indexTerms;
	}
	
	
}
