package index.girindex.implementations;

import index.girindex.AbstractGIRIndex;
import index.girindex.combinationstrategy.ICombinationStrategy;
import index.spatialindex.ISpatialIndex;
import index.textindex.ITextIndex;
import index.utils.DBDataProvider;
import index.utils.Ranking;
import index.utils.indexchangelistener.IIndexChangeListener;
import index.utils.indexchangelistener.IIndexChangeSubject;
import index.utils.query.SpatialIndexQuery;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.List;

public class SeparatedGIRIndex extends AbstractGIRIndex implements  IIndexChangeSubject {
	private ITextIndex textIndex;
	private ISpatialIndex spatialIndex;
	
	private List<IIndexChangeListener> changeListeners;

	public SeparatedGIRIndex(DBDataProvider dbDataProvider, ITextIndex textIndex, ISpatialIndex spatialIndex, ICombinationStrategy combinationStrategy) {
		super(dbDataProvider, combinationStrategy); 
		this.textIndex = textIndex;
		this.spatialIndex = spatialIndex; 
		this.changeListeners = new ArrayList<IIndexChangeListener>();
		this.changeListeners.add(textIndex);
		this.changeListeners.add(spatialIndex);
	}

	@Override
	public void addDocument(String text) {
		dbDataProvider.addDocument(text);  
		updateIndexChangeListener();
	}

	@Override
	public void addDocuments(List<String> texts) {
		dbDataProvider.addDocuments(texts); 
		updateIndexChangeListener();
	}

	@Override
	public Ranking queryIndex(boolean isIntersected, TextIndexQuery textQuery, SpatialIndexQuery spatialQuery) {
		Ranking textRanking = textIndex.queryIndex(textQuery);
		Ranking spatialRanking = spatialIndex.queryIndex(spatialQuery);
		Ranking combination = combinationStrategy.combineScores(isIntersected, textRanking, spatialRanking);
		return combination;
	}

	@Override
	public void addIndexChangeListener(IIndexChangeListener listener) {
		this.changeListeners.add(listener);
	}

	@Override
	public void updateIndexChangeListener() {
		for(IIndexChangeListener cL: changeListeners){
			cL.refill();
		}
	}
}
