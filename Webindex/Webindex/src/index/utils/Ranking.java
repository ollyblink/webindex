package index.utils;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Ranking implements Iterable<IndexDocument>  {

	private String textQuery;
	private ArrayList<IndexDocument> results; 

	public Ranking(String textQuery, ArrayList<IndexDocument> results) {
		this.textQuery = textQuery;
		this.results = results; 
		//The following creates the <span></span> html tags around the original terms in the fulltext for displaying.
		for(IndexDocument doc: results){
			doc.addHiliteSpans();
		}
	}

	public Ranking(){
		
	}
	 
	public String getQuery() {
		return textQuery;
	}

	public void setQuery(String query) {
		this.textQuery = query;
	}
 
	public ArrayList<IndexDocument> getResults() {
		return results;
	}

	public void setResults(ArrayList<IndexDocument> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "Ranking [query=" + textQuery + ", results=" + results + "]";
	}

	@Override
	public Iterator<IndexDocument> iterator() {
		return results.iterator();
	}
	

}
