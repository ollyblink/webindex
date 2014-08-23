package index.textindex.utils.texttransformation;

import index.textindex.utils.Term;

import java.util.HashMap;

public abstract class AbstractTextTokenizer implements ITextTokenizer {

	@Override
	public HashMap<Term, Integer> transform(String text) { 
		text = text.replace("´","'"); 
		return tokenize(text);
	}

	protected abstract HashMap<Term, Integer> tokenize(String text);

}
