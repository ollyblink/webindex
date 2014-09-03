package rest.dao;

import index.utils.query.TextIndexQuery;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class only exists to transport ranking metadata through rest
 * 
 * @author rsp
 *
 */
@XmlRootElement
public class RESTRankingMetaData {
	private String combinationStrategy;
	private ArrayList<RESTRanking> rankings;
	private TextIndexQuery textIndexQuery;
	private RESTSpatialIndexQuery spatialIndexQuery;
	private RESTGIRQuery girQuery;
	public RESTRankingMetaData() {
	}

	public RESTRankingMetaData(String combinationStrategy, ArrayList<RESTRanking> rankings) {
		this.combinationStrategy = combinationStrategy;
		this.rankings = rankings;
	}

	public String getCombinationStrategy() {
		return combinationStrategy;
	}

	public void setCombinationStrategy(String combinationStrategy) {
		this.combinationStrategy = combinationStrategy;
	}

	public ArrayList<RESTRanking> getRankings() {
		return rankings;
	}

	public void setRankings(ArrayList<RESTRanking> rankings) {
		this.rankings = rankings;
	}

	public TextIndexQuery getTextIndexQuery() {
		return textIndexQuery;
	}

	public void setTextIndexQuery(TextIndexQuery textIndexQuery) {
		this.textIndexQuery = textIndexQuery;
	}

	public RESTSpatialIndexQuery getSpatialIndexQuery() {
		return spatialIndexQuery;
	}

	public void setSpatialIndexQuery(RESTSpatialIndexQuery spatialIndexQuery) {
		this.spatialIndexQuery = spatialIndexQuery;
	}

	public RESTGIRQuery getGirQuery() {
		return girQuery;
	}

	public void setGirQuery(RESTGIRQuery girQuery) {
		this.girQuery = girQuery;
	}

}
