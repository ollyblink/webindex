package similarities.spatialsimilarities.pointsimilarities;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import index.spatialindex.similarities.pointsimilarities.InRelationship;
import index.spatialindex.utils.SpatialScoreTriple;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import testutils.SwissProvider;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

public class InRelationshipTest {

	private static InRelationship inRel;
	private static List<Polygon> queryFootPrints;
	private static List<SpatialScoreTriple> documentFootPrints;

	@BeforeClass
	public static void init() {
		inRel = new InRelationship();
		queryFootPrints = new ArrayList<>();
		documentFootPrints = new ArrayList<>();

		GeometryFactory fctry = SwissProvider.getPolygonFactory();
		Polygon swissMBR = SwissProvider.getSwitzerlandMBR();
		queryFootPrints.add(swissMBR);

		documentFootPrints.add(new SpatialScoreTriple(1l, fctry.createPoint(new Coordinate(8.47, 47.38))));
		documentFootPrints.add(new SpatialScoreTriple(1l, fctry.createPolygon(new Coordinate[] { new Coordinate(8.47 - .1, 47.38 - .1), new Coordinate(8.47 - .1, 47.38 + .1),
				new Coordinate(8.47 + .1, 47.38 + .1), new Coordinate(8.47 + .1, 47.38 - .1), new Coordinate(8.47 - .1, 47.38 - .1) })));

		double iLat = 48;
		double iLon = 12;
		documentFootPrints.add(new SpatialScoreTriple(2l, fctry.createPoint(new Coordinate(iLon, iLat))));
		documentFootPrints.add(new SpatialScoreTriple(2l, fctry.createPolygon(new Coordinate[] { new Coordinate(iLon - .1, iLat - .1), new Coordinate(iLon - .1, iLat + .1),
				new Coordinate(iLon + .1, iLat + .1), new Coordinate(iLon + .1, iLat - .1), new Coordinate(iLon - .1, iLat - .1) })));

	}

	@Test
	public void testPointInRelationship() {
		inRel.calculateSimilarity(queryFootPrints, documentFootPrints);

		for (SpatialScoreTriple data : documentFootPrints) {
			String className = data.getGeometry().getClass().getSimpleName();

			assertTrue(className.equals("Point") || className.equals("Polygon"));

			if (data.getDocid() == 1l) {
				if (className.equals("Point")) {
					assertTrue(data.getGeometry().toString().contains("8.47") && data.getGeometry().toString().contains("47.38"));
					assertTrue(data.getScore() == 1f);
				} else if (className.equals("Polygon")) {
					assertTrue(data.getGeometry().toString().contains("8.37") && data.getGeometry().toString().contains("8.57") && data.getGeometry().toString().contains("47.28")
							&& data.getGeometry().toString().contains("47.48"));
					assertTrue(data.getScore() == 1f);
				} else {
					fail();
				}
			} else if (data.getDocid() == 2l) {
				if (className.equals("Point")) {
					assertTrue(!data.getGeometry().toString().contains("8.47") && !data.getGeometry().toString().contains("47.38"));
					assertTrue(data.getScore() == 0f);
				} else if (className.equals("Polygon")) {
					assertTrue(!data.getGeometry().toString().contains("8.37") && !data.getGeometry().toString().contains("8.57") && !data.getGeometry().toString().contains("47.28")
							&& !data.getGeometry().toString().contains("47.48"));
					assertTrue(data.getScore() == 0f);
				} else {
					fail();
				}
			} else {
				fail();
			}
		}

	}

}
