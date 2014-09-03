package index.utils;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Ranking implements Iterable<Score> {

	private ArrayList<Score> results;
	private RankingMetaData rankingMetaData;

	public Ranking() {
	}

	public Ranking(ArrayList<Score> results, RankingMetaData rankingMetaData) {
		this.results = results;
		this.rankingMetaData = rankingMetaData;
	}

	public ArrayList<Score> getResults() {
		return results;
	}

	public void setResults(ArrayList<Score> results) {
		this.results = results;
	}

	public RankingMetaData getRankingMetaData() {
		return rankingMetaData;
	}

	public void setRankingMetaData(RankingMetaData rankingMetaData) {
		this.rankingMetaData = rankingMetaData;
	}

	@Override
	public String toString() {
		String ranks = "";
		for (Score s : results) {
			ranks += s + " ";
		}
		return ranks;
	}

	@Override
	public Iterator<Score> iterator() {
		return results.iterator();
	}

	 

}
