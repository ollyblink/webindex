package index.textindex.similarities;

import index.textindex.ITextIndex;
import index.textindex.utils.Term;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.Ranking;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractTextSimilarity implements ITextSimilarity {
	protected ITextTokenizer queryTokenizer;
	protected ITextIndex index;

	public AbstractTextSimilarity(ITextIndex index, ITextTokenizer queryTokenizer){
		this.index = index;
		this.queryTokenizer = queryTokenizer;
	}
	@Override
	public Ranking calculateSimilarity(String query, boolean isIntersected) {
	 
		HashMap<Term, Integer> queryTerms = queryTokenizer.transform(query); 
		ArrayList<String> indexedTerms = new ArrayList<String>();
		for(Term term: queryTerms.keySet()){
			indexedTerms.add(term.getIndexedTerm() );
		}
		return calculateSimilarity(query, indexedTerms, isIntersected);
	}
	
	protected abstract Ranking calculateSimilarity(String query, ArrayList<String> indexedTerms, boolean isIntersected);
}
