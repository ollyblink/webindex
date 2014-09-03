package index.spatialindex.similarities;

import index.spatialindex.similarities.pointsimilarities.InRelationship;
import index.spatialindex.similarities.pointsimilarities.OverlapsRelationship;

public class SpatialRelationshipFactory {

	public static ISpatialRelationship create(String spatialRelationship) {
		switch (spatialRelationship) {
		case "overlaps":
			return new OverlapsRelationship();
		case "point_in":
		default:
			return new InRelationship();
		}
	}

}
