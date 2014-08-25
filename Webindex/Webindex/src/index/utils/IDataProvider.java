package index.utils;

import index.textindex.utils.TextIndexMetaData;
import index.textindex.utils.texttransformation.ITextTokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IDataProvider {
	/**
	 * Returns the number of documents in the index (normally called N)
	 * 
	 * @return
	 */
	public int getNrOfDocs();

	/**
	 * Returns the number of documents that contain the given term This parameter normally is called ni.
	 * 
	 * @param term
	 *            the term i to find the given number of documents it occurs in (ni) for
	 * @return
	 */
	public HashMap<String, Integer> getNrOfDocsWithTerms(List<String> terms);

	/**
	 * Returns the number of times a term occurs in a document defined by docid HashMap<DocId, HashMap<Term, fij>> getFijs(Map<DocId, List<DocTerms>) Returns null for fij if the term does not occur in a document. Due to the fact that the docTerms may not contain all the terms,
	 */
	public HashMap<Long, HashMap<String, Integer>> getFijs(Map<Long, List<String>> docTerms);

	/**
	 * Returns the number of index terms contained in the document specified by docid. This is the number of actually indexed terms that may have undergone text transformations like stemming.
	 * 
	 * @param docs
	 *            (only need to specify their ids)
	 * @return the word count for the index terms in the document specified by docid
	 */
	public HashMap<Long, HashMap<String, Integer>> getFijsOfAllTermsInDocuments(List<IndexDocument> docs);

	/**
	 * f
	 * 
	 * @param docid
	 * @return
	 */
	public HashMap<Long, String> getDocumentsTexts(List<Long> docids);

	/**
	 * Returns a sorted set of all terms contained in the index
	 * 
	 * @return
	 */
	public Set<String> getAllTerms();

	/**
	 * Returns all documents in which a given term occurs for multipleOnes
	 */
	public HashMap<String, List<IndexDocument>> getDocumentsForTerms(List<String> terms);

	/**
	 * Returns a list of all documents of the index, sorted by document id. A document's id is a number assigned at the time of indexing.
	 * 
	 * @return
	 */
	public Set<IndexDocument> getAllDocuments();

	/**
	 * Returns the number of index terms contained in the document specified by docid. This is the number of actually indexed terms that may have undergone text transformations like stemming.
	 * 
	 * @param docid
	 * @return the word count for the index terms in the document specified by docid
	 */
	public HashMap<Long, Integer> getIndexedNrOfTermsInDocuments(List<Long> docids);



	/**
	 * Retrieves documents for query terms and additional information for those document terms.
	 * 
	 * @param queryTerms
	 * @param isIntersected
	 * @return
	 */
	public ArrayList<IndexDocument> getDocTermKeyValues(List<String> queryTerms, boolean isIntersected);

	public TextIndexMetaData getTextMetaData();
}
