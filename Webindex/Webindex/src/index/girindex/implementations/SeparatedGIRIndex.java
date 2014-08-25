package index.girindex.implementations;

import java.util.List;

import index.combinationstrategy.ICombinationStrategy;
import index.girindex.IGIRIndex;
import index.spatialindex.ISpatialIndex;
import index.textindex.ITextIndex;
import index.utils.Ranking;
import index.utils.query.SpatialIndexQuery;
import index.utils.query.TextIndexQuery;

public class SeparatedGIRIndex implements IGIRIndex {
	private ITextIndex textIndex;
	private ISpatialIndex spatialIndex;
	private ICombinationStrategy combinationStrategy;
	
	public SeparatedGIRIndex(ITextIndex textIndex, ISpatialIndex spatialIndex, ICombinationStrategy combinationStrategy) {
		this.textIndex = textIndex;
		this.spatialIndex = spatialIndex;
		this.combinationStrategy = combinationStrategy;
	}

	@Override
	public Ranking queryIndex(TextIndexQuery textQuery, SpatialIndexQuery spatialQuery) {
		Ranking textRanking = textIndex.queryIndex(textQuery);
		Ranking spatialRanking = spatialIndex.queryIndex(spatialQuery); 
		Ranking combination = combinationStrategy.combineScores(textRanking, spatialRanking);
		return null;
	}

	@Override
	public void addDocument(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDocuments(List<String> texts) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
