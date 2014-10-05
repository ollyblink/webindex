package index.textindex.utils.informationextractiontools;

import index.textindex.utils.Term;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public abstract class AbstractTextInformationExtractor implements ITextInformationExtractor {

	@Override
	public HashMap<Term, Integer> fullTransformation(String text) {
		text = text.replace("´", "'");
		return internalFullTransformation(text);
	}

	public HashMap<Term, Integer> internalFullTransformation(String text) {
		text = removeStopwords(text);
		HashMap<String, ArrayList<String>> indexAndOriginalTerms = extractIndexAndOriginalTerms(text);
		HashMap<Term, Integer> termFreqs = getTermFrequencies(indexAndOriginalTerms);
		return termFreqs;
	}

	@Override
	public HashMap<String, ArrayList<String>> extractIndexAndOriginalTerms(String text) {
		HashMap<String, ArrayList<String>> indexOrigTerms = new HashMap<String, ArrayList<String>>();

		// Apply text transformation and extract terms
		List<String> originalTerms = tokenize(text);
		for (String origTerm : originalTerms) {
			String indexTerm = applyStemmer(origTerm);
			ArrayList<String> list = indexOrigTerms.get(indexTerm);
			if (list == null) {
				list = new ArrayList<String>();
				indexOrigTerms.put(indexTerm, list);
			}
			list.add(origTerm);
		}

		return indexOrigTerms;
	}

	@Override
	public HashMap<Term, Integer> getTermFrequencies(Map<String, ArrayList<String>> indexAndOriginalTerms) {
		// Convert index terms and original terms map to
		ArrayList<Term> terms = new ArrayList<>();
		for (String indexTerm : indexAndOriginalTerms.keySet()) {
			Term term = new Term(indexTerm, indexAndOriginalTerms.get(indexTerm));
			terms.add(term);
		}
		return convertToTermFreqMap(terms);
	}

	protected static HashMap<Term, Integer> convertToTermFreqMap(ArrayList<Term> terms) {
		HashMap<Term, Integer> termFreqs = new HashMap<>();
		for (Term term : terms) {
			Integer i = termFreqs.get(term);
			if (i == null) {
				i = 0;
			}
			i += term.getOriginalTerms().size();
			termFreqs.put(term, i);
		}
		return termFreqs;
	}

	@Override
	public String removeStopwords(String text) {
		return internalRemoveStopwords(text);
	}

	@Override
	public String applyStemmer(String text) {
		return internalApplyStemmer(text);
	}

	@Override
	public List<String> tokenize(String text) {
		final Tokenizer source = new StandardTokenizer(Version.LUCENE_4_9, new StringReader(text));
		TokenStream result = new StandardFilter(Version.LUCENE_4_9, source);
		return getTokenList(result);
	}

	protected abstract String internalApplyStemmer(String text);

	protected abstract String internalRemoveStopwords(String text);

	/**
	 * transforms the token stream into a list of tokens
	 * 
	 * @param tokenStream
	 * @return
	 */
	public List<String> getTokenList(TokenStream tokenStream) {
		List<String> tokens = new ArrayList<String>();
		try {
			CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

			tokenStream.reset();

			while (tokenStream.incrementToken()) {
				char[] termBuff = charTermAttribute.buffer();
				int termLen = charTermAttribute.length();
				String token = new String(termBuff, 0, termLen);
				token = token.replace("'","''");
				tokens.add(token);
			}

			tokenStream.end();
			tokenStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tokens;
	}

	/**
	 * Transforms the token stream into a whitespace separated text.
	 * 
	 * @param tokenStream
	 * @return
	 */
	public String getText(TokenStream tokenStream) {
		List<String> tokens = getTokenList(tokenStream);
		String text = "";
		for (String token : tokens) {
			text += token + " ";
		}
		return text.trim();
	}

	/**
	 * Transforms the token stream into Set of tokens
	 * 
	 * @param tokenStream
	 * @return
	 */
	public Set<String> getTokenSet(TokenStream tokenStream) {
		return new HashSet<>(getTokenList(tokenStream));
	}

}
