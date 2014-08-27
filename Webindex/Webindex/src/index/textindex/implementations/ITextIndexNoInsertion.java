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

/**
 * All the needed methods for a text index without text insertions
 * 
 * @author rsp
 *
 */
public interface ITextIndexNoInsertion {

	/**
	 * Query the text index with a textual input
	 * 
	 * @param query
	 * @return
	 */
	public Ranking queryIndex(TextIndexQuery query);

	/**
	 * Empties the current index. no terms/docs in the index anymore
	 */
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
	 * 
	 * @param terms
	 * @return
	 */
	public HashMap<Term, List<Document>> getSubsetFor(List<Term> terms);

	/**
	 * Retrieve some meta data used for similarity calculations
	 * 
	 * @return
	 */
	public TextIndexMetaData getMetaData();

	/**
	 * Retrieve some meta data used for similarity calculations for a subset of documents and terms
	 * 
	 * @return
	 */
	public TextIndexMetaData getMetaData(Map<Term, List<Document>> docTerms);

}
