package index.utils;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author rsp
 *
 */
public class SpatialScore extends Score {
	private Geometry geometry;

	public SpatialScore(Long docid, Geometry geometry) {
		this(docid, geometry, 0f);
	}

	public SpatialScore(Long docid, Geometry geometry, Float score) {
		super(docid, score);
		this.geometry = geometry;

	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

}
