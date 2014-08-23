package index.spatialindex;

import index.spatialindex.utils.SpatialIndexDocumentMetaData;
import index.spatialindex.utils.SpatialIndexMetaData;
import index.spatialindex.utils.SpatialScoreTriple;
import index.utils.IndexDocument;
import index.utils.Ranking;

import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;

public interface ISpatialIndex {

	/**
	 * Add 1 - multiple locations to the index
	 * 
	 * @param documentFootPrints
	 */
	public void addLocations(SpatialIndexDocumentMetaData... documentFootPrints);

	/**
	 * Queries the index according to the specified queryFootprint using the specified SpatialAlgorithm
	 * 
	 * @param spatialRelationship
	 * @param location
	 * @return
	 */
	public Ranking queryIndex(String spatialRelationship, String location);

	/**
	 * Used to display various facts about the index
	 * 
	 * @return
	 */
	public SpatialIndexMetaData getAdditionalIndexInformation();

	

}
