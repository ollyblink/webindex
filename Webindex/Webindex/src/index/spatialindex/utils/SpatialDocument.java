package index.spatialindex.utils;

import javax.xml.bind.annotation.XmlRootElement;

import index.utils.Document;

import com.vividsolutions.jts.geom.Geometry;
@XmlRootElement
public class SpatialDocument {

	private Document document;
	private Geometry documentFootprint;

	public SpatialDocument(Document document, Geometry documentFootprint) {
		this.document = document;
		this.documentFootprint = documentFootprint;
	}

	 

	public Document getDocument() {
		return document;
	}



	public void setDocument(Document document) {
		this.document = document;
	}



	public Geometry getDocumentFootprint() {
		return documentFootprint;
	}

	public void setDocumentFootprint(Geometry documentFootprint) {
		this.documentFootprint = documentFootprint;
	}

}
