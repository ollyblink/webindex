package rest.dao;

import index.textindex.utils.Term;
import index.textindex.utils.TermDocs;
import index.utils.indexmetadata.OverallTextIndexMetaData;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RESTTextQueryMetaData {
	private String textQuery;
	private String textSimilarity;
	private boolean isTextIntersected;
	private ArrayList<RESTScore> scores;
	private HashMap<Term, Integer> queryTermFrequencies;
	private ArrayList<TermDocs> termDocs;
	private OverallTextIndexMetaData overallTextIndexMetaData;
	private String printableQuery;

	public RESTTextQueryMetaData() {
	}

	public RESTTextQueryMetaData(String textQuery, String textSimilarity, boolean isTextIntersected, ArrayList<RESTScore> originalTermScores, HashMap<Term, Integer> queryTermFrequencies, ArrayList<TermDocs> termDocs, OverallTextIndexMetaData overallTextIndexMetaData) {
		this.textQuery = textQuery;
		this.textSimilarity = textSimilarity;
		this.isTextIntersected = isTextIntersected;
		this.scores = originalTermScores;
		this.queryTermFrequencies = queryTermFrequencies;
		this.termDocs = termDocs;
		this.overallTextIndexMetaData = overallTextIndexMetaData;
	}

	public String getTextQuery() {
		return textQuery;
	}

	public void setTextQuery(String textQuery) {
		this.textQuery = textQuery;
	}

	public String getTextSimilarity() {
		return textSimilarity;
	}

	public void setTextSimilarity(String textSimilarity) {
		this.textSimilarity = textSimilarity;
	}

	public boolean isTextIntersected() {
		return isTextIntersected;
	}

	public void setTextIntersected(boolean isTextIntersected) {
		this.isTextIntersected = isTextIntersected;
	}

	public ArrayList<RESTScore> getScores() {
		return scores;
	}

	public void setScores(ArrayList<RESTScore> scores) {
		this.scores = scores;
	}

	public HashMap<Term, Integer> getQueryTermFrequencies() {
		return queryTermFrequencies;
	}

	public void setQueryTermFrequencies(HashMap<Term, Integer> queryTermFrequencies) {
		this.queryTermFrequencies = queryTermFrequencies;
	}

	public ArrayList<TermDocs> getTermDocs() {
		return termDocs;
	}

	public void setTermDocs(ArrayList<TermDocs> termDocs) {
		this.termDocs = termDocs;
	}

	public OverallTextIndexMetaData getOverallTextIndexMetaData() {
		return overallTextIndexMetaData;
	}

	public void setOverallTextIndexMetaData(OverallTextIndexMetaData overallTextIndexMetaData) {
		this.overallTextIndexMetaData = overallTextIndexMetaData;
	}

	public String getPrintableQuery() {
		return printableQuery;
	}

	public void setPrintableQuery(String printableQuery) {
		this.printableQuery = printableQuery;
	}

}
