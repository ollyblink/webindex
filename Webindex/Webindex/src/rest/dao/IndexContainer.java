package rest.dao;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IndexContainer {

	ArrayList<String> displayableIndex = new ArrayList<String>();
	int N;
	
	public IndexContainer() {
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}
 

	public IndexContainer(ArrayList<String> displayableIndex, int n) {
		this.displayableIndex = displayableIndex;
		N = n;
	}

	public ArrayList<String> getDisplayableIndex() {
		return displayableIndex;
	}

	public void setDisplayableIndex(ArrayList<String> displayableIndex) {
		this.displayableIndex = displayableIndex;
	}

	
}
