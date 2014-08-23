package index.textindex.utils.texttransformation;

import index.textindex.utils.Term;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class EnglishTextTokenizer extends AbstractTextTokenizer {

	public static void main(String[] args) {
		String s = "Blue House. Nicest nicest A blue house housing built in the year 2002. It isn't the nicest building in the city, but its blue colour is unique in Cansas, U.S.A.";
		EnglishTextTokenizer englishTextTokenizer = new EnglishTextTokenizer();
		HashMap<Term, Integer> transform = englishTextTokenizer.transform(s);
		for(Term term: transform.keySet()){
			System.out.println(term.getIndexedTerm() + ", " +term.getOriginalTerm() +", "+transform.get(term));
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
			
			//has to be done to remove the lower casing again so that the real terms are preserved
			if (originalTerms.length == indexTerms.length) { 
				List<String> originalTextTerms = new ArrayList<String>();
				originalTextTerms.addAll(getWordsWithoutTextMarks(text));
				
				int indexToRemove = -1;
				for(int i = 0; i< originalTerms.length;++i){
					indexToRemove = -1;
					for(int j = 0; j< originalTextTerms.size();++j){
						if(originalTextTerms.get(j).toLowerCase().contains(originalTerms[i])){
							originalTerms[i] = originalTextTerms.get(j);
							indexToRemove = j;
							break;
						}
					}
					if(indexToRemove > -1){
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
		return createString(applyPorterStemmer(preStemmerText));
	}

	private TokenStream applyPorterStemmer(String text) {
		final Tokenizer source = new StandardTokenizer(Version.LUCENE_4_9, new StringReader(text));
		TokenStream result = new StandardFilter(Version.LUCENE_4_9, source);
		return new PorterStemFilter(result);
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
		result = new StopFilter(Version.LUCENE_4_9, result, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		return result;
	}


	 
}
