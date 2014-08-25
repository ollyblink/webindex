package index.textindex;

import index.textindex.utils.TextIndexMetaData;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.Ranking;
import index.utils.query.TextIndexQuery;

import java.util.List;

public interface ITextIndex {

	/**
	 * Add one new document to the index
	 * 
	 * @param text
	 */
	public void addDocument(final String text);

	/**
	 * Add multiple new documents to the index
	 * 
	 * @param texts
	 */
	public void addDocuments(final List<String> texts);

	public void close();

	 
	/**
	 * The tokenizer used in this index to alter the terms into index terms
	 * 
	 * @return
	 */
	public ITextTokenizer getTokenizer();

	public TextIndexMetaData getTextMetaData();

	public Ranking queryIndex(TextIndexQuery query);
}
