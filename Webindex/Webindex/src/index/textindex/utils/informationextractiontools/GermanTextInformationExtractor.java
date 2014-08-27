package index.textindex.utils.informationextractiontools;

import index.textindex.utils.Term;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.de.GermanStemFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.WordlistLoader;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;

public class GermanTextInformationExtractor extends AbstractTextInformationExtractor {

	private CharArraySet snowballWordSet;

	public GermanTextInformationExtractor() {
		try {
			snowballWordSet = WordlistLoader
					.getSnowballWordSet(
							IOUtils.getDecodingReader(SnowballFilter.class, GermanAnalyzer.DEFAULT_STOPWORD_FILE, StandardCharsets.UTF_8),
							Version.LUCENE_4_9);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String internalApplyStemmer(String text) {
		final Tokenizer source = new StandardTokenizer(Version.LUCENE_4_9, new StringReader(text));
		TokenStream result = new StandardFilter(Version.LUCENE_4_9, source);
		return getText(new GermanStemFilter(result));
	}

	@Override
	protected String internalRemoveStopwords(String text) {
		final Tokenizer source = new StandardTokenizer(Version.LUCENE_4_9, new StringReader(text));
		TokenStream result = new StandardFilter(Version.LUCENE_4_9, source);
		result = new LowerCaseFilter(Version.LUCENE_4_9, result);
		result = new StopFilter(Version.LUCENE_4_9, result, snowballWordSet);
		return getText(result);
	}

	public static void main(String[] args) {
		ITextInformationExtractor tok = new EnglishTextInformationExtractor();
		Map<Term, Integer> termFreqs = tok
				.fullTransformation("Besonders gravierend wirkt sich der Koordinationsabzug bei mehrere Stellen aus. Das zeigt das Beispiel eines Angestellten, der Teilzeit bei zwei Arbeitgebern leistet. Vom Arbeitgeber A erhält er einen Jahreslohn von 30‘000 Franken. Zuerst wird der Koordinationsabzug gemacht, es können noch total 5430 Franken der Lohnsumme in der Pensionskasse versichert werden. Arbeitgeber B zahlt einen Jahreslohn von 50‘000 Franken. Der Koordinationsbezug wird nochmals gemacht. Der in der PK verbliebene Lohn beträgt dann 25‘430 Franken.");
		for (Term t : termFreqs.keySet()) {
			System.out.print(t.getIndexedTerm() + ", ");
			for (String s : t.getOriginalTerms()) {
				System.out.print(s + ", ");
			}
			System.out.println();
		}
	}

}