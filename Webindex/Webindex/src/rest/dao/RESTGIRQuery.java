package rest.dao;

import index.utils.query.TextIndexQuery;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RESTGIRQuery {
	private boolean isIntersected;
	private TextIndexQuery textQuery;
	private RESTSpatialIndexQuery spatialQuery;

	public RESTGIRQuery(boolean isIntersected, TextIndexQuery textQuery, RESTSpatialIndexQuery spatialQuery) {
		this.isIntersected = isIntersected;
		this.textQuery = textQuery;
		this.spatialQuery = spatialQuery;
	}

	public boolean isIntersected() {
		return isIntersected;
	}

	public void setIntersected(boolean isIntersected) {
		this.isIntersected = isIntersected;
	}

	public TextIndexQuery getTextQuery() {
		return textQuery;
	}

	public void setTextQuery(TextIndexQuery textQuery) {
		this.textQuery = textQuery;
	}

	public RESTSpatialIndexQuery getSpatialQuery() {
		return spatialQuery;
	}

	public void setSpatialQuery(RESTSpatialIndexQuery spatialQuery) {
		this.spatialQuery = spatialQuery;
	}

	 
	
}
