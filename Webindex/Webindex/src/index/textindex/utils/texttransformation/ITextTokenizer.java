package index.textindex.utils.texttransformation;

import index.textindex.utils.Term;

import java.util.ArrayList;
import java.util.HashMap;

public interface ITextTokenizer  { 
	public HashMap<Term, Integer> transform(String text);
 
}
