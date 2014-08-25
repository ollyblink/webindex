package index.textindex.implementations;

import index.textindex.AbstractTextIndex;
import index.textindex.similarities.ITextSimilarity;
import index.textindex.similarities.TextSimilarityFactory;
import index.textindex.utils.Term;
import index.textindex.utils.TextIndexMetaData;
import index.textindex.utils.TextIndexUtils;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.DBDataProvider;
import index.utils.IndexDocument;
import index.utils.Ranking;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBTextOnlyIndex extends AbstractTextIndex {

	public DBTextOnlyIndex(DBDataProvider dbDataProvider, ITextTokenizer tokenizer) {
		super(dbDataProvider, tokenizer);
	}

	@Override
	public void addDocument(String text) {
		dbDataProvider.addDocument(text); 
	}

	@Override
	public void addDocuments(List<String> texts) {
		dbDataProvider.addDocuments(texts); 
	}

	@Override
	public Ranking queryIndex(TextIndexQuery query) {
		return TextIndexUtils.performTextQuery(query, tokenizer, dbDataProvider, null);
	}

	
	
	@Override
	public TextIndexMetaData getTextMetaData() {
		return dbDataProvider.getTextMetaData();
	}
	
	

	

	@Override
	public void refill() {
		//Nothing to do... direct access to database...
	}

}
