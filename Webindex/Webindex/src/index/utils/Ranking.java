package index.utils;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Ranking implements Iterable<Score> {

	private ArrayList<Score> results;

	public Ranking(ArrayList<? extends Score> results) {
		this.results = new ArrayList<Score>();
		this.results.addAll(results);

	}

	public Ranking() {

	}

	public ArrayList<Score> getResults() {
		return results;
	}

	public void setResults(ArrayList<Score> results) {
		this.results = results;
	}

	@Override
	public Iterator<Score> iterator() {
		return results.iterator();
	}

}
