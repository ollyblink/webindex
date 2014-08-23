package index.spatialindex.utils;

import com.vividsolutions.jts.geom.Geometry;

public class SpatialScoreTriple {
	private Long docid;
	private Geometry geometry;
	private Float score;

	public SpatialScoreTriple(Long docid, Geometry geometry) {
		this(docid, geometry, 0f);
	}

	public SpatialScoreTriple(Long docid, Geometry geometry, Float score) {
		this.docid = docid;
		this.geometry = geometry;
		this.score = score;
	}

	public Long getDocid() {
		return docid;
	}

	public void setDocid(Long docid) {
		this.docid = docid;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

}
