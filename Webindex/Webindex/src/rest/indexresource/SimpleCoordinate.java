package rest.indexresource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SimpleCoordinate {
	private long docid;
	private double x;
	private double y;

	public SimpleCoordinate() {
	}

	public SimpleCoordinate(long docid, double x, double y) {
		this.docid = docid;
		this.x = x;
		this.y = y;
	}

	public long getDocid() {
		return docid;
	}

	public void setDocid(long docid) {
		this.docid = docid;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
