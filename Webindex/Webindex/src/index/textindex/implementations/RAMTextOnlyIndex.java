package index.textindex.implementations;

import index.textindex.similarities.ITextSimilarity;
import index.textindex.similarities.tfidfweighting.DocTFIDFTypes;
import index.textindex.similarities.tfidfweighting.Formula3TFStrategy;
import index.textindex.similarities.tfidfweighting.QueryIDFTypes;
import index.textindex.similarities.vectorspacemodels.CosineSimilarity;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocs;
import index.utils.Document;
import index.utils.Ranking;
import index.utils.identifers.TermDocsIdentifier;
import index.utils.indexmetadata.TextIndexMetaData;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a simple RAM inverted index based on a {@link HashMap}
 * 
 * @author rsp
 *
 */
public class RAMTextOnlyIndex implements ITextIndex {
	private static final ITextSimilarity DEFAULT_SIMILARITY = new CosineSimilarity(new Formula3TFStrategy(), QueryIDFTypes.TERM_IDF1,
			DocTFIDFTypes.DOC_TFIDF3);

	/** Index storing all terms and document occurrence lists */
	private Map<Term /* Term */, List<Document> /* Document occurrences */> index;

	/** This is used to speed up similarity calculations and contains important information about term-document combinations */
	private TextIndexMetaData indexMetaData;

	/** The currently used similarity */
	private ITextSimilarity currentSimilarity;

	public RAMTextOnlyIndex(Map<Term, List<Document>> index, TextIndexMetaData indexMetaData, ITextSimilarity currentSimilarity) {
		this.index = index;
		this.indexMetaData = indexMetaData;
		this.currentSimilarity = (currentSimilarity == null ? DEFAULT_SIMILARITY : currentSimilarity);
	}

	@Override
	public void addDocument(Term term, Document document) {
		if (term == null || document == null) {
			return;
		}
		retrieveDocumentListFromIndex(term).add(document);
	}

	private List<Document> retrieveDocumentListFromIndex(Term term) {
		if (term == null) {
			return null;
		}
		List<Document> documents = index.get(term);
		if (documents == null) {
			documents = new ArrayList<Document>();
			index.put(term, documents);
		}
		return documents;
	}

	@Override
	public void addDocuments(Map<Term, List<Document>> documents) {
		if (documents == null) {
			return;
		}
		for (Term term : documents.keySet()) { 
			for (Document doc : documents.get(term)) {
				addDocument(term, doc);
			}
		}
	}

	@Override
	public void close() {
		index.clear();
		System.gc();
	}

	@Override
	public Ranking queryIndex(TextIndexQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSimilarity(ITextSimilarity similarity) {
		this.currentSimilarity = similarity;
	}

	@Override
	public void clear() {
		this.index.clear();
	}

	@Override
	public int N() {
		return indexMetaData.getOverallIndexMetaData().getN();
	}

	@Override
	public int ni(Term term) {
		for (Term indexTerm : index.keySet()) {
			if (indexTerm.equals(term)) {
				return indexTerm.getNi();
			}
		}
		return 0;
	}

	@Override
	public Iterable<Term> getAllTerms() {
		return index.keySet();
	}

	@Override
	public List<Document> getDocumentsFor(Term term) {
		return index.get(term);
	}

	@Override
	public TextIndexMetaData getMetaData() {
		return indexMetaData;
	}

	@Override
	public HashMap<Term, List<Document>> getSubsetFor(List<Term> terms) {
 

		HashMap<Term, List<Document>> subset = new HashMap<Term, List<Document>>();
		for (Term term : terms) {
			List<Document> docs = index.get(term);
			if (docs != null) {
				Term actualTerm = findActualTerm(term);  
				subset.put(actualTerm, docs);
			}
		}
		 
		return subset;
	}

	private Term findActualTerm(Term term) {
		for (Term t : index.keySet()) {
			if (t.equals(term)) { 
				return t;
			}
		}
		return null;
	}

	@Override
	public TextIndexMetaData getMetaData(Map<Term, List<Document>> docTerms) {
		Map<TermDocsIdentifier, TermDocs> subSet = new HashMap<TermDocsIdentifier, TermDocs>();

		for (Term term : docTerms.keySet()) { 
			for (Document document : docTerms.get(term)) {
				TermDocsIdentifier termDocsIdentifier = new TermDocsIdentifier(term.getIndexedTerm().getTermId(), document.getId().getId()); 
				subSet.put(termDocsIdentifier, indexMetaData.getTermDocRelationship().get(termDocsIdentifier)); 
			}
		}
		TextIndexMetaData textIndexMetaData = new TextIndexMetaData(subSet, indexMetaData.getOverallIndexMetaData());

		return textIndexMetaData;
	}

}
