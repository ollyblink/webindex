package index.textindex.utils.informationextractiontools;

import index.textindex.utils.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ITextInformationExtractor {
	/**
	 * Accomplishes full transformation including tokenization, stop word removal and stemming
	 * additionally, terms and their frequencies are extracted.
	 * */
	public HashMap<Term, Integer> fullTransformation(String text);

	/**
	 * Only tokenizes the text. no further transformation
	 * 
	 * @param text
	 * @return tokenized text
	 */
	public List<String> tokenize(String text);

	/**
	 * Removes stopwords
	 * 
	 * @param tokenizedText
	 * @return
	 */
	public String removeStopwords(String text);

	/**
	 * Stems words
	 * 
	 * @param tokenizedText
	 * @return
	 */
	public String applyStemmer(String text);
	
	/**
	 * Used to extract index terms and original terms from an input text
	 * @param text
	 * @return
	 */
	public HashMap<String/*index terms*/, ArrayList<String>/*Original terms*/> extractIndexAndOriginalTerms(String text);
	
	/**
	 * Calculates the term frequency for a given term set.
	 * @param indexAndOriginalTerms
	 * @return
	 */
	public HashMap<Term, Integer> getTermFrequencies(Map<String/*Index terms*/, ArrayList<String>/*OriginalTerms*/> indexAndOriginalTerms);
}
