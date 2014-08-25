package index.textindex.similarities.probabilisticmodels;

import static org.junit.Assert.assertEquals;
import index.textindex.implementations.DBIndexTest;
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

public class BM1Test {
 
	private static BestMatch similarity;
	private static DBDataProvider dbDataProvider;

	@BeforeClass
	public static void init() {
		DBIndexTest.initDB(); 
		dbDataProvider = new DBDataProvider(DBIndexTest.dbManager,null, 1);
		similarity = new BestMatch(dbDataProvider.getTextMetaData(), new BM1());
	}

	@Test
	public void testCalculateSimilarity() {
		String query = "to do";

		HashMap<Term, Integer> queryTerms = new MockTextTokenizer().transform(query);
		ArrayList<String> indexedTerms = getTermFreqsForQuery(queryTerms); 
		ArrayList<IndexDocument> documents = dbDataProvider.getDocTermKeyValues(indexedTerms, true);
		Ranking hits = similarity.calculateSimilarity(new TextIndexQuery(query, "bm1", true), queryTerms, documents, true); 
		
		HashMap<Long, Float> values = new HashMap<Long, Float>();
		values.put(1l, -1.222f);
		values.put(2l, 0f);
		values.put(3l, -1.222f);
		values.put(4l, -1.222f);
		for (IndexDocument res : hits) {
 			assertEquals(values.get(res.getId()), res.getTextIndexDocumentMetaData().getSimilarity(), 0.001f);
		}
	}
	public static ArrayList<String> getTermFreqsForQuery(HashMap<Term, Integer> queryTerms) {
		ArrayList<String> indexedTerms = new ArrayList<String>();
		for (Term term : queryTerms.keySet()) {
			indexedTerms.add(term.getIndexedTerm());
		}
		return indexedTerms;
	}
	@AfterClass
	public static void tearDown() {
		DBIndexTest.dbManager.dropTables();
		DBIndexTest.index.close();

	}
}
