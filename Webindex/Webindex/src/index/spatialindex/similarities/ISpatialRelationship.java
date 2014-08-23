package index.spatialindex.similarities;

import index.spatialindex.utils.SpatialScoreTriple;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public interface ISpatialRelationship {
	public List<SpatialScoreTriple> calculateSimilarity(final List<? extends Geometry> queryFootPrints, final List<SpatialScoreTriple> documentFootPrints);
}
