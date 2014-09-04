package rest.dao;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RESTGIRQueryMetaData {
	private boolean isIntersected;
	private String combinationStrategy;
	private ArrayList<RESTScore> scores;
	private String printableQuery;
 
	public RESTGIRQueryMetaData(boolean isIntersected, String combinationStrategy, ArrayList<RESTScore> finalScores) {
		this.isIntersected = isIntersected;
		this.combinationStrategy = combinationStrategy;
		this.scores = finalScores;
	}

	 


	public ArrayList<RESTScore> getScores() {
		return scores;
	}




	public void setScores(ArrayList<RESTScore> scores) {
		this.scores = scores;
	}




	public RESTGIRQueryMetaData() {
	}

	public boolean isIntersected() {
		return isIntersected;
	}

	public void setIntersected(boolean isIntersected) {
		this.isIntersected = isIntersected;
	}

	public String getCombinationStrategy() {
		return combinationStrategy;
	}

	public void setCombinationStrategy(String combinationStrategy) {
		this.combinationStrategy = combinationStrategy;
	}
	public String getPrintableQuery() {
		return printableQuery;
	}

	public void setPrintableQuery(String printableQuery) {
		this.printableQuery = printableQuery;
	}

}
