package index.textindex.implementations;

import index.textindex.AbstractTextIndex;
import index.textindex.similarities.ITextSimilarity;
import index.textindex.similarities.TextSimilarityFactory;
import index.textindex.utils.Term;
import index.textindex.utils.TextIndexMetaData;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.DBDataProvider;
import index.utils.IndexDocument;
import index.utils.Ranking;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBTextOnlyIndex extends AbstractTextIndex {

	public DBTextOnlyIndex(DBDataProvider dbDataProvider, ITextTokenizer tokenizer) {
		super(dbDataProvider, tokenizer);
	}

	@Override
	public void addDocument(String text) {
		dbDataProvider.addDocument(text); 
	}

	@Override
	public void addDocuments(List<String> texts) {
		dbDataProvider.addDocuments(texts); 
	}

	@Override
	public Ranking queryIndex(TextIndexQuery query) {
		//Convert the text query to index terms and frequencies
		HashMap<Term, Integer> queryTerms = tokenizer.transform(query.getTextQuery());
		  
		// get the indexed documents matching the query terms
		ArrayList<IndexDocument> documents = dbDataProvider.getDocTermKeyValues(getTermFreqsForQuery(queryTerms), query.isIntersected());
		
		//Get the similarity
		ITextSimilarity similarityStrategy = TextSimilarityFactory.getSimilarity(query.getSimilarity(), dbDataProvider.getTextMetaData());
		
		//Calculate the ranking according to that similarity
		Ranking ranking = similarityStrategy.calculateSimilarity(query, queryTerms, documents, query.isIntersected());
		
		return ranking;
	}


	@Override
	public TextIndexMetaData getTextMetaData() {
		return dbDataProvider.getTextMetaData();
	}
	
	

	private ArrayList<String> getTermFreqsForQuery(HashMap<Term, Integer> queryTerms) {
		ArrayList<String> indexedTerms = new ArrayList<String>();
		for (Term term : queryTerms.keySet()) {
			indexedTerms.add(term.getIndexedTerm());
		}
		return indexedTerms;
	}

}
