package index.girindex.utils.girtexttransformation.informationextractiontools;

import index.textindex.utils.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Does nothing with the text. Only used for testing purposes
 * 
 * @author rsp
 *
 */
public class MockTextInformationExtractor extends AbstractTextInformationExtractor {

	public static void main(String[] args) {
		ITextInformationExtractor tok = new MockTextInformationExtractor();
		Map<Term, Integer> termFreqs = tok
				.fullTransformation("Hello World War world this is a text trial with houses that are blue and green and lanterns");
		for (Term t : termFreqs.keySet()) {
			System.out.print(t.getIndexedTerm() + ":::");
			for (String s : t.getOriginalTerms()) {
				System.out.print(s + "/");
			}
			System.out.println();
		}
	}

	@Override
	public List<String> tokenize(String text) {
		String[] tokens = text.toLowerCase().replace(".", " ").replace(",", " ").replace(";", " ").replace("'", "´").toLowerCase().split("\\s+");
		List<String> tokenList = new ArrayList<>();
		for (String token : tokens) {
			tokenList.add(token);
		}
		return tokenList;
	}

	@Override
	protected String internalApplyStemmer(String text) {
		return text;
	}

	@Override
	protected String internalRemoveStopwords(String text) {
		return text;
	}

}
