package index.utils;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class only exists to transport coordinates through rest
 * 
 * @author rsp
 *
 */
@XmlRootElement
public class CoordinateWrapper {
	private double lon;
	private double lat;

	public CoordinateWrapper(double lon, double lat) {
		this.lon = lon;
		this.lat = lat;
	}

	public CoordinateWrapper() {
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lon);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		CoordinateWrapper other = (CoordinateWrapper) obj;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lon) != Double.doubleToLongBits(other.lon))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + lon + " | " + lat + ")";
	}

}
