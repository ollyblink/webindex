package index.textindex;

import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.DBDataProvider;

public abstract class AbstractTextIndex implements ITextIndex {

	protected DBDataProvider dbDataProvider;
	protected ITextTokenizer tokenizer;

	public AbstractTextIndex(DBDataProvider dbDataProvider, ITextTokenizer tokenizer) {
		this.dbDataProvider = dbDataProvider;
		this.tokenizer = tokenizer;
		dbDataProvider.initializeDB();
	}
  
 

	@Override
	public void close() {
		dbDataProvider.close();
	}

	@Override
	public ITextTokenizer getTokenizer() {
		return tokenizer;
	}

}
