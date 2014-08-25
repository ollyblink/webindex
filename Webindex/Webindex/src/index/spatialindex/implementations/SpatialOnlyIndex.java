package index.spatialindex.implementations;

import index.spatialindex.AbstractSpatialIndex;
import index.spatialindex.similarities.ISpatialRelationship;
import index.spatialindex.similarities.SpatialRelationshipFactory;
import index.spatialindex.utils.LocationProvider;
import index.spatialindex.utils.SpatialIndexDocumentMetaData;
import index.spatialindex.utils.SpatialIndexMetaData;
import index.spatialindex.utils.SpatialScoreTriple;
import index.utils.DBDataProvider;
import index.utils.IndexDocument;
import index.utils.Ranking;
import index.utils.query.SpatialIndexQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class SpatialOnlyIndex extends AbstractSpatialIndex {

	public SpatialOnlyIndex(Quadtree quadTree, DBDataProvider docProvider, Long... docids) {
		super(quadTree, docProvider, docids);
	}

	 
	@Override
	public Ranking queryIndex(SpatialIndexQuery query) {

		// Define spatial relationship algorithm
		ISpatialRelationship spatRelAlgorithm = SpatialRelationshipFactory.create(query.getSpatialRelationship());

		// =======================================================================================
		// Querying spatial index
		// =======================================================================================
		// Get spatial location geometry
		ArrayList<Geometry> queryFootPrints = LocationProvider.INSTANCE.retrieveLocations(query.getLocation());
		query.setQueryFootPrints(queryFootPrints);
		
		// Filter stage
		List<SpatialScoreTriple> documentFootPrints = new ArrayList<SpatialScoreTriple>();
		for (Geometry qFP : queryFootPrints) {
			@SuppressWarnings("unchecked")
			List<SpatialScoreTriple> queryResult = quadTree.query(qFP.getEnvelopeInternal());
			documentFootPrints.addAll(queryResult);
		}
		// Algorithm stage: calculate score for each found geometry
		List<SpatialScoreTriple> results = spatRelAlgorithm.calculateSimilarity(queryFootPrints, documentFootPrints);
		// =======================================================================================
		// End querying spatial index
		// =======================================================================================

		// Retrieve all the documents for the scores
		ArrayList<IndexDocument> documents = getDocuments(results);

		// Sorting documents so they are ranked from highest to lowerst rank
		Collections.sort(documents);

		// Create the spatial ranking
		Ranking ranking = new Ranking(documents);
		ranking.setSpatialQuery(query);
		
		return ranking;
	}

	

	@Override
	public SpatialIndexMetaData getSpatialMetaData() {
		//TODO 
		throw new NoSuchMethodError();
	}

	@Override
	protected void fillQuadtree(Long... docids) {
		List<SpatialIndexDocumentMetaData> docLocs = docProvider.getDocumentLocations(docids);
		for (SpatialIndexDocumentMetaData docLoc : docLocs) {
			List<Geometry> geometries = docLoc.getGeometries();
			for (Geometry geometry : geometries) {
				quadTree.insert(geometry.getEnvelopeInternal(), new SpatialScoreTriple(docLoc.getDocid(), geometry, 0f));
			}
		}
	}

}
