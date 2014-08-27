package index.spatialindex.implementations;

import index.spatialindex.utils.SpatialDocument;

import java.util.List;

public interface ISpatialIndexInsertion {
	/**
	 * Add 1 - multiple locations to the index
	 * 
	 * @param documentFootPrints
	 */
	public void addDocument(SpatialDocument spatialDocument);

	public void addDocuments(List<SpatialDocument> spatialDocuments);
}
