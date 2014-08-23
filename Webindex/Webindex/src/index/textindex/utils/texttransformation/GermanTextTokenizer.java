package index.textindex.utils.texttransformation;

import index.textindex.utils.Term;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.de.GermanStemFilter;
import org.apache.lucene.analysis.miscellaneous.SetKeywordMarkerFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.WordlistLoader;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;

public class GermanTextTokenizer extends AbstractTextTokenizer {

	public static void main(String[] args) {
		String s = "Start am schweizerisch-italienischen Zoll auf dem Splügenpass 2118m. Anfangs auf einem Weg bis zum romantischen Bergseeli 2311m. Dann weglos einzelnen Steinmänner folgend in nordöstlicher Richtung zum Surettajoch 2851m. Über den Nordgrat, Blockgrat mit Kletterstellen im 2. Grad auf den Gipfel des Surettahorn 3027m. Ab der Splügen Passhöhe kann die Tour auch gut als Eintagestour zu Gemüte geführt werden. Schöne und einsame Gegend, keine Modetour im Vergleich zum prominenten Nachbar Pizzo Tambo.";
		GermanTextTokenizer tokenizer = new GermanTextTokenizer();
		HashMap<Term, Integer> transform = tokenizer.transform(s);
		for (Term term : transform.keySet()) {
			System.out.println(term.getIndexedTerm() + ", " + term.getOriginalTerm() + ", " + transform.get(term));
		}
	}

	private CharArraySet snowballWordSet;
//	private Object exclusionSet;

	public GermanTextTokenizer() {
		try {
			snowballWordSet = WordlistLoader.getSnowballWordSet(IOUtils.getDecodingReader(SnowballFilter.class, GermanAnalyzer.DEFAULT_STOPWORD_FILE, StandardCharsets.UTF_8), Version.LUCENE_CURRENT);
//			exclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(Version.LUCENE_4_9, stemExclusionSet));
		} catch (IOException e) { 
			e.printStackTrace();
		}

	}

	@Override
	public HashMap<Term, Integer> tokenize(String text) {
		HashMap<Term, Integer> wordFreqs = new HashMap<Term, Integer>();
		try {
			String preStemmerText = analyze(text);
			String afterStemmerText = stem(preStemmerText);
			String[] originalTerms = preStemmerText.split(" +");
			String[] indexTerms = afterStemmerText.split(" +");

			// has to be done to remove the lower casing again so that the real terms are preserved
			if (originalTerms.length == indexTerms.length) {
				List<String> originalTextTerms = new ArrayList<String>();
				originalTextTerms.addAll(getWordsWithoutTextMarks(text));

				int indexToRemove = -1;
				for (int i = 0; i < originalTerms.length; ++i) {
					indexToRemove = -1;
					for (int j = 0; j < originalTextTerms.size(); ++j) {
						if (originalTextTerms.get(j).toLowerCase().contains(originalTerms[i])) {
							originalTerms[i] = originalTextTerms.get(j);
							indexToRemove = j;
							break;
						}
					}
					if (indexToRemove > -1) {
						originalTextTerms.remove(indexToRemove);
					}
				}

				for (int i = 0; i < indexTerms.length; ++i) {
					Term term = new Term(indexTerms[i].replace("'", "''"), originalTerms[i].replace("'", "''"));

					Integer frequency = wordFreqs.get(term);

					if (frequency == null) {
						frequency = new Integer(0);
					}
					frequency += 1;
					wordFreqs.put(term, frequency);
				}
			} else {
				throw new Exception("length of original and index terms array is not the same.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wordFreqs;
	}

	public static ArrayList<String> getWordsWithoutTextMarks(String text) throws IOException {
		final Tokenizer source = new StandardTokenizer(Version.LUCENE_4_9, new StringReader(text));
		TokenStream result = new StandardFilter(Version.LUCENE_4_9, source);
		String[] originalText = createString(result).split(" ");
		return new ArrayList<String>(Arrays.asList(originalText));
	}

	private String stem(String preStemmerText) throws IOException {
		return createString(applyStemmer(preStemmerText));
	}

	private TokenStream applyStemmer(String text) {
		final Tokenizer source = new StandardTokenizer(Version.LUCENE_4_9, new StringReader(text));
		TokenStream result = new StandardFilter(Version.LUCENE_4_9, source);
		
	    
		return new GermanStemFilter(result);
	}

	private String analyze(String text) throws IOException {
		return createString(transformText(text));
	}

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

	private TokenStream transformText(String text) {
		final Tokenizer source = new StandardTokenizer(Version.LUCENE_4_9, new StringReader(text));
		TokenStream result = new StandardFilter(Version.LUCENE_4_9, source);
		result = new LowerCaseFilter(Version.LUCENE_4_9, result);
		result = new StopFilter(Version.LUCENE_4_9, result, snowballWordSet); 
//	    result = new SetKeywordMarkerFilter(result, exclusionSet); 
		return result;
	}

}