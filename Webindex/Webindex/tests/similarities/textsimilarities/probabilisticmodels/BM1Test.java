package similarities.textsimilarities.probabilisticmodels;

import static org.junit.Assert.assertEquals;
import index.textindex.DBIndexTest;
import index.textindex.similarities.probabilisticmodels.BM1;
import index.textindex.similarities.probabilisticmodels.BestMatch;
import index.utils.IndexDocument;
import index.utils.Ranking;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BM1Test {
 
	private static BestMatch similarity;

	@BeforeClass
	public static void init() {
		DBIndexTest.initDB();
		similarity = new BestMatch(DBIndexTest.index, DBIndexTest.index.getTokenizer(), new BM1());
	}

	@Test
	public void testCalculateSimilarity() {
		Ranking hits = similarity.calculateSimilarity("to do", true); 
		
		HashMap<Long, Float> values = new HashMap<Long, Float>();
		values.put(1l, -1.222f);
		values.put(2l, 0f);
		values.put(3l, -1.222f);
		values.put(4l, -1.222f);
		for (IndexDocument res : hits) {
 			assertEquals(values.get(res.getId()), res.getTextIndexDocumentMetaData().getSimilarity(), 0.001f);
		}
	}

	@AfterClass
	public static void tearDown() {
		DBIndexTest.dbManager.dropTables();
		DBIndexTest.index.close();

	}
}
