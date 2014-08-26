package index.textindex.utils.texttransformation;

import index.textindex.utils.Term;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public abstract class AbstractTextTokenizer implements ITextTokenizer {

	@Override
	public HashMap<Term, Integer> transform(String text) {
		text = text.replace("´", "'");
		return tokenize(text);
	}

	public HashMap<Term, Integer> tokenize(String text) {
		ArrayList<Term> terms = new ArrayList<>();
		try {
			String preStemmerText;
			preStemmerText = analyze(text);
			String[] originalTerms = preStemmerText.split(" +");
			Map<String, ArrayList<String>> term_origTerms = stem(originalTerms);

			for (String indexTerm : term_origTerms.keySet()) {
				ArrayList<String> origTerms = term_origTerms.get(indexTerm);
				Term term = new Term(indexTerm, origTerms);
				terms.add(term);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		HashMap<Term, Integer> termFreqs = convertToMap(terms);
		return termFreqs;
	}

	public static HashMap<Term, Integer> convertToMap(ArrayList<Term> terms) {
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

	private Map<String, ArrayList<String>> stem(String[] originalTerms) {
		Map<String, ArrayList<String>> indexOrigTerms = new HashMap<String, ArrayList<String>>();

		for (String origTerm : originalTerms) {
			try {
				String indexTerm = stem(origTerm);
				ArrayList<String> list = indexOrigTerms.get(indexTerm);
				if (list == null) {
					list = new ArrayList<String>();
					indexOrigTerms.put(indexTerm, list);
				}
				list.add(origTerm);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return indexOrigTerms;
	}

	protected String analyze(String text) throws IOException {
		return createString(transformText(text));
	}

	protected String stem(String preStemmerText) throws IOException {
		return createString(applyStemmer(preStemmerText));
	}

	protected abstract TokenStream applyStemmer(String text);

	protected abstract TokenStream transformText(String text);

	public static String createString(TokenStream tokenStream) throws IOException {
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

		tokenStream.reset();

		String s = "";
		while (tokenStream.incrementToken()) {
			char[] termBuff = charTermAttribute.buffer();
			int termLen = charTermAttribute.length();
			s += new String(termBuff, 0, termLen) + " ";
		}

		tokenStream.end();
		tokenStream.close();
		return s;
	}

	public static ArrayList<String> getWordsWithoutTextMarks(String text) throws IOException {
		final Tokenizer source = new StandardTokenizer(Version.LUCENE_4_9, new StringReader(text));
		TokenStream result = new StandardFilter(Version.LUCENE_4_9, source);
		String[] originalText = createString(result).split(" ");
		return new ArrayList<String>(Arrays.asList(originalText));
	}
}
