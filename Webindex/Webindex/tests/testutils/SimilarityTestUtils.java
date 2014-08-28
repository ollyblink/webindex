package testutils;

import index.textindex.implementations.ITextIndexNoInsertion;
import index.textindex.utils.Term;
import index.textindex.utils.informationextractiontools.MockTextInformationExtractor;
import index.utils.DBDataManagerTest;
import index.utils.Document;
import index.utils.indexmetadata.TextIndexMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.dbcrud.DBDataManager;

public class SimilarityTestUtils {
	public String query = "to do";
	public HashMap<Term, Integer> queryTermFreqs;
	public ArrayList<Term> terms;
	public HashMap<Term, List<Document>> docSubset;
	public TextIndexMetaData metaData;
	public ITextIndexNoInsertion index; 

	public SimilarityTestUtils() {
		new DBDataManagerTest().initTest();
		index = DBDataManager.getTestIndex();
		
		query = "to do";
		queryTermFreqs = new MockTextInformationExtractor().fullTransformation(query);
		terms = new ArrayList<>(queryTermFreqs.keySet());
		docSubset = index.getSubsetFor(terms); 
		metaData = index.getMetaData(docSubset);
		
		
	}

}
