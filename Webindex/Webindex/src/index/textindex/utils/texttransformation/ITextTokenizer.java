package index.textindex.utils.texttransformation;

import index.textindex.utils.Term;

import java.util.ArrayList;
import java.util.Map;

public interface ITextTokenizer {
	public Map<Term, Integer> transform(String text);

}
