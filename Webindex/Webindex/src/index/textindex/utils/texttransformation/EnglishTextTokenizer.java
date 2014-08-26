package index.textindex.utils.texttransformation;

import index.textindex.utils.Term;

import java.io.StringReader;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

public class EnglishTextTokenizer extends AbstractTextTokenizer {

	public static void main(String[] args) {
		ITextTokenizer tok = new EnglishTextTokenizer();
		Map<Term, Integer> termFreqs  = tok.transform("Hello World War world this is a text trial with houses that are blue and green and lanterns");
		for(Term t: termFreqs.keySet()){
			System.out.print (t.getIndexedTerm()+", ");
			for(String s: t.getOriginalTerms()){
				System.out.print(s+", ");
			}
			System.out.println();
		}
	}
	
 

	@Override
	protected TokenStream applyStemmer(String text) {
		final Tokenizer source = new StandardTokenizer(Version.LUCENE_4_9, new StringReader(text));
		TokenStream result = new StandardFilter(Version.LUCENE_4_9, source);
		return new PorterStemFilter(result);
	}

	@Override
	protected TokenStream transformText(String text) {
		final Tokenizer source = new StandardTokenizer(Version.LUCENE_4_9, new StringReader(text));
		TokenStream result = new StandardFilter(Version.LUCENE_4_9, source);
		result = new LowerCaseFilter(Version.LUCENE_4_9, result);
		result = new StopFilter(Version.LUCENE_4_9, result, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		return result;
	}
	
	 
	

}
