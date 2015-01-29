package index.spatialindex.implementations;

import index.spatialindex.similarities.ISpatialRelationship;
import index.spatialindex.similarities.SpatialRelationshipFactory;
import index.spatialindex.utils.GeometryConverter;
import index.spatialindex.utils.SpatialDocument;
import index.spatialindex.utils.geolocating.georeferencing.IPlaceExtractor;
import index.spatialindex.utils.geolocating.georeferencing.LocationFinder;
import index.spatialindex.utils.geolocating.georeferencing.YPMPlaceExtractor;
import index.utils.Score;
import index.utils.query.SpatialIndexQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rest.dao.RESTSpatialQueryMetaData;
import rest.dao.Ranking;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class SpatialOnlyIndex implements ISpatialIndex {

	private Quadtree index;
	private LocationFinder locationFinder;

	public SpatialOnlyIndex(LocationFinder locationFinder) {
		this.index = new Quadtree();
		this.locationFinder = locationFinder;
	}

	public SpatialOnlyIndex() {
		this.index = new Quadtree(); 
		this.locationFinder = new LocationFinder(); 
	}

	@Override
	public Ranking queryIndex(SpatialIndexQuery query) {
		// Define spatial relationship algorithm
		ISpatialRelationship spatRelAlgorithm = SpatialRelationshipFactory.create(query.getSpatialRelationship());

		// =======================================================================================
		// Querying spatial index
		// =======================================================================================
		// Get spatial location geometry
		ArrayList<Geometry> queryFootPrints = locationFinder.findMBR(query.getLocation());
		query.setQueryFootPrints(spatRelAlgorithm.preCalculateQueryFootprint(queryFootPrints));

		// Filter stage
		List<SpatialDocument> documentFootPrints = new ArrayList<SpatialDocument>();
		for (Geometry qFP : queryFootPrints) {
			@SuppressWarnings("unchecked")
			List<SpatialDocument> queryResult = index.query(qFP.getEnvelopeInternal());
			documentFootPrints.addAll(queryResult);
		}
		query.setQueryFootPrints(queryFootPrints);
		// Algorithm stage: calculate score for each found geometry
		ArrayList<Score> results = spatRelAlgorithm.calculateSimilarity(queryFootPrints, documentFootPrints);

		// =======================================================================================
		// End querying spatial index
		// =======================================================================================

		// ArrayList<Score> scores = new ArrayList<Score>();
		// for (SpatialScore sST : results) {
		// scores.add(new Score(sST.getDocid(), sST.getScore()));
		// }
		Collections.sort(results);
		// Create the spatial ranking
		Ranking ranking = collectMetaData(query, queryFootPrints, results);

		return ranking;
	}

	private Ranking collectMetaData(SpatialIndexQuery query, ArrayList<Geometry> queryFootPrints, ArrayList<Score> results) {
		Ranking ranking = new Ranking();
		RESTSpatialQueryMetaData spatialMeta = new RESTSpatialQueryMetaData();
		spatialMeta.setLocation(query.getLocation());
		spatialMeta.setSpatialRelationship(query.getSpatialRelationship());
		spatialMeta.setQueryFootPrints(GeometryConverter.convertGeometriesToREST(queryFootPrints));
		spatialMeta.setScores(GeometryConverter.convertScoresToREST(results));
		spatialMeta.setPrintableQuery(getSpatialPartOfQuery(query.getSpatialRelationship(), query.getLocation()));
		ranking.setSpatialQueryMetaData(spatialMeta);
		return ranking;
	}

	private String getSpatialPartOfQuery(String spatialrelationship, String locationquery) {
		if (locationquery == null || locationquery.trim().length() == 0) {
			return "";
		} else {
			return "<" + spatialrelationship + "><" + locationquery + ">";
		}
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

	@Override
	public void clear() {
		index = new Quadtree();
	}

	public Quadtree getIndex() {
		return index;
	}

	public void setIndex(Quadtree index) {
		this.index = index;
	}

}
