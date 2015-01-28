package index.spatialindex.implementations;

import static org.junit.Assert.assertEquals;
import index.spatialindex.utils.SpatialDocument;
import index.spatialindex.utils.geolocating.georeferencing.LocationFinder;
import index.utils.Document;
import index.utils.documenttransformation.ExtractionRequest;
import index.utils.documenttransformation.spatialtransformation.GeoReferencingStage;
import index.utils.documenttransformation.spatialtransformation.GeoTaggingStage;
import index.utils.query.SpatialIndexQuery;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import rest.dao.RESTScore;
import rest.dao.Ranking;
import utils.dbconnection.PGDBConnector;

import com.vividsolutions.jts.geom.Geometry;

public class SpatialOnlyIndexTest {

	private static final boolean IS_SHOW_TRANSFORMATION_ENABLED = false;
	private static SpatialOnlyIndex spatialOnlyIndex;
	private static String host = "localhost";
	private static String port = "5432";
	private static String database = "girindex";
	private static String user = "postgres";
	private static String password = "postgres";

	@BeforeClass
	public static void init() {

		String[] docs = new String[] { "This text is about Zürich, Schweiz", "The mayor of London was not amused", "Many people died in World War II when Berlin was bombed by the allies" };

		LocationFinder testLF = new LocationFinder(true);
		// Spatial
		GeoTaggingStage geoTaggingStage = new GeoTaggingStage(IS_SHOW_TRANSFORMATION_ENABLED, new PGDBConnector(host, port, database, user, password));
		GeoReferencingStage geoReferencingStage = new GeoReferencingStage(testLF, IS_SHOW_TRANSFORMATION_ENABLED );

		// Chaining the stages together in an appropriate way

		geoTaggingStage.setSuccessor(geoReferencingStage);
		geoReferencingStage.setPrecursor(geoTaggingStage);

		spatialOnlyIndex = new SpatialOnlyIndex(testLF);
		long docCounter = 1;
		for (String doc : docs) {
			long id = docCounter++;
			ExtractionRequest request = new ExtractionRequest(doc);
			geoTaggingStage.handleRequest(request);

			@SuppressWarnings("unchecked")
			List<Geometry> documentFootprints = (List<Geometry>) request.getTransformationStage(geoReferencingStage.getClass().getSimpleName());
			List<SpatialDocument> spatialDocuments = new ArrayList<SpatialDocument>();
			// System.out.println(documentFootprints);
			for (Geometry geom : documentFootprints) {
				spatialDocuments.add(new SpatialDocument(new Document(id), geom));
			}

			spatialOnlyIndex.addDocuments(spatialDocuments);
		}

	}

	@Test
	public void queryTest() {
		Ranking ranking = spatialOnlyIndex.queryIndex(new SpatialIndexQuery("point_in", "Switzerland"));
		System.out.println();

		for (RESTScore d : ranking.getResults()) {
			if (d.getDocument().getId().getId() == 1) {
				assertEquals(1f, d.getScore(), 0.01f);
			} else {
				assertEquals(0f, d.getScore(), 0.01f);
			}
		}
	}

}
