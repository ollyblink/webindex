package index.spatialindex.utils;

import index.utils.identifers.LocationIdentifier;

import com.vividsolutions.jts.geom.Geometry;

public class Location {

	private LocationIdentifier docid;
	private Geometry geometry;

	public Location(Long docid, Geometry geometry) {
		this.docid = new LocationIdentifier(docid);
		this.geometry = geometry;
	}

	public LocationIdentifier getDocid() {
		return docid;
	}

	public void setDocid(Long docid) {
		this.docid = new LocationIdentifier(docid);
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docid == null) ? 0 : docid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (docid == null) {
			if (other.docid != null)
				return false;
		} else if (!docid.equals(other.docid))
			return false;
		return true;
	}

}
