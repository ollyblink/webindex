package index.spatialindex.implementations;

import index.spatialindex.similarities.ISpatialRelationship;
import index.spatialindex.similarities.SpatialRelationshipFactory;
import index.spatialindex.utils.SpatialDocument;
import index.spatialindex.utils.georeferencing.LocationProvider;
import index.utils.Ranking;
import index.utils.Score;
import index.utils.SpatialScore;
import index.utils.query.SpatialIndexQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class SpatialOnlyIndex implements ISpatialIndex {

	private Quadtree index;

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
		List<SpatialScore> documentFootPrints = new ArrayList<SpatialScore>();
		for (Geometry qFP : queryFootPrints) {
			@SuppressWarnings("unchecked")
			List<SpatialScore> queryResult = index.query(qFP.getEnvelopeInternal());
			documentFootPrints.addAll(queryResult);
		}
		// Algorithm stage: calculate score for each found geometry
		List<SpatialScore> results = spatRelAlgorithm.calculateSimilarity(queryFootPrints, documentFootPrints);
		// =======================================================================================
		// End querying spatial index
		// =======================================================================================

		ArrayList<Score> scores = new ArrayList<Score>();
		for (SpatialScore sST : results) {
			scores.add(new Score(sST.getDocid(), sST.getScore()));
		}
		Collections.sort(scores);
		// Create the spatial ranking
		Ranking ranking = new Ranking(scores);

		return ranking;
	}
 

	@Override
	public void addDocument(SpatialDocument spatialDocument) {
		if (spatialDocument == null || spatialDocument.getDocumentFootprint() == null) {
			return;
		}
		index.insert(spatialDocument.getDocumentFootprint().getEnvelopeInternal(), spatialDocument);
	}

	@Override
	public void addDocuments(List<SpatialDocument> spatialDocuments) {
		if (spatialDocuments == null || spatialDocuments.size() == 0) {
			return;
		} 
		for (SpatialDocument sD : spatialDocuments) {
			addDocument(sD);
		}
	}

}
