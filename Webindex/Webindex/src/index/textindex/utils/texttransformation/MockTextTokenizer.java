package index.textindex.utils.texttransformation;

import index.textindex.utils.Term;

import java.util.HashMap;

/**
 * Does nothing with the text. Only used for testing purposes
 * 
 * @author rsp
 *
 */
public class MockTextTokenizer extends AbstractTextTokenizer {

 

	@Override
	public HashMap<Term, Integer> tokenize(String text) { 
		String[] tokens = text.toLowerCase().replace("."," ").replace(","," ").replace(";"," ").replace("'", "´").toLowerCase().split("\\s+");
		HashMap<Term, Integer> indexTerms = new HashMap<Term, Integer>();
 
		for (String token : tokens) {   
			Term term = new Term(token, token);
			Integer frequency = indexTerms.get(term);
			if (frequency == null) { 
				frequency = 0;
			}
			frequency = frequency + 1;
			indexTerms.put(term, frequency);
		}

		return indexTerms;
	}
  
	
 
}
