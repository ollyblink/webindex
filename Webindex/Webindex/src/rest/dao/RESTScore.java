package rest.dao;

import index.utils.Document;
import index.utils.GeometryWrapper;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class exists only to transport a score through rest
 * @author rsp
 *
 */
@XmlRootElement
public class RESTScore {
	private Document document;
	private Float score;
	private GeometryWrapper geometry;

	public RESTScore(Document document, Float score, GeometryWrapper geometry) {
		this.document = document;
		this.score = score;
		this.geometry = geometry;
	}

	public RESTScore() {
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public GeometryWrapper getGeometry() {
		return geometry;
	}

	public void setGeometry(GeometryWrapper geometry) {
		this.geometry = geometry;
	}

	 
}
