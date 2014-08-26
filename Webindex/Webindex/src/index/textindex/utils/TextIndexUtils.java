package index.textindex.utils;

import index.textindex.similarities.ITextSimilarity;
import index.textindex.similarities.TextSimilarityFactory;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.Document;
import index.utils.Ranking;
import index.utils.Score;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.HashMap;

import utils.dbcrud.DBDataManager;

public class TextIndexUtils {
	public static Ranking performTextQuery(TextIndexQuery query, ITextTokenizer tokenizer, DBDataManager dbDataProvider,
			ArrayList<Score> spatialRanking) {
//
//		// Convert the text query to index terms and frequencies
//		HashMap<Term, Integer> queryTerms = tokenizer.transform(query.getTextQuery());
//
//		// get the indexed documents matching the query terms
//		ArrayList<Document> documents = dbDataProvider.getDocTermKeyValues(transformTermsToStrings(queryTerms), query.isIntersected(),
//				spatialRanking);
//
//		// Get the similarity
//		ITextSimilarity similarityStrategy = TextSimilarityFactory.getSimilarity(query.getSimilarity(), dbDataProvider.getTextMetaData());
//
//		// Calculate the ranking according to that similarity
//		Ranking ranking = similarityStrategy.calculateSimilarity(query, queryTerms, documents, query.isIntersected());

		return null;
	}

	public static ArrayList<String> transformTermsToStrings(HashMap<Term, Integer> queryTerms) {
		ArrayList<String> indexedTerms = new ArrayList<String>();
//		for (Term term : queryTerms.keySet()) {
//			indexedTerms.add(term.getIndexedTerm());
//		}
		return indexedTerms;
	}
}
