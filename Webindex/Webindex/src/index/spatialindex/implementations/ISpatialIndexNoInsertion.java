package index.spatialindex.implementations;

import index.utils.query.SpatialIndexQuery;
import rest.dao.Ranking;

public interface ISpatialIndexNoInsertion {
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

		/** 
		 * Deletes all documents from the index
		 */
		public void clear();
}
