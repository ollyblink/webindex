package index.spatialindex.similarities;

import index.spatialindex.utils.SpatialScoreTriple;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public interface ISpatialRelationship {
	 
	public void calculateSimilarity(List<? extends Geometry> queryFootPrints, List<SpatialScoreTriple> documentFootPrints);
}
