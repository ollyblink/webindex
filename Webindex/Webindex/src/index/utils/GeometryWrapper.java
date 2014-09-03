package index.utils;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * this class only exists to send the geometry through REST
 * 
 * @author rsp
 *
 */
@XmlRootElement
public class GeometryWrapper {
	private ArrayList<CoordinateWrapper> coordinates;
	private int srid;

	public GeometryWrapper() {
	}

	public GeometryWrapper(ArrayList<CoordinateWrapper> coordinates, int srid) {
		this.coordinates = coordinates;
		this.srid = srid;
	}

	public int getSrid() {
		return srid;
	}

	public void setSrid(int srid) {
		this.srid = srid;
	}

	public ArrayList<CoordinateWrapper> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(ArrayList<CoordinateWrapper> coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
		result = prime * result + srid;
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
		GeometryWrapper other = (GeometryWrapper) obj;
		if (coordinates == null) {
			if (other.coordinates != null)
				return false;
		} else if (!coordinates.equals(other.coordinates))
			return false;
		if (srid != other.srid)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String toString = srid + "[";
		for(CoordinateWrapper c: coordinates){
			toString += c +", ";
		} 
		return toString;
	}

	
}
