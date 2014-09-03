package rest.dao;

import java.util.ArrayList;

/**
 * Only exists to transport a ranking through rest
 * @author rsp
 *
 */
public class RESTRanking {
	private ArrayList<RESTScore> results;
	private RESTRankingMetaData rankingMetaData;
	private String query;

	public RESTRanking() {
	}

	public ArrayList<RESTScore> getResults() {
		return results;
	}

	public void setResults(ArrayList<RESTScore> results) {
		this.results = results;
	}

	public RESTRankingMetaData getRankingMetaData() {
		return rankingMetaData;
	}

	public void setRankingMetaData(RESTRankingMetaData rankingMetaData) {
		this.rankingMetaData = rankingMetaData;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

 
}
