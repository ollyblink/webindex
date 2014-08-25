package index.girindex;

import index.girindex.combinationstrategy.ICombinationStrategy;
import index.utils.DBDataProvider;

public abstract class AbstractGIRIndex implements IGIRIndex {

	protected DBDataProvider dbDataProvider;
	protected ICombinationStrategy combinationStrategy;

	public AbstractGIRIndex(DBDataProvider dbDataProvider, ICombinationStrategy combinationStrategy) {
		this.dbDataProvider = dbDataProvider;
		this.combinationStrategy = combinationStrategy;
	}

}
