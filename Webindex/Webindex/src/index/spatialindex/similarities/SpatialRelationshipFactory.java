package index.spatialindex.similarities;

import index.spatialindex.similarities.pointsimilarities.directional.EastRelationship;
import index.spatialindex.similarities.pointsimilarities.directional.NorthRelationship;
import index.spatialindex.similarities.pointsimilarities.directional.SouthRelationship;
import index.spatialindex.similarities.pointsimilarities.directional.WestRelationship;
import index.spatialindex.similarities.pointsimilarities.in.InRelationship;
import index.spatialindex.similarities.pointsimilarities.in.OverlapsRelationship;
import index.spatialindex.similarities.pointsimilarities.near.CircularBufferNearRelationship;
import index.spatialindex.similarities.pointsimilarities.near.CircularLinearDecayNearRelationship;

public class SpatialRelationshipFactory {
	private static final float multiplicationFactor = 10f;

	public static ISpatialRelationship create(String spatialRelationship) {
		switch (spatialRelationship) {
		case "circularbuffernear":
			return new CircularBufferNearRelationship(1f, 5f);
		case "circularlinearnear":
			return new CircularLinearDecayNearRelationship(1.1f);
		case "overlaps":
			return new OverlapsRelationship();
		case "north":
			return new NorthRelationship(multiplicationFactor);
		case "east":
			return new EastRelationship(multiplicationFactor);
		case "west":
			return new WestRelationship(multiplicationFactor);
		case "south":
			return new SouthRelationship(multiplicationFactor);
		case "in":
		default:
			return new InRelationship();
		}
	}

}
