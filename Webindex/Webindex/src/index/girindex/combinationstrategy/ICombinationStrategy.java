package index.girindex.combinationstrategy;
 
import index.utils.Ranking;

public interface ICombinationStrategy {
 

	/** 
	 * Combines the rankings provided by rankings int one single ranking.
	 * Duplicated IndexDocuments are removed.
	 * @param rankings
	 * @return
	 */
	public Ranking combineScores(Ranking... rankings);
 

}
