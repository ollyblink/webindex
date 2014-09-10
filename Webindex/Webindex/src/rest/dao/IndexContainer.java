package rest.dao;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IndexContainer {

	ArrayList<String> displayableIndex = new ArrayList<String>();
	
	
	public IndexContainer() {
	}

	public IndexContainer(ArrayList<String> displayableIndex) {
		this.displayableIndex = displayableIndex;
	}

	public ArrayList<String> getDisplayableIndex() {
		return displayableIndex;
	}

	public void setDisplayableIndex(ArrayList<String> displayableIndex) {
		this.displayableIndex = displayableIndex;
	}

	
}
