package index.textindex.implementations;

import index.spatialindex.utils.GeometryConverter;
import index.textindex.similarities.ITextSimilarity;
import index.textindex.similarities.TextSimilarityFactory;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocs;
import index.textindex.utils.informationextractiontools.ITextInformationExtractor;
import index.textindex.utils.informationextractiontools.MockTextInformationExtractor;
import index.utils.Document;
import index.utils.Score;
import index.utils.identifers.TermDocsIdentifier;
import index.utils.indexmetadata.TextIndexMetaData;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rest.dao.RESTTextQueryMetaData;
import rest.dao.Ranking;

/**
 * This class implements a simple RAM inverted index based on a {@link HashMap}
 * 
 * @author rsp
 *
 */
public class RAMTextOnlyIndex implements ITextIndex {

	/** Index storing all terms and document occurrence lists */
	private Map<Term /* Term */, List<Document> /* Document occurrences */> index;

	/** This is used to speed up similarity calculations and contains important information about term-document combinations */
	private TextIndexMetaData indexMetaData;

	/** The used tokenizer to analyse the query */
	private ITextInformationExtractor tokenizer;

	public RAMTextOnlyIndex(TextIndexMetaData indexMetaData, ITextInformationExtractor tokenizer) {
		this.index = new HashMap<>();
		this.indexMetaData = indexMetaData;
		this.tokenizer = (tokenizer == null ? new MockTextInformationExtractor() : tokenizer);
	}

	public RAMTextOnlyIndex(HashMap<Term, List<Document>> predefinedIndex, TextIndexMetaData indexMetaData, ITextInformationExtractor tokenizer) {
		this.index = predefinedIndex;
		this.indexMetaData = indexMetaData;
		this.tokenizer = (tokenizer == null ? new MockTextInformationExtractor() : tokenizer);
	}

	@Override
	public void addDocument(Document document, List<Term> termInDocuments) {
		for (Term term : termInDocuments) {
			retrieveDocumentListFromIndex(term).add(document);
		}
	}

	@Override
	public void addDocuments(Map<Document, List<Term>> documents) {
		for (Document doc : documents.keySet()) {
			addDocument(doc, documents.get(doc));
		}
	}

	@Override
	public void addTerms(Term term, List<Document> documents) {
		if (term == null || documents == null) {
			return;
		}
		retrieveDocumentListFromIndex(term).addAll(documents);
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
	public void addTerms(Map<Term, List<Document>> documents) {
		if (documents == null) {
			return;
		}
		for (Term term : documents.keySet()) {
			addTerms(term, documents.get(term));
		}
	}

	@Override
	public Ranking queryIndex(TextIndexQuery query) {
		// Tokenize query, create terms and calculate term occurrences (fiq) in query
		HashMap<Term, Integer> queryTermFreqs = tokenizer.fullTransformation(query.getTextQuery());
		// Retrieve the relevant documents from the index (only those that contain one or more query term)
		HashMap<Term, List<Document>> relevantDocuments = getAllRelevantDocs(queryTermFreqs.keySet(), queryTermFreqs.keySet().size(), query.isIntersected());
		// Create the correct similarity
		ITextSimilarity currentSimilarity = TextSimilarityFactory.create(query.getSimilarity());
		// Calculate the similarity between the query and the documents and create a ranked list of documents
		ArrayList<Score> scoreList = currentSimilarity.calculateSimilarity(query, queryTermFreqs, relevantDocuments, indexMetaData);
		// Sort according to highest score
		Collections.sort(scoreList);

		// Collect metadata
		Ranking ranking = collectMetaData(query, scoreList, queryTermFreqs);
		return ranking;
	}

	private Ranking collectMetaData(TextIndexQuery query, ArrayList<Score> results, HashMap<Term, Integer> queryTermFreqs) {
		Ranking ranking = new Ranking();
		RESTTextQueryMetaData textMeta = new RESTTextQueryMetaData();
		textMeta.setScores(GeometryConverter.convertScoresToREST(results));
		textMeta.setOverallTextIndexMetaData(this.indexMetaData.getOverallIndexMetaData());
		textMeta.setQueryTermFrequencies(queryTermFreqs);
		ArrayList<TermDocs> termDocs = new ArrayList<TermDocs>();
		for (Term t : queryTermFreqs.keySet()) {
			for (Score s : results) {
				TermDocs termDoc = indexMetaData.getTermDocRelationship().get(new TermDocsIdentifier(t.getIndexedTerm().getTermId(), s.getDocument().getId().getId()));
				if (termDoc != null) {
					termDocs.add(termDoc);
				}
			}
		}
		textMeta.setTermDocs(termDocs);
		textMeta.setTextIntersected(query.isIntersected());
		textMeta.setTextQuery(query.getTextQuery());
		textMeta.setTextSimilarity(query.getSimilarity());
		textMeta.setPrintableQuery(getTextPartOfQuery(query.getTextQuery(), query.isIntersected()));
		ranking.setTextQueryMetaData(textMeta);

		return ranking;
	}

	private String convertToBooleanValues(boolean bool) {
		if (bool) {
			return " AND ";
		} else {
			return " OR ";
		}
	}

	private String getTextPartOfQuery(String textquery, boolean isTextIntersected) {
		if (textquery == null || textquery.trim().length() == 0) {
			return "";
		} else {
			return "<" + textquery.replaceAll("[^öÖäÄüÜéÉàÀèÈßa-zA-Z ']", "").replace(" ", " " + convertToBooleanValues(isTextIntersected) + " ") + ">";
		}
	}

	private HashMap<Term, List<Document>> getAllRelevantDocs(Iterable<Term> terms, int numberOfQueryTerms, boolean isIntersected) {
		Map<Long, Integer> counter = new HashMap<Long, Integer>();

		HashMap<Term, List<Document>> relevantDocuments = new HashMap<>();
		for (Term term : terms) {
			Term actualTerm = findActualTerm(term);
			if (actualTerm != null) {
				ArrayList<String> originalTerms = actualTerm.getOriginalTerms();
				ArrayList<String> copyOrigTerms = new ArrayList<String>();
				copyOrigTerms.addAll(originalTerms);

				Term termCopy = new Term(actualTerm.getIndexedTerm().getTermId(), copyOrigTerms, actualTerm.getNi(), actualTerm.getTermIdf1(), actualTerm.getTermIdf2());
				List<Document> docs = index.get(actualTerm);

				if (docs != null) {
					List<Document> docCopies = new ArrayList<>();
					for (Document d : docs) {
						docCopies.add(new Document(d.getId().getId(), d.getFulltext(), d.getSizeInBytes(), d.getIndexedNrOfWords(), d.getRawNrOfWords(), d.getDocVectorNorm1(), d.getDocVectorNorm2(), d.getDocVectorNorm3()));
					}
					relevantDocuments.put(termCopy, docCopies);
					addDocumentsToCounter(counter, docCopies);
				}
			}
		}

		checkForIntersection(relevantDocuments, counter, numberOfQueryTerms, isIntersected);
		return relevantDocuments;
	}

	private void addDocumentsToCounter(Map<Long, Integer> counter, List<Document> docs) {
		for (Document doc : docs) {
			long docid = doc.getId().getId();
			Integer count = counter.get(docid);
			if (count == null) {
				count = 0;
			}
			count++;
			counter.put(doc.getId().getId(), count);
		}
	}

	private void checkForIntersection(HashMap<Term, List<Document>> relevantDocuments, Map<Long, Integer> counter, int numberOfQueryTerms, boolean isIntersected) {
		if (isIntersected) {
			Set<Term> keys = relevantDocuments.keySet();
			for (Term term : keys) {
				List<Document> termDocsToRemove = new ArrayList<>();
				List<Document> termDocs = relevantDocuments.get(term);
				for (Document d : termDocs) {
					if (counter.get(d.getId().getId()).intValue() != numberOfQueryTerms) {
						termDocsToRemove.add(d);
					}
				}
				for (Document d : termDocsToRemove) {
					termDocs.remove(d);
				}
			}
		}
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
		HashMap<TermDocsIdentifier, TermDocs> subSet = new HashMap<TermDocsIdentifier, TermDocs>();

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
