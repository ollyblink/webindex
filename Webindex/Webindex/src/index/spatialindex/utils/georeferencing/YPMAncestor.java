package index.spatialindex.utils.georeferencing;

/**
 * Helper class for XML extraction from YPM.
 * @author Oliver Zihler
 * @version 0.1
 *
 */
public class YPMAncestor { 
	private int ancesterWoeid; 
	private String type; 
	private String name;
	
	public YPMAncestor(int ancesterWoeid, String type, String name) { 
		this.ancesterWoeid = ancesterWoeid;
		this.type = type;
		this.name = name;
	}

	public int getAncesterWoeid() {
		return ancesterWoeid;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setAncesterWoeid(int ancesterWoeid) {
		this.ancesterWoeid = ancesterWoeid;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
