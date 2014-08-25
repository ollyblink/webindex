package index.utils;

import index.utils.query.SpatialIndexQuery;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Ranking implements Iterable<IndexDocument> {

	private TextIndexQuery textQuery;
	private SpatialIndexQuery spatialQuery;
	private ArrayList<IndexDocument> results;

	public Ranking(ArrayList<IndexDocument> results) {
		this.results = results;
		// The following creates the <span></span> html tags around the original terms in the fulltext for displaying.
		for (IndexDocument doc : results) {
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

	public ArrayList<IndexDocument> getResults() {
		return results;
	}

	public void setResults(ArrayList<IndexDocument> results) {
		this.results = results;
	}

	@Override
	public Iterator<IndexDocument> iterator() {
		return results.iterator();
	}

}
