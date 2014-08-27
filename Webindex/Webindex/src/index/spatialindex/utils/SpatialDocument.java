package index.spatialindex.utils;

import index.utils.identifers.LocationIdentifier;

import com.vividsolutions.jts.geom.Geometry;

public class SpatialDocument {

	private LocationIdentifier docid;
	private Geometry documentFootprint;

	public SpatialDocument(Long docid, Geometry documentFootprint) {
		this.docid = new LocationIdentifier(docid);
		this.documentFootprint = documentFootprint;
	}

	public LocationIdentifier getDocid() {
		return docid;
	}

	public void setDocid(LocationIdentifier docid) {
		this.docid = docid;
	}

	public Geometry getDocumentFootprint() {
		return documentFootprint;
	}

	public void setDocumentFootprint(Geometry documentFootprint) {
		this.documentFootprint = documentFootprint;
	}

}
