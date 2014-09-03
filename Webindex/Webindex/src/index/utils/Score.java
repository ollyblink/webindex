package index.utils;

import javax.xml.bind.annotation.XmlRootElement;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author rsp
 *
 */
@XmlRootElement
public class Score implements Comparable<Score> {
	private Document document;
	private Float score;
	private Geometry geometry;

	public Score() {
	}

	public Score(Document document, Float score, Geometry geometry) {
		this.document = document;
		this.score = score;
		this.geometry = geometry;
	}

	@Override
	public int compareTo(Score o) {
		return -score.compareTo(o.score);
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

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
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
		Score other = (Score) obj;
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

	@Override
	public String toString() {
		return "<" + document.getId() + ",  " + score + ", " + geometry + ">";
	}

}
