package index.spatialindex.implementations;

import static org.junit.Assert.*;
import index.girindex.utils.girtexttransformation.ExtractionRequest;
import index.girindex.utils.girtexttransformation.spatialtransformation.GeoReferencingStage;
import index.girindex.utils.girtexttransformation.spatialtransformation.GeoTaggingStage;
import index.spatialindex.utils.SpatialDocument;
import index.utils.Ranking;
import index.utils.Score;
import index.utils.query.SpatialIndexQuery;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.vividsolutions.jts.geom.Geometry;

public class SpatialOnlyIndexTest {

	private static final boolean IS_SHOW_TRANSFORMATION_ENABLED = false;
	private static SpatialOnlyIndex spatialOnlyIndex;

	@BeforeClass
	public static void init() {

		String[] docs = new String[] { "This text is about Zürich, Schweiz", "The mayor of London was not amused",
				"Many people died in World War II when Berlin was bombed by the allies" };

		// Terms
				// ITextInformationExtractor extractor = new MockTextInformationExtractor();
				// StopwordRemovalStage stopwordRemovalStage = new StopwordRemovalStage(extractor);
				// StemmingStage stemmingStage = new StemmingStage(extractor);
				// TokenizationStage tokenizationStage = new TokenizationStage(extractor);
				// IndexAndOriginalTokenExtractionStage indexAndOriginalTokenExtractionStage = new IndexAndOriginalTokenExtractionStage(extractor);
				// TermFrequencyExtractionStage termFrequencyExtractionStage = new TermFrequencyExtractionStage(extractor);

				// Spatial
				GeoTaggingStage geoTaggingStage = new GeoTaggingStage(IS_SHOW_TRANSFORMATION_ENABLED);
				GeoReferencingStage geoReferencingStage = new GeoReferencingStage(IS_SHOW_TRANSFORMATION_ENABLED);

				// Chaining the stages together in an appropriate way

				// First text extraction
				// stopwordRemovalStage.setSuccessor(stemmingStage);
				// stemmingStage.setPrecursor(stopwordRemovalStage);
				// stemmingStage.setSuccessor(tokenizationStage);
				// tokenizationStage.setPrecursor(stemmingStage);
				// tokenizationStage.setSuccessor(indexAndOriginalTokenExtractionStage);
				// indexAndOriginalTokenExtractionStage.setPrecursor(stopwordRemovalStage); // so that only the relevant terms are analysed
				// indexAndOriginalTokenExtractionStage.setSuccessor(termFrequencyExtractionStage);
				// termFrequencyExtractionStage.setPrecursor(indexAndOriginalTokenExtractionStage);
				// // Now spatial extraction
				// termFrequencyExtractionStage.setSuccessor(geoTaggingStage);
				// geoTaggingStage.setPrecursor(termFrequencyExtractionStage);
				geoTaggingStage.setSuccessor(geoReferencingStage);
				geoReferencingStage.setPrecursor(geoTaggingStage);

		spatialOnlyIndex = new SpatialOnlyIndex();
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
				spatialDocuments.add(new SpatialDocument(id, geom));
			}

			spatialOnlyIndex.addDocuments(spatialDocuments);
		}

	}

	@Test
	public void queryTest() {
		Ranking ranking = spatialOnlyIndex.queryIndex(new SpatialIndexQuery("point_in", "Switzerland"));
//		for (Score d : ranking.getResults()) {
//			System.out.println(d.getDocid() + ": "+ d.getScore());
//		}
		for (Score d : ranking.getResults()) {
			if (d.getDocid() == 1) {
				assertEquals(1f, d.getScore(), 0.01f);
			} else { 
				assertEquals(0f, d.getScore(), 0.01f);
			}
		}
	}

}
