package rest.dao;

import index.utils.GeometryWrapper;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RESTSpatialQueryMetaData {
	private String spatialRelationship;
	private String location;
	private ArrayList<GeometryWrapper> queryFootPrints;
	private ArrayList<RESTScore> scores;
	private String printableQuery;

	public RESTSpatialQueryMetaData() {
	}

	public RESTSpatialQueryMetaData(String spatialRelationship, String location, ArrayList<GeometryWrapper> queryFootPrints, ArrayList<RESTScore> originalSpatialScores) {
		this.spatialRelationship = spatialRelationship;
		this.location = location;
		this.queryFootPrints = queryFootPrints;
		this.scores = originalSpatialScores;
	}

	public String getSpatialRelationship() {
		return spatialRelationship;
	}

	public void setSpatialRelationship(String spatialRelationship) {
		this.spatialRelationship = spatialRelationship;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public ArrayList<GeometryWrapper> getQueryFootPrints() {
		return queryFootPrints;
	}

	public void setQueryFootPrints(ArrayList<GeometryWrapper> queryFootPrints) {
		this.queryFootPrints = queryFootPrints;
	}

	public ArrayList<RESTScore> getScores() {
		return scores;
	}

	public void setScores(ArrayList<RESTScore> scores) {
		this.scores = scores;
	}

	public String getPrintableQuery() {
		return printableQuery;
	}

	public void setPrintableQuery(String printableQuery) {
		this.printableQuery = printableQuery;
	}

}
