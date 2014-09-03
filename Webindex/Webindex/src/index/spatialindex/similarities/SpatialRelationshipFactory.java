package index.spatialindex.similarities;

import index.spatialindex.similarities.pointsimilarities.InRelationship;
import index.spatialindex.similarities.pointsimilarities.BufferedNearRelationship;
import index.spatialindex.similarities.pointsimilarities.NorthRelationship;
import index.spatialindex.similarities.pointsimilarities.OverlapsRelationship;

public class SpatialRelationshipFactory {

	public static ISpatialRelationship create(String spatialRelationship) {
		switch (spatialRelationship) {
		case "bufferednear":
			return new BufferedNearRelationship();
		case "overlaps":
			return new OverlapsRelationship();
		case "north":
			return new NorthRelationship();
		case "in":
		default:
			return new InRelationship();
		}
	}

}
