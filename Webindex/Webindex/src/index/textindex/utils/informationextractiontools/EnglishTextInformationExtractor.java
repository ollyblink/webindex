package index.textindex.utils.informationextractiontools;

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

public class EnglishTextInformationExtractor extends AbstractTextInformationExtractor {

	public static void main(String[] args) {
		ITextInformationExtractor tok = new EnglishTextInformationExtractor();
		Map<Term, Integer> termFreqs  = tok.fullTransformation("Hello World War world this is a text trial with houses that are blue and green and lanterns");
		for(Term t: termFreqs.keySet()){
			System.out.print (t.getIndexedTerm()+", ");
			for(String s: t.getOriginalTerms()){
				System.out.print(s+", ");
			}
			System.out.println();
		}
	}
	
 

	@Override
	protected String internalApplyStemmer(String text) {
		final Tokenizer source = new StandardTokenizer(Version.LUCENE_4_9, new StringReader(text));
		TokenStream result = new StandardFilter(Version.LUCENE_4_9, source);
		return getText(new PorterStemFilter(result));
	}

	@Override
	protected String internalRemoveStopwords(String text) {
		final Tokenizer source = new StandardTokenizer(Version.LUCENE_4_9, new StringReader(text));
		TokenStream result = new StandardFilter(Version.LUCENE_4_9, source);
		result = new LowerCaseFilter(Version.LUCENE_4_9, result);
		result = new StopFilter(Version.LUCENE_4_9, result, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		return getText(result);
	}


 
	 
	

}
