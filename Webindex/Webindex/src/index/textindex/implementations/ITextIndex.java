package index.textindex.implementations;

import index.textindex.similarities.ITextSimilarity;
import index.textindex.utils.Term;
import index.utils.Document;
import index.utils.Ranking;
import index.utils.indexmetadata.TextIndexMetaData;
import index.utils.query.TextIndexQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ITextIndex {

	/**
	 * Add one new document to the index
	 * 
	 * @param text
	 */
	public void addDocument(Term term, Document document);

	/**
	 * Add multiple new documents to the index
	 * 
	 * @param texts
	 */
	public void addDocuments(Map<Term, List<Document>> documents);

	public void close();

	public Ranking queryIndex(TextIndexQuery query);

	public void setSimilarity(ITextSimilarity similarity);

	public void clear();

	/**
	 * Retrieves the number of documents in the index
	 * 
	 * @return
	 */
	public int N();

	/**
	 * retrieves the number of documents the term occurs in
	 * 
	 * @param term
	 * @return
	 */
	public int ni(Term term);

	/**
	 * retrieves all terms indexed.
	 * 
	 * @return
	 */
	public Iterable<Term> getAllTerms();

	/**
	 * retrieves all documents for a term
	 * 
	 * @param term
	 * @return
	 */
	public List<Document> getDocumentsFor(Term term);
	/**
	 * Retrieves a subset of the given index containing both term and documents that term occurs in
	 * @param terms
	 * @return
	 */
	public HashMap<Term, List<Document>> getSubsetFor(List<Term> terms);
	
	/**
	 * Retrieve some meta data used for similarity calculations
	 * @return
	 */
	public TextIndexMetaData getMetaData();
	
	
	
	/**
	 * Retrieve some meta data used for similarity calculations for a subset of documents and terms
	 * @return
	 */
	public TextIndexMetaData getMetaData(Map<Term, List<Document>> docTerms);

}
