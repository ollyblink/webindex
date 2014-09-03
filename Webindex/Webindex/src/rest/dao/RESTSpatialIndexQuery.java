package rest.dao;

import index.utils.GeometryWrapper;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RESTSpatialIndexQuery {
	private final String spatialRelationship;
	private final String location;
	private ArrayList<GeometryWrapper> queryFootPrints;

	public RESTSpatialIndexQuery(String spatialRelationship, String location) {
		this.spatialRelationship = spatialRelationship;
		this.location = location;
		this.queryFootPrints = new ArrayList<>();
	}

	public String getSpatialRelationship() {
		return spatialRelationship;
	}

	public String getLocation() {
		return location;
	}

	public ArrayList<GeometryWrapper> getQueryFootPrints() {
		return queryFootPrints;
	}

	public void setQueryFootPrints(ArrayList<GeometryWrapper> queryFootPrints) {
		this.queryFootPrints = queryFootPrints;
	}

	 
}
