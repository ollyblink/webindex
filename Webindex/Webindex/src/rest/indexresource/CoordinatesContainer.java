package rest.indexresource;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CoordinatesContainer {
	private ArrayList<SimpleCoordinate> docCoords;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;

	public CoordinatesContainer() {
		this.docCoords = new ArrayList<>();
		this.minX = Double.MAX_VALUE;
		this.minY = Double.MAX_VALUE;
		this.maxX = Double.MIN_VALUE;
		this.maxY = Double.MIN_VALUE;
	}

	public ArrayList<SimpleCoordinate> getDocCoords() {
		return docCoords;
	}

	public void setDocCoords(ArrayList<SimpleCoordinate> docCoords) {
		this.docCoords = docCoords;
	}

	public void addCoordinate(SimpleCoordinate simpleCoordinate) {
		this.docCoords.add(simpleCoordinate);
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

}
