package index.girindex;

import utils.dbcrud.DBDataManager;
import index.girindex.combinationstrategy.ICombinationStrategy;

public abstract class AbstractGIRIndex implements IGIRIndex {

	protected DBDataManager dbDataProvider;
	protected ICombinationStrategy combinationStrategy;

	public AbstractGIRIndex(DBDataManager dbDataProvider, ICombinationStrategy combinationStrategy) {
		this.dbDataProvider = dbDataProvider;
		this.combinationStrategy = combinationStrategy;
	}

}
