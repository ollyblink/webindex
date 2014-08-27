package index.spatialindex.implementations;

import index.spatialindex.utils.SpatialDocument;
import index.utils.Ranking;
import index.utils.query.SpatialIndexQuery;

import java.util.List;

public interface ISpatialIndex {

	/**
	 * Add 1 - multiple locations to the index
	 * 
	 * @param documentFootPrints
	 */
	public void addDocument(SpatialDocument spatialDocument);

	public void addDocuments(List<SpatialDocument> spatialDocuments);

	// Maybe in the future somewhen?
	// /**
	// * Used to display various facts about the index
	// *
	// * @return
	// */
	// public SpatialIndexMetaData getSpatialMetaData();

	/**
	 * Query this index with a spatial query
	 * 
	 * @param query
	 * @return
	 */
	public Ranking queryIndex(SpatialIndexQuery query);

}
