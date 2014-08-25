package index.spatialindex;

import index.spatialindex.utils.SpatialIndexDocumentMetaData;
import index.spatialindex.utils.SpatialIndexMetaData;
import index.utils.Ranking;
import index.utils.indexchangelistener.IIndexChangeListener;
import index.utils.query.SpatialIndexQuery;

public interface ISpatialIndex extends IIndexChangeListener{

	/**
	 * Add 1 - multiple locations to the index
	 * 
	 * @param documentFootPrints
	 */
	public void addLocations(SpatialIndexDocumentMetaData... documentFootPrints);

 

	/**
	 * Used to display various facts about the index
	 * 
	 * @return
	 */
	public SpatialIndexMetaData getSpatialMetaData();

	public Ranking queryIndex(SpatialIndexQuery query);

	

}
