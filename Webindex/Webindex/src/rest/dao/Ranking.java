package rest.dao;

import java.util.ArrayList;

/**
 * Only exists to transport a ranking through rest
 * 
 * @author rsp
 *
 */
public class Ranking {
	private RESTGIRQueryMetaData girQueryMetaData;
	private RESTTextQueryMetaData textQueryMetaData;
	private RESTSpatialQueryMetaData spatialQueryMetaData;

	public Ranking() {
	}

	public Ranking(RESTGIRQueryMetaData girQueryMetaData, RESTTextQueryMetaData textQueryMetaData, RESTSpatialQueryMetaData spatialQueryMetaData) {
		this.girQueryMetaData = girQueryMetaData;
		this.textQueryMetaData = textQueryMetaData;
		this.spatialQueryMetaData = spatialQueryMetaData;
	}

	public RESTGIRQueryMetaData getGirQueryMetaData() {
		return girQueryMetaData;
	}

	public void setGirQueryMetaData(RESTGIRQueryMetaData girQueryMetaData) {
		this.girQueryMetaData = girQueryMetaData;
	}

	public RESTTextQueryMetaData getTextQueryMetaData() {
		return textQueryMetaData;
	}

	public void setTextQueryMetaData(RESTTextQueryMetaData textQueryMetaData) {
		this.textQueryMetaData = textQueryMetaData;
	}

	public RESTSpatialQueryMetaData getSpatialQueryMetaData() {
		return spatialQueryMetaData;
	}

	public void setSpatialQueryMetaData(RESTSpatialQueryMetaData spatialQueryMetaData) {
		this.spatialQueryMetaData = spatialQueryMetaData;
	}

	public ArrayList<RESTScore> getResults() {
		if (getGirQueryMetaData() != null) {
			return getGirQueryMetaData().getScores();
		} else {
			if (getTextQueryMetaData() != null) {
				return getTextQueryMetaData().getScores();
			} else if (getSpatialQueryMetaData() != null) {
				return getSpatialQueryMetaData().getScores();
			} else {
				return new ArrayList<>();
			}
		}
	}

	public void setResults(ArrayList<RESTScore> scores) {
		if (getGirQueryMetaData() != null) {
			getGirQueryMetaData().setScores(scores);
		} else {
			if (getTextQueryMetaData() != null) {
				getTextQueryMetaData().setScores(scores);
			} else if (getSpatialQueryMetaData() != null) {
				getSpatialQueryMetaData().setScores(scores);
			}
		}
	}

	public Object getRankingType() {
		if (getGirQueryMetaData() != null) {
			return getGirQueryMetaData();
		} else {
			if (getTextQueryMetaData() != null) {
				return getTextQueryMetaData();
			} else if (getSpatialQueryMetaData() != null) {
				return getSpatialQueryMetaData();
			} else {
				return new ArrayList<>();
			}
		}
	}

}
