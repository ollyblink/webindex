package index.textindex.utils;

import index.textindex.similarities.ITextSimilarity;
import index.textindex.similarities.TextSimilarityFactory;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.DBDataProvider;
import index.utils.IndexDocument;
import index.utils.Ranking;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextIndexUtils {
	public static Ranking performTextQuery(TextIndexQuery query, ITextTokenizer tokenizer, DBDataProvider dbDataProvider, Ranking spatialRanking) {
		List<IndexDocument> indexDocuments = null;
		if(spatialRanking != null && (spatialRanking.getResults() != null || spatialRanking.getResults().size() > 0)){
			indexDocuments = new ArrayList<>(spatialRanking.getResults().keySet());
		}
		
		//Convert the text query to index terms and frequencies
		HashMap<Term, Integer> queryTerms = tokenizer.transform(query.getTextQuery());
		  
		// get the indexed documents matching the query terms
		ArrayList<IndexDocument> documents = dbDataProvider.getDocTermKeyValues(transformTermsToStrings(queryTerms), query.isIntersected(), indexDocuments);
		
		//Get the similarity
		ITextSimilarity similarityStrategy = TextSimilarityFactory.getSimilarity(query.getSimilarity(), dbDataProvider.getTextMetaData());
		
		//Calculate the ranking according to that similarity
		Ranking ranking = similarityStrategy.calculateSimilarity(query, queryTerms, documents, query.isIntersected());
		
		return ranking;
	}

	public static ArrayList<String> transformTermsToStrings(HashMap<Term, Integer> queryTerms) {
		ArrayList<String> indexedTerms = new ArrayList<String>();
		for (Term term : queryTerms.keySet()) {
			indexedTerms.add(term.getIndexedTerm());
		}
		return indexedTerms;
	}
}
