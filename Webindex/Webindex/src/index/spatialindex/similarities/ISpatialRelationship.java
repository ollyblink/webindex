package index.spatialindex.similarities;

import index.spatialindex.utils.SpatialDocument;
import index.utils.Score;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public interface ISpatialRelationship {
	public ArrayList<? extends Score> calculateSimilarity(final List<? extends Geometry> queryFootPrints, final List<SpatialDocument> documentFootPrints);
}
