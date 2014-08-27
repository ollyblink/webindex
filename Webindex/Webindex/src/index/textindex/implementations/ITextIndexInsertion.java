package index.textindex.implementations;

import index.girindex.IGIRIndex;
import index.textindex.utils.Term;
import index.utils.Document;

import java.util.List;
import java.util.Map;

/**
 * Only insertion methods..
 * The reason to segregate {@link ITextIndex} is that {@link IGIRIndex} does not need normal document insertion methods
 * @author rsp
 *
 */
public interface ITextIndexInsertion {
	/**
	 * Add a new document and all terms that occur in that document to the index
	 * @param document
	 * @param termInDocuments
	 */
	public void addDocument(Document document, List<Term> termInDocuments);
	
	/**
	 * Add multiple documents
	 * @param documents
	 */
	public void addDocuments(Map<Document, List<Term>> documents);
	
	/**
	 * Add one new document to the index
	 * 
	 * @param text
	 */
	public void addTerms(Term term, List<Document> documents);

	/**
	 * Add multiple new terms and their corresponding documents to the index
	 * 
	 * @param texts
	 */
	public void addTerms(Map<Term, List<Document>> documents);

}
