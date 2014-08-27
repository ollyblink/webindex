package index.spatialindex.similarities;

import index.utils.SpatialScore;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public interface ISpatialRelationship {
	public List<SpatialScore> calculateSimilarity(final List<? extends Geometry> queryFootPrints, final List<SpatialScore> documentFootPrints);
}
