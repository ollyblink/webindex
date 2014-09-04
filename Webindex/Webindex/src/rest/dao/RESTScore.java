package rest.dao;

import index.utils.Document;
import index.utils.GeometryWrapper;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class exists only to transport a score through rest
 * 
 * @author rsp
 *
 */
@XmlRootElement
public class RESTScore implements Comparable<RESTScore>{
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

	@Override
	public int compareTo(RESTScore o) {
		return -score.compareTo(o.score);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((document == null) ? 0 : document.hashCode());
		result = prime * result + ((geometry == null) ? 0 : geometry.hashCode());
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
		RESTScore other = (RESTScore) obj;
		if (document == null) {
			if (other.document != null)
				return false;
		} else if (!document.equals(other.document))
			return false;
		if (geometry == null) {
			if (other.geometry != null)
				return false;
		} else if (!geometry.equals(other.geometry))
			return false;
		return true;
	}

}
