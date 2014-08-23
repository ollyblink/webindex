package index.spatialindex.similarities;

import index.spatialindex.similarities.pointsimilarities.InRelationship;

public class SpatialRelationshipFactory {

	public static ISpatialRelationship create(String spatialRelationship) {
		switch (spatialRelationship) {
		case "point_in":
		default:
			return new InRelationship();
		}
	}

}
