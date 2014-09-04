package index.girindex.combinationstrategy;
 
import index.utils.query.GIRQuery;
import rest.dao.Ranking;

public interface ICombinationStrategy {
 

	/** 
	 * Combines the rankings provided by rankings int one single ranking.
	 * Duplicated IndexDocuments are removed.
	 * @param rankings
	 * @return
	 */
	public Ranking combineScores(GIRQuery query, Ranking... rankings);
 

}
