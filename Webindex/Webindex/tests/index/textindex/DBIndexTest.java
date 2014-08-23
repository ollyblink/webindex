package index.textindex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import index.textindex.utils.Term;
import index.textindex.utils.texttransformation.MockTextTokenizer;
import index.utils.DBManager;
import index.utils.IndexDocument;
import index.utils.dbconnection.PGDBConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DBIndexTest {

	public static Map<Long, List<String>> testDocTerms;
	public static DBTextIndex index;
	public static DBManager dbManager;
	public static final long d1 = 1;
	public static final long d2 = 2;
	public static final long d3 = 3;
	public static final long d4 = 4;

	@BeforeClass
	public static void init() {
		initDB();

	}

	public static void initDB() {

		MockTextTokenizer tokenizer = new MockTextTokenizer();
		String[] docs = { "To do is to be. To be is to do.", "To be or not to be. I am what I am.", "I think therefore I am. Do be do be do.", "Do do do, da da da. Let it be, let it be." };
		
		
		dbManager = DBManager.getTestDBManager();
		index = DBManager.initTestTextDB(tokenizer, dbManager, docs); 

		testDocTerms = new HashMap<Long, List<String>>();
		long counter = 1;

		for (String doc : docs) {
			HashMap<Term, Integer> termFreqs = tokenizer.transform(doc);
			List<String> indexedTerms = new ArrayList<String>();

			for (Term t : termFreqs.keySet()) {
				indexedTerms.add(t.getIndexedTerm());
			}
			testDocTerms.put(counter++, indexedTerms);
		}

	}

	

	@Test
	public void testNrOfDocs() {
		assertEquals(4, index.getNrOfDocs());
	}

	@Test
	public void testNrOfDocsWithTerm() {
		Set<String> terms = new HashSet<>();
		for (List<String> ts : testDocTerms.values()) {
			terms.addAll(ts);
		}
		HashMap<String, Integer> results = index.getNrOfDocsWithTerms(new ArrayList<String>(terms));
		String term = "to";
		assertEquals(2, (int) results.get(term));
		term = "do";
		assertEquals(3, (int) results.get(term));
		term = "is";
		assertEquals(1, (int) results.get(term));
		term = "be";
		assertEquals(4, (int) results.get(term));
		term = "or";
		assertEquals(1, (int) results.get(term));
		term = "not";
		assertEquals(1, (int) results.get(term));
		term = "i";
		assertEquals(2, (int) results.get(term));
		term = "am";
		assertEquals(2, (int) results.get(term));
		term = "what";
		assertEquals(1, (int) results.get(term));
		term = "think";
		assertEquals(1, (int) results.get(term));
		term = "therefore";
		assertEquals(1, (int) results.get(term));
		term = "da";
		assertEquals(1, (int) results.get(term));
		term = "let";
		assertEquals(1, (int) results.get(term));
		term = "it";
		assertEquals(1, (int) results.get(term));

	}

	@Test
	public void testGetFijsOfAllTermsInDocuments() {
		HashMap<Long, HashMap<String, Integer>> fijs = index.getFijs(testDocTerms);

		String term = "to";
		assertEquals(4, (int) fijs.get(d1).get(term));
		assertEquals(2, (int) fijs.get(d2).get(term));
		assertEquals(null, fijs.get(d3).get(term));
		assertEquals(null, fijs.get(d4).get(term));

		term = "do";
		assertEquals(2, (int) fijs.get(d1).get(term));
		assertEquals(null, fijs.get(d2).get(term));
		assertEquals(3, (int) fijs.get(d3).get(term));
		assertEquals(3, (int) fijs.get(d4).get(term));

		term = "is";
		assertEquals(2, (int) fijs.get(d1).get(term));
		assertEquals(null, fijs.get(d2).get(term));
		assertEquals(null, fijs.get(d3).get(term));
		assertEquals(null, fijs.get(d4).get(term));

		term = "be";
		assertEquals(2, (int) fijs.get(d1).get(term));
		assertEquals(2, (int) fijs.get(d2).get(term));
		assertEquals(2, (int) fijs.get(d3).get(term));
		assertEquals(2, (int) fijs.get(d4).get(term));

		term = "or";
		assertEquals(null, fijs.get(d1).get(term));
		assertEquals(1, (int) fijs.get(d2).get(term));
		assertEquals(null, fijs.get(d3).get(term));
		assertEquals(null, fijs.get(d4).get(term));

		term = "not";
		assertEquals(null, fijs.get(d1).get(term));
		assertEquals(1, (int) fijs.get(d2).get(term));
		assertEquals(null, fijs.get(d3).get(term));
		assertEquals(null, fijs.get(d4).get(term));

		term = "i";
		assertEquals(null, fijs.get(d1).get(term));
		assertEquals(2, (int) fijs.get(d2).get(term));
		assertEquals(2, (int) fijs.get(d3).get(term));
		assertEquals(null, fijs.get(d4).get(term));

		term = "am";
		assertEquals(null, fijs.get(d1).get(term));
		assertEquals(2, (int) fijs.get(d2).get(term));
		assertEquals(1, (int) fijs.get(d3).get(term));
		assertEquals(null, fijs.get(d4).get(term));

		term = "what";
		assertEquals(null, fijs.get(d1).get(term));
		assertEquals(1, (int) fijs.get(d2).get(term));
		assertEquals(null, fijs.get(d3).get(term));
		assertEquals(null, fijs.get(d4).get(term));

		term = "think";
		assertEquals(null, fijs.get(d1).get(term));
		assertEquals(null, fijs.get(d2).get(term));
		assertEquals(1, (int) fijs.get(d3).get(term));
		assertEquals(null, fijs.get(d4).get(term));

		term = "therefore";
		assertEquals(null, fijs.get(d1).get(term));
		assertEquals(null, fijs.get(d2).get(term));
		assertEquals(1, (int) fijs.get(d3).get(term));
		assertEquals(null, fijs.get(d4).get(term));

		term = "da";
		assertEquals(null, fijs.get(d1).get(term));
		assertEquals(null, fijs.get(d2).get(term));
		assertEquals(null, fijs.get(d3).get(term));
		assertEquals(3, (int) fijs.get(d4).get(term));

		term = "let";
		assertEquals(null, fijs.get(d1).get(term));
		assertEquals(null, fijs.get(d2).get(term));
		assertEquals(null, fijs.get(d3).get(term));
		assertEquals(2, (int) fijs.get(d4).get(term));

		term = "it";
		assertEquals(null, fijs.get(d1).get(term));
		assertEquals(null, fijs.get(d2).get(term));
		assertEquals(null, fijs.get(d3).get(term));
		assertEquals(2, (int) fijs.get(d4).get(term));
	}

	@Test
	public void testFij() {
		HashMap<Long, List<String>> docTerms = new HashMap<Long, List<String>>();
		List<String> terms = new ArrayList<String>();
		terms.add("to");
		terms.add("do");
		docTerms.put(d1, terms);
		HashMap<Long, HashMap<String, Integer>> fijs = index.getFijs(docTerms);

		assertEquals(4, (int) fijs.get(d1).get("to"));
		assertEquals(2, (int) fijs.get(d1).get("do"));
		assertEquals(null, fijs.get(d1).get("be"));

	}

	@Test
	public void testGetDocumentText() {
		HashMap<Long, String> fulltexts = index.getDocumentsTexts(new ArrayList<Long>(testDocTerms.keySet()));
		assertEquals("To do is to be. To be is to do.", fulltexts.get(d1));
		assertEquals("To be or not to be. I am what I am.", fulltexts.get(d2));
		assertEquals("I think therefore I am. Do be do be do.", fulltexts.get(d3));
		assertEquals("Do do do, da da da. Let it be, let it be.", fulltexts.get(d4));
	}

	@Test
	public void testGetAllTerms() {
		Set<String> terms = index.getAllTerms();
		assertEquals(14, terms.size());
		terms.remove("to");
		assertEquals(13, terms.size());
		terms.remove("do");
		assertEquals(12, terms.size());
		terms.remove("is");
		assertEquals(11, terms.size());
		terms.remove("be");
		assertEquals(10, terms.size());
		terms.remove("or");
		assertEquals(9, terms.size());
		terms.remove("not");
		assertEquals(8, terms.size());
		terms.remove("i");
		assertEquals(7, terms.size());
		terms.remove("am");
		assertEquals(6, terms.size());
		terms.remove("what");
		assertEquals(5, terms.size());
		terms.remove("think");
		assertEquals(4, terms.size());
		terms.remove("therefore");
		assertEquals(3, terms.size());
		terms.remove("da");
		assertEquals(2, terms.size());
		terms.remove("let");
		assertEquals(1, terms.size());
		terms.remove("it");
		assertEquals(0, terms.size());
	}

	@Test
	public void testGetDocumentsForTerm() {
		List<String> terms = new ArrayList<String>();
		for (List<String> docTerms : testDocTerms.values()) {
			terms.addAll(docTerms);
		}
		HashMap<String, List<IndexDocument>> documentsForTerms = index.getDocumentsForTerms(terms);

		List<IndexDocument> docs = documentsForTerms.get("to");
		assertTrue(docs.contains(new IndexDocument(d1, "")));
		assertTrue(docs.contains(new IndexDocument(d2, "")));

		docs = documentsForTerms.get("do");
		assertTrue(docs.contains(new IndexDocument(d1, "")));
		assertTrue(docs.contains(new IndexDocument(d3, "")));
		assertTrue(docs.contains(new IndexDocument(d4, "")));

		docs = documentsForTerms.get("is");
		assertTrue(docs.contains(new IndexDocument(d1, "")));

		docs = documentsForTerms.get("be");
		assertTrue(docs.contains(new IndexDocument(d1, "")));
		assertTrue(docs.contains(new IndexDocument(d2, "")));
		assertTrue(docs.contains(new IndexDocument(d3, "")));
		assertTrue(docs.contains(new IndexDocument(d4, "")));

		docs = documentsForTerms.get("or");
		assertTrue(docs.contains(new IndexDocument(d2, "")));

		docs = documentsForTerms.get("not");
		assertTrue(docs.contains(new IndexDocument(d2, "")));

		docs = documentsForTerms.get("i");
		assertTrue(docs.contains(new IndexDocument(d3, "")));
		assertTrue(docs.contains(new IndexDocument(d2, "")));

		docs = documentsForTerms.get("am");
		assertTrue(docs.contains(new IndexDocument(d3, "")));
		assertTrue(docs.contains(new IndexDocument(d2, "")));

		docs = documentsForTerms.get("what");
		assertTrue(docs.contains(new IndexDocument(d2, "")));

		docs = documentsForTerms.get("think");
		assertTrue(docs.contains(new IndexDocument(d3, "")));

		docs = documentsForTerms.get("therefore");
		assertTrue(docs.contains(new IndexDocument(d3, "")));

		docs = documentsForTerms.get("da");
		assertTrue(docs.contains(new IndexDocument(d4, "")));

		docs = documentsForTerms.get("let");
		assertTrue(docs.contains(new IndexDocument(d4, "")));

		docs = documentsForTerms.get("it");
		assertTrue(docs.contains(new IndexDocument(d4, "")));

	}

	@Test
	public void testGetAllDocuments() {
		Set<IndexDocument> documents = index.getAllDocuments();
		assertEquals(4, documents.size());
		assertTrue(documents.contains(new IndexDocument(d1)));
		assertTrue(documents.contains(new IndexDocument(d2)));
		assertTrue(documents.contains(new IndexDocument(d3)));
		assertTrue(documents.contains(new IndexDocument(d4)));
	}

	@Test
	public void testNrOfTermsIn() {
		List<Long> list = new ArrayList<>();
		list.add(d1);
		assertTrue(index.getIndexedNrOfTermsInDocuments(list).get(d1) == 4);
		list.clear();
		list.add(d2);
		assertTrue(index.getIndexedNrOfTermsInDocuments(list).get(d2) == 7);
		list.clear();
		list.add(d3); 
		assertTrue(index.getIndexedNrOfTermsInDocuments(list).get(d3) == 6);
		list.clear();
		list.add(d4); 
		assertTrue(index.getIndexedNrOfTermsInDocuments(list).get(d4) == 5);
	}

	@AfterClass
	public static void tearDown() {
		DBManager.tearDownTestDB(dbManager);
	}

}
