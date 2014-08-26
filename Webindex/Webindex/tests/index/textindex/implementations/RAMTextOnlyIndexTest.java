package index.textindex.implementations;

import static org.junit.Assert.*;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocs;
import index.utils.Document;
import index.utils.identifers.DocumentIdentifier;
import index.utils.identifers.TermDocsIdentifier;
import index.utils.indexmetadata.TextIndexMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testutils.DBInitializer;
import utils.dbcrud.DBDataManager;
 

public class RAMTextOnlyIndexTest {
	private static RAMTextOnlyIndex index;
	private static DBDataManager dbDataManager;
	private static HashMap<Term, List<Document>> documents;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dbDataManager = new DBDataManager(DBInitializer.getTestDBManager(), null, 1);
	 
		ArrayList<TermDocs> termDocs = dbDataManager.getTermDocs();
		Map<TermDocsIdentifier, TermDocs> termDocsMeta = new HashMap<>();
		
		for(TermDocs t: termDocs){
			termDocsMeta.put(t.getId(), t);
		}
		documents = DBDataManager.createIndexableDocuments();
		index = new RAMTextOnlyIndex(new HashMap<>(), new TextIndexMetaData(termDocsMeta, dbDataManager.getOverallTextIndexMetaData()), null);
		
		index.addDocuments(documents);
	}

	

	@Test
	public void addDocumentTest() {
		
		
		assertEquals(4, index.N());
		for (Term term : index.getAllTerms()) {
			List<Document> indexDocumentList = index.getDocumentsFor(term);
			List<Document> exampleDocumentList = documents.get(term);
			assertEquals(exampleDocumentList.size(), index.ni(term));
			assertEquals(exampleDocumentList.size(), indexDocumentList.size());
			for (Document document : exampleDocumentList) {
				assertTrue(indexDocumentList.contains(document));
			}
		}
	}
	
	@Test
	public void getSubsetForTest(){
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
//		fail();
	}

	@AfterClass
	public static void clear() {
		index.clear();
//		DBInitializer.tearDownTestDB(dbDataManager);
	}

}
