package index.spatialindex.similarities.pointsimilarities.near;

import index.spatialindex.utils.SpatialDocument;
import index.utils.Score;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Formula: 1 - (distance(Centroid_Query, Centroid_DocFootPrint)/Radius)
 * See Master thesis page 25, LinearNear()
 * @author rsp
 *
 */
public class CircularLinearDecayNearRelationship extends AbstractNearRelationship{

	public CircularLinearDecayNearRelationship(float multiplicationFactor) {
		super(multiplicationFactor); 
	}

	@Override
	protected void calculateSimilarity(ArrayList<Score> results, Geometry qFP, SpatialDocument dFP) {
		Geometry docFootPrint = dFP.getDocumentFootprint();
		double distance = qFP.getCentroid().distance(docFootPrint.getCentroid());
		//qFP in this case has to be (according to AbstractNearRelationship) a circle, thus, area = r^2 * PI >> r = sqrt(area/PI)
		double radius = Math.sqrt(qFP.getArea()/Math.PI);
		float scoreValue = (float) (1.0 -(distance/radius));
		if(scoreValue > 0f){
			super.checkLargestScore(results, dFP, scoreValue);
		}
		
	}

}
