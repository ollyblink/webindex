package index.textindex.utils.texttransformation;

import index.textindex.utils.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Does nothing with the text. Only used for testing purposes
 * 
 * @author rsp
 *
 */
public class MockTextTokenizer implements ITextTokenizer {

	public static void main(String[] args) {
		ITextTokenizer tok = new MockTextTokenizer();
		Map<Term, Integer> termFreqs = tok.transform("Hello World War world this is a text trial with houses that are blue and green and lanterns");
		for (Term t : termFreqs.keySet()) {
			System.out.print(t.getIndexedTerm() + ":::");
			for (String s : t.getOriginalTerms()) {
				System.out.print(s + "/");
			}
			System.out.println();
		}
	}

	@Override
	public HashMap<Term, Integer>  transform(String text) {
		ArrayList<Term> terms = new ArrayList<Term>();
		String[] originalTerms = text.toLowerCase().replace(".", " ").replace(",", " ").replace(";", " ").replace("'", "´").toLowerCase()
				.split("\\s+");
		Map<String, ArrayList<String>> term_origTerms = new HashMap<String, ArrayList<String>>();
		for (String orig : originalTerms) {
			ArrayList<String> origTerms = term_origTerms.get(orig);
			if (origTerms == null) {
				origTerms = new ArrayList<String>();
				term_origTerms.put(orig, origTerms);
			}
			origTerms.add(orig);
		}

		for (String indexTerm : term_origTerms.keySet()) {
			ArrayList<String> origTerms = term_origTerms.get(indexTerm);
			Term term = new Term(indexTerm, origTerms);
			terms.add(term);
		}
		

		return AbstractTextTokenizer.convertToMap(terms);
	}

}
