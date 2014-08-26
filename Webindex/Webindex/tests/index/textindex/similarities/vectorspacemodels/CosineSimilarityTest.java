package index.textindex.similarities.vectorspacemodels;

import static org.junit.Assert.assertEquals;
import index.textindex.similarities.tfidfweighting.DocTFIDFTypes;
import index.textindex.similarities.tfidfweighting.Formula3TFStrategy;
import index.textindex.similarities.tfidfweighting.QueryIDFTypes;
import index.utils.DBDataProviderTest;
import index.utils.Ranking;
import index.utils.Score;
import index.utils.SimilarityTestUtils;
import index.utils.query.TextIndexQuery;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CosineSimilarityTest {

	private static CosineSimilarity similarity;
	private static SimilarityTestUtils similarityTestUtils;

	@BeforeClass
	public static void init() {
		similarityTestUtils = new SimilarityTestUtils(); 
		similarity = new CosineSimilarity(new Formula3TFStrategy(), QueryIDFTypes.TERM_IDF1, DocTFIDFTypes.DOC_TFIDF3);
	}

	@Test
	public void testCalculateSimilarity() {

		Ranking hits = similarity.calculateSimilarity(new TextIndexQuery(similarityTestUtils.query, "cosine", true),
				similarityTestUtils.queryTermFreqs, similarityTestUtils.docSubset, similarityTestUtils.metaData, false);
		HashMap<Long, Float> values = new HashMap<Long, Float>();
		values.put(1l, 0.660f);
		values.put(2l, 0.408f);
		values.put(3l, 0.118f);
		values.put(4l, 0.058f);
		for (Score res : hits) {
			System.out.println(res);
		}
		for (Score res : hits) {
			assertEquals(values.get(res.getDocid()), res.getScore(), 0.001f);
		}
	}

	@AfterClass
	public static void tearDown() {
		// DBDataProviderTest.index.dropTables();
		DBDataProviderTest.index.close();

	}
}
