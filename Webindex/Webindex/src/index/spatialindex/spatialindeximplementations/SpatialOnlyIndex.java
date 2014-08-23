package index.spatialindex.spatialindeximplementations;

import index.spatialindex.AbstractSpatialIndex;
import index.spatialindex.similarities.ISpatialRelationship;
import index.spatialindex.similarities.SpatialRelationshipFactory;
import index.spatialindex.utils.IndexDocumentProvider;
import index.spatialindex.utils.LocationProvider;
import index.spatialindex.utils.SpatialIndexDocumentMetaData;
import index.spatialindex.utils.SpatialIndexMetaData;
import index.spatialindex.utils.SpatialScoreTriple;
import index.utils.IndexDocument;
import index.utils.Ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class SpatialOnlyIndex extends AbstractSpatialIndex {

	public SpatialOnlyIndex(Quadtree quadTree, IndexDocumentProvider docProvider) {
		super(quadTree, docProvider);
	}

	@Override
	public void addLocations(SpatialIndexDocumentMetaData... dFPs) {
		if (dFPs == null || dFPs.length == 0) { // Bouncer
			return;
		} else {
			docProvider.storePersistently(dFPs);
			refillQuadtree();
		}
	}

	@Override
	public Ranking queryIndex(String spatialRelationship, String location) {
		
		// Define spatial relationship algorithm
		ISpatialRelationship spatRelAlgorithm = SpatialRelationshipFactory.create(spatialRelationship);

		// =======================================================================================
		// Querying spatial index
		// =======================================================================================
		// Get spatial location geometry
		List<? extends Geometry> queryFootPrints = LocationProvider.INSTANCE.retrieveLocations(location);
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
		return new Ranking("<spatrel>" + spatialRelationship + "</spatrel><location>" + location + "</location>", documents);
	}

	/**
	 * Used to query whatever information provider has been implemented to get the whole documents specified by Location::docid
	 * 
	 * @return a list of documents according to the locations
	 */
	public ArrayList<IndexDocument> getDocuments(List<SpatialScoreTriple> dFPs) {
		return docProvider.getDocumentIdAndFulltext(dFPs);
	}

	@Override
	public SpatialIndexMetaData getAdditionalIndexInformation() {
		throw new NoSuchMethodError();
	}

}
