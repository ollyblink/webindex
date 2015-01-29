package index.textindex.implementations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import index.spatialindex.utils.geolocating.georeferencing.IPlaceExtractor;
import index.spatialindex.utils.geolocating.georeferencing.LocationFinder;
import index.spatialindex.utils.geolocating.georeferencing.YPMPlaceExtractor;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocs;
import index.utils.Document;
import index.utils.identifers.TermDocsIdentifier;
import index.utils.indexmetadata.TextIndexMetaData;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import rest.dao.RESTScore;
import rest.dao.Ranking;
import testutils.DBInitializer;
import utils.dbcrud.DBDataManager;

public class RAMTextOnlyIndexTest {
	private static RAMTextOnlyIndex index;
	private static DBDataManager dbDataManager;
	private static HashMap<Term, List<Document>> terms;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Create location finder
		LocationFinder locationFinder = new LocationFinder();
		dbDataManager = new DBDataManager(DBInitializer.initDB(), null, locationFinder, true);
		terms = DBDataManager.createIndexableDocuments();
		ArrayList<TermDocs> termDocs = dbDataManager.getTermDocs(null);
		HashMap<TermDocsIdentifier, TermDocs> termDocsMeta = new HashMap<>();

		for (TermDocs t : termDocs) {
			termDocsMeta.put(t.getId(), t);
		}
		index = new RAMTextOnlyIndex(new TextIndexMetaData(termDocsMeta, dbDataManager.getOverallTextIndexMetaData()), null);

		index.addTerms(terms);
	}

	@Test
	public void addTermTest() {

		assertEquals(4, index.N());
		for (Term term : index.getAllTerms()) {
			List<Document> indexDocumentList = index.getDocumentsFor(term);
			List<Document> exampleDocumentList = terms.get(term);
			assertEquals(exampleDocumentList.size(), index.ni(term));
			assertEquals(exampleDocumentList.size(), indexDocumentList.size());
			for (Document document : exampleDocumentList) {
				assertTrue(indexDocumentList.contains(document));
			}
		}
	}

	@Test
	public void addDocumentTest() {

		Map<Document, List<Term>> documents = new HashMap<Document, List<Term>>();
		for (Term term : terms.keySet()) {
			List<Document> docs = terms.get(term);
			for (Document doc : docs) {
				List<Term> list = documents.get(doc);
				if (list == null) {
					list = new ArrayList<>();
					documents.put(doc, list);
				}
				list.add(term);
			}
		}

		for (Document doc : documents.keySet()) {
			index.addDocument(doc, documents.get(doc));
		}

		index.clear();
		assertEquals(4, index.N());
		for (Term term : index.getAllTerms()) {
			List<Document> indexDocumentList = index.getDocumentsFor(term);
			List<Document> exampleDocumentList = terms.get(term);
			assertEquals(exampleDocumentList.size(), index.ni(term));
			assertEquals(exampleDocumentList.size(), indexDocumentList.size());
			for (Document document : exampleDocumentList) {
				assertTrue(indexDocumentList.contains(document));
			}
		}

		index.addDocuments(documents);

		index.clear();
		assertEquals(4, index.N());
		for (Term term : index.getAllTerms()) {
			List<Document> indexDocumentList = index.getDocumentsFor(term);
			List<Document> exampleDocumentList = terms.get(term);
			assertEquals(exampleDocumentList.size(), index.ni(term));
			assertEquals(exampleDocumentList.size(), indexDocumentList.size());
			for (Document document : exampleDocumentList) {
				assertTrue(indexDocumentList.contains(document));
			}
		}
	}

	@Test
	public void getSubsetForTest() {
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Term("to"));
		terms.add(new Term("do"));
		HashMap<Term, List<Document>> subsetFor = index.getSubsetFor(terms);
		List<Document> toList = subsetFor.get(new Term("to"));
		List<Document> doList = subsetFor.get(new Term("do"));
		assertTrue(toList.size() == 2);
		assertTrue(toList.contains(new Document(1l)));
		assertTrue(toList.contains(new Document(2l)));
		assertFalse(toList.contains(new Document(3l)));
		assertFalse(toList.contains(new Document(4l)));

		assertTrue(doList.size() == 3);
		assertTrue(doList.contains(new Document(1l)));
		assertFalse(doList.contains(new Document(2l)));
		assertTrue(doList.contains(new Document(3l)));
		assertTrue(doList.contains(new Document(4l)));
	}

	@Test
	public void queryIndex() {
		HashMap<Long, Float> trialScores = new HashMap<Long, Float>();
		trialScores.put(1l, 0.660f);
		trialScores.put(2l, 0.408f);
		trialScores.put(3l, 0.118f);
		trialScores.put(4l, 0.058f);

		TextIndexQuery query = new TextIndexQuery("to do", "cosine3", false);
		Ranking ranking = index.queryIndex(query);

		for (RESTScore score : ranking.getResults()) {
			assertEquals(trialScores.get(score.getDocument().getId().getId()), score.getScore(), 0.01);
		}
	}

	@AfterClass
	public static void clear() {
		index.clear();
	}

}
