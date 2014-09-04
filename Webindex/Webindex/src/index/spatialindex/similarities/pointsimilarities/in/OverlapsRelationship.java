package index.spatialindex.similarities.pointsimilarities.in;

import index.spatialindex.similarities.AbstractSpatialRelationship;
import index.spatialindex.utils.SpatialDocument;
import index.utils.Score;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Geometry;

public class OverlapsRelationship extends AbstractSpatialRelationship {

	@Override
	protected void calculateSimilarity(ArrayList<Score> results, Geometry qFP, SpatialDocument dFP) {
		if(dFP.getDocumentFootprint().getClass().getSimpleName().equals("Point")){
			return;
		}
		if(qFP.intersects(dFP.getDocumentFootprint())){
			float scoreValue = (float) (qFP.intersection(dFP.getDocumentFootprint()).getArea()/qFP.getArea());
			checkLargestScore(results, dFP, scoreValue);
		} 
	}

	@Override
	public ArrayList<Geometry> preCalculateQueryFootprint(ArrayList<Geometry> queryFootPrints) {
		return queryFootPrints;
	} 
}
