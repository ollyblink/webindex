package index.spatialindex.similarities;

import index.spatialindex.utils.SpatialDocument;
import index.utils.Score;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public interface ISpatialRelationship {
	public ArrayList<Score> calculateSimilarity(final List<? extends Geometry> queryFootPrints, final List<SpatialDocument> documentFootPrints);

	/**
	 * Precalculates the query footprint MBR used in the filter stage.
	 * @param queryFootPrints
	 * @return
	 */
	public ArrayList<Geometry> preCalculateQueryFootprint(ArrayList<Geometry> queryFootPrints);
}
