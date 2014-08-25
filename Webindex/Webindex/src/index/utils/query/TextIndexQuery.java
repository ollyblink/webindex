package index.utils.query;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class TextIndexQuery  {
	private final String textQuery;
	private final String similarity;
	private final boolean isIntersected;
	
	public TextIndexQuery(String textQuery, String similarity, boolean isIntersected) {
		this.textQuery = textQuery;
		this.similarity = similarity;
		this.isIntersected = isIntersected;
	}
	
	public String getTextQuery() {
		return textQuery;
	}
	public String getSimilarity() {
		return similarity;
	}
	public boolean isIntersected() {
		return isIntersected;
	}
	
	
}
