package index.combinationstrategy;
 
import index.utils.Ranking;

public interface ICombinationStrategy {
 

	public Ranking combineScores(Ranking textRanking, Ranking spatialRanking);
 

}
