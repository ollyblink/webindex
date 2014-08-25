package index.textindex.similarities.vectorspacemodels;

import static org.junit.Assert.assertEquals;
import index.textindex.implementations.DBIndexTest;
import index.textindex.similarities.probabilisticmodels.BM1Test;
import index.textindex.similarities.tfidfweighting.DocTFIDFTypes;
import index.textindex.similarities.tfidfweighting.Formula3TFStrategy;
import index.textindex.similarities.tfidfweighting.QueryIDFTypes;
import index.textindex.utils.Term;
import index.textindex.utils.texttransformation.MockTextTokenizer;
import index.utils.DBDataProvider;
import index.utils.IndexDocument;
import index.utils.Ranking;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CosineSimilarityTest {

	private static CosineSimilarity similarity;
	private static DBDataProvider dbDataProvider;

	@BeforeClass
	public static void init() {
		DBIndexTest.initDB();
		dbDataProvider = new DBDataProvider(DBIndexTest.dbManager, null, 1);
		similarity = new CosineSimilarity(new Formula3TFStrategy(), QueryIDFTypes.TERM_IDF1, DocTFIDFTypes.DOC_TFIDF3);
	}

	@Test
	public void testCalculateSimilarity() {
		String query = "to do";
		HashMap<Term, Integer> queryTerms = new MockTextTokenizer().transform(query);
		ArrayList<String> indexedTerms = BM1Test.getTermFreqsForQuery(queryTerms);
		ArrayList<IndexDocument> documents = dbDataProvider.getDocTermKeyValues(indexedTerms, true);
		Ranking hits = similarity.calculateSimilarity(new TextIndexQuery(query, "cosine", true), queryTerms, documents, true);
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
