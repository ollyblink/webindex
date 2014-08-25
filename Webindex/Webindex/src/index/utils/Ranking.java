package index.utils;

import index.girindex.utils.ScoreTuple;
import index.utils.query.SpatialIndexQuery;
import index.utils.query.TextIndexQuery;

import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Ranking implements Iterable<IndexDocument> {

	private TextIndexQuery textQuery;
	private SpatialIndexQuery spatialQuery;
	private Map<IndexDocument, ScoreTuple> results;  

	public Ranking(Map<IndexDocument, ScoreTuple> results) {
		this.results = results;
		// The following creates the <span></span> html tags around the original terms in the fulltext for displaying.
		for (IndexDocument doc : results.keySet()) {
			doc.addHiliteSpans();
		} 
 
	}

	public Ranking() {

	}
	 
	public TextIndexQuery getTextQuery() {
		return textQuery;
	}

	public void setTextQuery(TextIndexQuery textQuery) {
		this.textQuery = textQuery;
	}

	public SpatialIndexQuery getSpatialQuery() {
		return spatialQuery;
	}

	public void setSpatialQuery(SpatialIndexQuery spatialQuery) {
		this.spatialQuery = spatialQuery;
	}

	@Override
	public Iterator<IndexDocument> iterator() {
		return results.keySet().iterator();
	}

	public Map<IndexDocument, ScoreTuple> getResults() {
		return results;
	}

	public void setResults(Map<IndexDocument, ScoreTuple> results) {
		this.results = results;
	}

 
}
