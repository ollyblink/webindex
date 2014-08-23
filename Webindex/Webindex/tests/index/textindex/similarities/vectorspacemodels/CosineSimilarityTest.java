package index.textindex.similarities.vectorspacemodels;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import index.textindex.DBIndexTest;
import index.textindex.similarities.tfidfweighting.DocTFIDFTypes;
import index.textindex.similarities.tfidfweighting.Formula3TFStrategy;
import index.textindex.similarities.tfidfweighting.QueryIDFTypes;
import index.textindex.similarities.vectorspacemodels.CosineSimilarity;
import index.utils.IndexDocument;
import index.utils.Ranking;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CosineSimilarityTest {

	private static CosineSimilarity similarity;

	@BeforeClass
	public static void init() {
		DBIndexTest.initDB();
		similarity = new CosineSimilarity(DBIndexTest.index, DBIndexTest.index.getTokenizer(), new Formula3TFStrategy(), QueryIDFTypes.TERM_IDF1, DocTFIDFTypes.DOC_TFIDF3);
	}

	@Test
	public void testCalculateSimilarity() {
		Ranking hits = similarity.calculateSimilarity("to do", false); 
		HashMap<Long, Float> values = new HashMap<Long, Float>();
		values.put(1l, 0.660f);
		values.put(2l, 0.408f);
		values.put(3l, 0.118f);
		values.put(4l, 0.058f);
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
