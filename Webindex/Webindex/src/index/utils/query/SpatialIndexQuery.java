package index.utils.query;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.vividsolutions.jts.geom.Geometry;

@XmlRootElement
public final class SpatialIndexQuery {
	private final String spatialRelationship;
	private final String location;
	private ArrayList<Geometry> queryFootPrints;

	public SpatialIndexQuery(String spatialRelationship, String location) {
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

	public void setQueryFootPrints(ArrayList<Geometry> queryFootPrints) {
		this.queryFootPrints = queryFootPrints;
	}

	public ArrayList<? extends Geometry> getQueryFootPrints() {
		return queryFootPrints;
	}

}
