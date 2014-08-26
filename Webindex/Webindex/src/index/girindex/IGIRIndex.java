package index.girindex;

import index.spatialindex.ISpatialIndex;
import index.textindex.implementations.ITextIndex;
import index.utils.Ranking;
import index.utils.query.SpatialIndexQuery;
import index.utils.query.TextIndexQuery;

public interface IGIRIndex extends ITextIndex, ISpatialIndex {
	public Ranking queryIndex(boolean isIntersected, TextIndexQuery textQuery, SpatialIndexQuery spatialQuery);

	 
}
