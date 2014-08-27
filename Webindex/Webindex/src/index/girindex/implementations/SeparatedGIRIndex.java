package index.girindex.implementations;

import index.girindex.AbstractGIRIndex;
import index.girindex.combinationstrategy.ICombinationStrategy;
import index.spatialindex.implementations.ISpatialIndex;
import index.spatialindex.utils.SpatialDocument;
import index.spatialindex.utils.SpatialIndexMetaData;
import index.textindex.implementations.ITextIndex;
import index.textindex.utils.TextIndexMetaData;
import index.textindex.utils.informationextractiontools.ITextInformationExtractor;
import index.utils.Ranking;
import index.utils.query.SpatialIndexQuery;
import index.utils.query.TextIndexQuery;

import java.util.List;

import utils.dbcrud.DBDataManager;

public class SeparatedGIRIndex extends AbstractGIRIndex {
	private ITextIndex textIndex;
	private ISpatialIndex spatialIndex;
	 

	public SeparatedGIRIndex(DBDataManager dbDataProvider, ITextIndex textIndex, ISpatialIndex spatialIndex, ICombinationStrategy combinationStrategy) {
		super(dbDataProvider, combinationStrategy); 
		this.textIndex = textIndex;
		this.spatialIndex = spatialIndex;  
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
	public Ranking queryIndex(boolean isIntersected, TextIndexQuery textQuery, SpatialIndexQuery spatialQuery) {
		Ranking textRanking = textIndex.queryIndex(textQuery);
		Ranking spatialRanking = spatialIndex.queryIndex(spatialQuery);
		Ranking combination = combinationStrategy.combineScores(isIntersected, textRanking, spatialRanking);
		return combination;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ITextInformationExtractor getTokenizer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextIndexMetaData getTextMetaData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ranking queryIndex(TextIndexQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addDocumentFootprint(SpatialDocument... documentFootPrints) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SpatialIndexMetaData getSpatialMetaData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ranking queryIndex(SpatialIndexQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	 
}
