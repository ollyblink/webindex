package index.spatialindex.similarities.pointsimilarities.directional;

import index.spatialindex.similarities.AbstractSpatialRelationship;
import index.spatialindex.utils.SpatialDocument;
import index.utils.Score;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public abstract class AbstractDirectionalRelation extends AbstractSpatialRelationship {

	protected GeometryFactory gF;
	private float multiplicationFactor;

	public AbstractDirectionalRelation(float multiplicationFactor) {
		this.gF = new GeometryFactory();
		this.multiplicationFactor = multiplicationFactor;
	}

	@Override
	protected void calculateSimilarity(ArrayList<Score> results, Geometry qFP, SpatialDocument dFP) {

		Point dfpCentroid = dFP.getDocumentFootprint().getCentroid();

		Point qfpCentroid = qFP.getCentroid();
		if (checkIfBelowLine(dfpCentroid, qfpCentroid)) {
			return;
		}
		Point zero = createZeroPoint(dfpCentroid, qfpCentroid);

		double adjacent = qfpCentroid.distance(zero);
		double hypothenuse = qfpCentroid.distance(dfpCentroid);

		double alpha = Math.acos(adjacent / hypothenuse);

		float directionalScore = 1 - (float) ((alpha / Math.toRadians(45)));

		directionalScore *= nearScore(qfpCentroid, adjacent, dfpCentroid);
		if (directionalScore > 0f) {
			checkLargestScore(results, dFP, directionalScore);
		}
	}

	protected abstract Point createZeroPoint(Point dfpCentroid, Point qfpCentroid);

	private float nearScore(Point qFP, double radius, Point dFP) {
		double distance = qFP.distance(dFP);
		float nearScoreValue = (float) (1.0 - (distance / (radius * multiplicationFactor)));

		System.out.println(distance + ", " + (radius * multiplicationFactor) + ", " + nearScoreValue);
		if (nearScoreValue < 0f) {
			nearScoreValue = 0f;
		}
		return nearScoreValue;
	}

	@Override
	public ArrayList<Geometry> preCalculateQueryFootprint(ArrayList<Geometry> queryFootPrints) {
		ArrayList<Geometry> alteredGeoms = new ArrayList<Geometry>();
		for (Geometry geom : queryFootPrints) {
			if (geom.getClass().equals("Polygon")) {
				Polygon polygon = (Polygon) geom;
				Point c = polygon.getCentroid();
				Coordinate[] coordinates = polygon.getCoordinates();
				Point ne = getUpperRight(coordinates);
				Point sw = getLowerLeft(coordinates);

				createNewQueryPolygon(alteredGeoms, c, ne, sw);
			}
		}
		return alteredGeoms;
	}

	private void createNewQueryPolygon(ArrayList<Geometry> alteredGeoms, Point c, Point ne, Point sw) {
		Point a = getLowerLeftOfNewQueryPolygon(c, sw);
		Point b = getUpperRightOfNewQueryPolygon(c, ne);
		Polygon newQFP = createNewQueryPolygon(a, b);
		alteredGeoms.add(newQFP);
	}

	protected abstract boolean checkIfBelowLine(Point dfpCentroid, Point qfpCentroid);

	protected abstract Point getUpperRightOfNewQueryPolygon(Point c, Point ne);

	protected abstract Point getLowerLeftOfNewQueryPolygon(Point c, Point sw);

	protected Point getLowerLeft(Coordinate[] cs) {

		Coordinate minCoord = cs[0];

		for (int i = 1; i < cs.length; ++i) {
			if (cs[i].x < minCoord.x && cs[i].y < minCoord.y) {
				minCoord = cs[i];
			}
		}
		return new GeometryFactory().createPoint(minCoord);
	}

	protected Point getUpperRight(Coordinate[] cs) {

		Coordinate maxCoord = cs[0];

		for (int i = 1; i < cs.length; ++i) {
			if (cs[i].x > maxCoord.x && cs[i].y > maxCoord.y) {
				maxCoord = cs[i];
			}
		}
		return new GeometryFactory().createPoint(maxCoord);
	}

	protected Polygon createNewQueryPolygon(Point a, Point b) {
		Coordinate[] newQueryPolygon = new Coordinate[5];
		newQueryPolygon[0] = a.getCoordinate();
		newQueryPolygon[1] = new Coordinate(b.getCoordinate().x, a.getCoordinate().y);
		newQueryPolygon[2] = b.getCoordinate();
		newQueryPolygon[3] = new Coordinate(a.getCoordinate().x, b.getCoordinate().y);
		newQueryPolygon[4] = a.getCoordinate();
		Polygon newQFP = gF.createPolygon(newQueryPolygon);
		return newQFP;
	}

}
