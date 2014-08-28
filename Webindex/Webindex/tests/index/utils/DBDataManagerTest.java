package index.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import index.spatialindex.utils.SpatialDocument;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocs;
import index.textindex.utils.informationextractiontools.MockTextInformationExtractor;
import index.utils.identifers.TermDocsIdentifier;
import index.utils.indexmetadata.OverallTextIndexMetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import testutils.DBInitializer;
import testutils.SwissProvider;
import utils.dbcrud.DBDataManager;
import utils.dbcrud.DBTablesManager;
import utils.dbcrud.IDataManager;

import com.vividsolutions.jts.geom.Polygon;

public class DBDataManagerTest {

	public static Map<Long, List<String>> testDocTerms;
	public static IDataManager dbDataManager;
	public static DBTablesManager dbTablesManager;
	public static final long d1 = 1;
	public static final long d2 = 2;
	public static final long d3 = 3;
	public static final long d4 = 4;

	@Before
	public void initTest() {
		dbTablesManager = DBInitializer.initDB();
		dbDataManager = new DBDataManager(dbTablesManager, new MockTextInformationExtractor(), 60, true);
	}

	@After
	public void tearDownTest() {
		DBInitializer.tearDownTestDB(dbTablesManager);
	}

	private static String[] getDocs(String type) {
		switch (type) {
		case "spatial":
			return new String[] { "This text is about Zürich, Schweiz", "The mayor of London was not amused", "Many people died in World War II when Berlin was bombed by the allies" };
		case "text":
		default:
			return new String[] { "To do is to be. To be is to do.", "To be or not to be. I am what I am.", "I think therefore I am. Do be do be do.", "Do do do, da da da. Let it be, let it be." };
		}
	}

	@Test
	public void testAddDocuments() {
		populateDB("text");
		assertEquals(4, dbDataManager.getDocuments().size());
		assertEquals(14, dbDataManager.getTerms().size());
		assertEquals(22, dbDataManager.getTermDocs().size());
	}

	private void populateDB(String text) {
		List<String> documents = new ArrayList<>();
		String[] docs = getDocs(text);
		for (String doc : docs) {
			documents.add(doc);
		}

		dbDataManager.addDocuments(documents);
	}

	@Test
	public void testAddDocumentDeferred() {

		String[] docs = getDocs("text");
		for (int i = 0; i < docs.length; ++i) {
			dbDataManager.addDocumentDeferred(docs[i]);
		}

		while (!dbDataManager.isUpdated() || !dbDataManager.isDocumentQueueEmptyEmpty()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		assertEquals(4, dbDataManager.getDocuments().size());
		assertEquals(14, dbDataManager.getTerms().size());
		assertEquals(22, dbDataManager.getTermDocs().size());
	}

	@Test
	public void testGetDocuments() {
		populateDB("text");

		float[] norms = { 5.068f, 4.889f, 3.762f, 7.735f };
		int[] nrWords = { 10, 11, 10, 12 };

		ArrayList<Document> docs = dbDataManager.getDocuments();
		assertEquals(4, docs.size());

		for (int i = 0; i < docs.size(); ++i) {
			assertEquals(norms[i], docs.get(i).getDocVectorNorm3(), 0.01f);
			assertEquals(norms[i], docs.get(i).getDocVectorNorm3(), 0.01f);
			assertEquals(norms[i], docs.get(i).getDocVectorNorm3(), 0.01f);
			assertEquals(norms[i], docs.get(i).getDocVectorNorm3(), 0.01f);
			assertEquals(nrWords[i], docs.get(i).getIndexedNrOfWords());
		}
	}

	@Test
	public void testGetTerms() {
		populateDB("text");
		ArrayList<Term> terms = dbDataManager.getTerms();

		assertEquals(14, terms.size());
		for (Term term : terms) {
			if (term.getIndexedTerm().getTermId().equalsIgnoreCase("to")) {
				assertEquals(2, term.getNi());
				assertEquals(1f, term.getTermIdf1(), 0.01f);
			} else if (term.getIndexedTerm().getTermId().equalsIgnoreCase("do")) {
				assertEquals(3, term.getNi());
				assertEquals(0.415f, term.getTermIdf1(), 0.01f);

			} else if (term.getIndexedTerm().getTermId().equalsIgnoreCase("is")) {
				assertEquals(1, term.getNi());
				assertEquals(2f, term.getTermIdf1(), 0.01f);

			} else if (term.getIndexedTerm().getTermId().equalsIgnoreCase("be")) {
				assertEquals(4, term.getNi());
				assertEquals(0f, term.getTermIdf1(), 0.01f);

			} else if (term.getIndexedTerm().getTermId().equalsIgnoreCase("or")) {
				assertEquals(1, term.getNi());
				assertEquals(2f, term.getTermIdf1(), 0.01f);

			} else if (term.getIndexedTerm().getTermId().equalsIgnoreCase("not")) {
				assertEquals(1, term.getNi());
				assertEquals(2f, term.getTermIdf1(), 0.01f);

			} else if (term.getIndexedTerm().getTermId().equalsIgnoreCase("I")) {
				assertEquals(2, term.getNi());
				assertEquals(1f, term.getTermIdf1(), 0.01f);

			} else if (term.getIndexedTerm().getTermId().equalsIgnoreCase("am")) {
				assertEquals(2, term.getNi());
				assertEquals(1f, term.getTermIdf1(), 0.01f);

			} else if (term.getIndexedTerm().getTermId().equalsIgnoreCase("what")) {
				assertEquals(1, term.getNi());
				assertEquals(2f, term.getTermIdf1(), 0.01f);

			} else if (term.getIndexedTerm().getTermId().equalsIgnoreCase("think")) {
				assertEquals(1, term.getNi());
				assertEquals(2f, term.getTermIdf1(), 0.01f);

			} else if (term.getIndexedTerm().getTermId().equalsIgnoreCase("therefore")) {
				assertEquals(1, term.getNi());
				assertEquals(2f, term.getTermIdf1(), 0.01f);

			} else if (term.getIndexedTerm().getTermId().equalsIgnoreCase("da")) {
				assertEquals(1, term.getNi());
				assertEquals(2f, term.getTermIdf1(), 0.01f);

			} else if (term.getIndexedTerm().getTermId().equalsIgnoreCase("let")) {
				assertEquals(1, term.getNi());
				assertEquals(2f, term.getTermIdf1(), 0.01f);

			} else if (term.getIndexedTerm().getTermId().equalsIgnoreCase("it")) {
				assertEquals(1, term.getNi());
				assertEquals(2f, term.getTermIdf1(), 0.01f);
			}
		}

	}

	@Test
	public void testGetTermDocs() {
		populateDB("text");

		float[][] tfidfs = { { 3f, 0.83f, 4f }, { 2, 2, 2, 2, 2, 2 }, { 1.073f, 2, 1, 2, 2 }, { 1.073f, 5.170f, 4, 4 } };
		List<TermDocs> termDocs = dbDataManager.getTermDocs();
		assertEquals(22, termDocs.size());
		for (int i = 0; i <= 18; ++i) {
			if (termDocs.get(i).getId().equals(new TermDocsIdentifier("to", 1l))) {
				assertEquals(tfidfs[0][0], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("do", 1l))) {
				assertEquals(tfidfs[0][1], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("is", 1l))) {
				assertEquals(tfidfs[0][2], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("to", 2l))) {
				assertEquals(tfidfs[1][0], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("or", 2l))) {
				assertEquals(tfidfs[1][1], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("not", 2l))) {
				assertEquals(tfidfs[1][2], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("I", 2l))) {
				assertEquals(tfidfs[1][3], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("am", 2l))) {
				assertEquals(tfidfs[1][4], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("what", 2l))) {
				assertEquals(tfidfs[1][5], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("do", 3l))) {
				assertEquals(tfidfs[2][0], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("I", 3l))) {
				assertEquals(tfidfs[2][1], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("am", 3l))) {
				assertEquals(tfidfs[2][2], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("think", 3l))) {
				assertEquals(tfidfs[2][3], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("therefore", 3l))) {
				assertEquals(tfidfs[2][4], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("do", 4l))) {
				assertEquals(tfidfs[3][0], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("da", 4l))) {
				assertEquals(tfidfs[3][1], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("let", 4l))) {
				assertEquals(tfidfs[3][2], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			} else if (termDocs.get(i).getId().equals(new TermDocsIdentifier("it", 4l))) {
				assertEquals(tfidfs[3][3], termDocs.get(i).getDocTermTfidf3(), 0.01f);

			}
		}
	}

	@Test
	public void getOverallIndexMetaData() {
		populateDB("text");
		OverallTextIndexMetaData meta = dbDataManager.getOverallTextIndexMetaData();
		float avgNrWords = (10f + 11f + 10f + 12f) / 4f;
		float avgVectNorm = (5.068f + 4.899f + 3.762f + 7.738f) / 4f;
		assertEquals(4, meta.getN());
		assertEquals(avgNrWords, meta.getAvgDocLengthRawNrOfWords(), 0.01f);
		assertEquals(avgVectNorm, meta.getAvgDocLengthVectorNorm3(), 0.01f);
	}

	@Test
	public void testGetLocations() {
		populateDB("spatial");
		ArrayList<SpatialDocument> locations = dbDataManager.getLocations(null);
		assertEquals(19, locations.size());
		Polygon swissMBR = SwissProvider.getSwitzerlandMBR();
		assertTrue(swissMBR.contains(locations.get(0).getDocumentFootprint()));

		for (int i = 1; i < locations.size(); ++i) {
			assertFalse(swissMBR.contains(locations.get(i).getDocumentFootprint()));
		}
	}

	@AfterClass
	public static void close() {
		dbDataManager.close();
	}
}
