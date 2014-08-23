package index.combinationstrategy;

import index.utils.ISimilarityProvider;

public interface ICombinationStrategy {

	public Float combineScores(Float... similarities);
 

}
