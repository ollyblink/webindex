package index.utils;

import index.textindex.implementations.ITextIndex;
import index.textindex.utils.Term;
import index.textindex.utils.texttransformation.MockTextTokenizer;
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
	public ITextIndex index; 

	public SimilarityTestUtils() {
		DBDataProviderTest.initDB();
		index = DBDataManager.getTestIndex();
		
		query = "to do";
		queryTermFreqs = new MockTextTokenizer().transform(query);
		terms = new ArrayList<>(queryTermFreqs.keySet());
		docSubset = index.getSubsetFor(terms); 
		metaData = index.getMetaData(docSubset);
		
		
	}

}
