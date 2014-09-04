package index.textindex.similarities.probabilisticmodels;

import static org.junit.Assert.assertEquals;
import index.utils.DBDataManagerTest;
import index.utils.Ranking;
import index.utils.Score;
import index.utils.query.TextIndexQuery;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testutils.SimilarityTestUtils;

public class BM25Test {

	private static BestMatch similarity;
	private static SimilarityTestUtils similarityTestUtils;

	@BeforeClass
	public static void init() {
		similarityTestUtils = new SimilarityTestUtils();
		float k1 = 1.2f;
		float b = 0.75f;
		similarity = new BestMatch(new BM25(k1, b));
	}

	@Test
	public void testCalculateSimilarity() {
		Ranking hits = similarity.calculateSimilarity(new TextIndexQuery(similarityTestUtils.query, "bm1", true), similarityTestUtils.queryTermFreqs, similarityTestUtils.docSubset, similarityTestUtils.metaData);

		HashMap<Long, Float> values = new HashMap<Long, Float>();
		values.put(1l, -1.222f);
		values.put(2l, 0f);
		values.put(3l, -1.222f);
		values.put(4l, -1.222f);
		for (Score res : hits) {
			assertEquals(values.get(res.getDocument().getId().getId()), res.getScore(), 0.001f);
		}
	}

	@AfterClass
	public static void tearDown() {
		DBDataManagerTest.dbTablesManager.dropTables();
		DBDataManagerTest.dbDataManager.close();

	}
}
