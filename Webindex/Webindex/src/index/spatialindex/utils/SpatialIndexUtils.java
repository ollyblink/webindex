package index.spatialindex.utils;

import index.spatialindex.similarities.ISpatialRelationship;
import index.spatialindex.similarities.SpatialRelationshipFactory;
import index.utils.DBDataProvider;
import index.utils.IndexDocument;
import index.utils.Ranking;
import index.utils.Score;
import index.utils.query.SpatialIndexQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class SpatialIndexUtils {
	public static Ranking performSpatialQuery(SpatialIndexQuery query, Quadtree quadTree, DBDataProvider dbDataProvider) {
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

		 
		ArrayList<Score> scores = new ArrayList<Score>();
		for(SpatialScoreTriple sST: results){
			scores.add(new Score(sST.getDocid(), sST.getScore()));
		}
		Collections.sort(scores);
 		// Create the spatial ranking
		Ranking ranking = new Ranking(scores); 

		return ranking;
	}

	/**
	 * Used to query whatever information provider has been implemented to get the whole documents specified by Location::docid
	 * 
	 * @return a list of documents according to the locations
	 */
	public static ArrayList<IndexDocument> getDocuments(List<SpatialScoreTriple> dFPs, DBDataProvider dbDataProvider) {
		return dbDataProvider.getDocumentIdAndFulltext(dFPs);
	}

}
