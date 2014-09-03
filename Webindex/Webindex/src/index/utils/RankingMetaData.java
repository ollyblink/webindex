package index.utils;

import index.utils.query.GIRQuery;
import index.utils.query.SpatialIndexQuery;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
 
public class RankingMetaData {

	private String combinationStrategy;
	private ArrayList<Ranking> rankings;
	private TextIndexQuery textIndexQuery;
	private SpatialIndexQuery spatialIndexQuery;
	private GIRQuery girQuery;

	public RankingMetaData() {
	}

	public RankingMetaData(String combinationStrategy, ArrayList<Ranking> rankings) {
		this.combinationStrategy = combinationStrategy;
		this.rankings = rankings;
	}

	public String getCombinationStrategy() {
		return combinationStrategy;
	}

	public void setCombinationStrategy(String combinationStrategy) {
		this.combinationStrategy = combinationStrategy;
	}

	public ArrayList<Ranking> getRankings() {
		return rankings;
	}

	public void setRankings(ArrayList<Ranking> rankings) {
		this.rankings = rankings;
	}

	/**
	 * This method can be used to see if the retrieved ranking has been composed of multiple rankings
	 * 
	 * @return
	 */
	public boolean isCombined() {
		return rankings.size() > 1;
	}

	@Override
	public String toString() {
		return combinationStrategy + " >> [" + rankings + "]";
	}

	public void setTextIndexQuery(TextIndexQuery query) {
		this.textIndexQuery = query;
	}

	public TextIndexQuery getTextIndexQuery() {
		return textIndexQuery;
	}

	public void setSpatialIndexQuery(SpatialIndexQuery query) {
		this.spatialIndexQuery = query;
	}

	public SpatialIndexQuery getSpatialIndexQuery() {
		return spatialIndexQuery;
	}

	public void setGIRQuery(GIRQuery query) {
		this.girQuery = query;
	}

	public GIRQuery getGirQuery() {
		return girQuery;
	}

	public void setGirQuery(GIRQuery girQuery) {
		this.girQuery = girQuery;
	}
	
	
}
