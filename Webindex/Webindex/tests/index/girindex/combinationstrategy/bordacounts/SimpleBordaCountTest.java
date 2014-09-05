package index.girindex.combinationstrategy.bordacounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import index.utils.Document;
import index.utils.Score;
import index.utils.query.GIRQuery;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import rest.dao.RESTScore;
import rest.dao.RESTTextQueryMetaData;
import rest.dao.Ranking;

public class SimpleBordaCountTest {

	private static Ranking[] rankings;
	private static BordaCount comb;

	@BeforeClass
	public static void init() {
		ArrayList<RESTScore> scores = new ArrayList<RESTScore>();
		rankings = new Ranking[3];
		comb = new BordaCount();

		scores.add(new RESTScore(new Document(4l), 14.5f, null));
		scores.add(new RESTScore(new Document(3l), 12f, null));
		scores.add(new RESTScore(new Document(5l), 8.7f, null));
		scores.add(new RESTScore(new Document(1l), 0.5f, null));
		rankings[0] = new Ranking();
		RESTTextQueryMetaData restTextQueryMetaData = new RESTTextQueryMetaData();
		restTextQueryMetaData.setScores(scores);
		rankings[0].setTextQueryMetaData(restTextQueryMetaData);

		ArrayList<RESTScore> scores2 = new ArrayList<RESTScore>();
		scores2.add(new RESTScore(new Document(6l), 150f, null));
		scores2.add(new RESTScore(new Document(1l), 120f, null));
		scores2.add(new RESTScore(new Document(4l), 80f, null));
		scores2.add(new RESTScore(new Document(7l), -10f, null));
		scores2.add(new RESTScore(new Document(2l), -30f, null));
		rankings[1] = new Ranking();
		restTextQueryMetaData = new RESTTextQueryMetaData();
		restTextQueryMetaData.setScores(scores2);
		rankings[1].setTextQueryMetaData(restTextQueryMetaData);

		ArrayList<RESTScore> scores3 = new ArrayList<RESTScore>();
		scores3.add(new RESTScore(new Document(6l), 1f, null));
		scores3.add(new RESTScore(new Document(4l), 0.7f, null));
		scores3.add(new RESTScore(new Document(7l), 0.5f, null));
		scores3.add(new RESTScore(new Document(1l), 0.5f, null));
		scores3.add(new RESTScore(new Document(2l), 0.5f, null));

		rankings[2] = new Ranking();
		restTextQueryMetaData = new RESTTextQueryMetaData();
		restTextQueryMetaData.setScores(scores3);
		rankings[2].setTextQueryMetaData(restTextQueryMetaData);
	}

	@Test
	public void testCombination() {
		boolean isIntersected = false;
		GIRQuery query = new GIRQuery(isIntersected, null, null);
		Ranking combineScores = comb.combineScores(query, rankings);
		ArrayList<RESTScore> results = combineScores.getResults();
		assertTrue(results.size() == 7);
		assertEquals(12, results.get(0).getScore(), 0.1);
		assertEquals(4, results.get(0).getDocument().getId().getId().longValue());
		assertEquals(10, results.get(1).getScore(), 0.1);
		assertEquals(6, results.get(1).getDocument().getId().getId().longValue());
		assertEquals(8, results.get(2).getScore(), 0.1);
		assertEquals(1, results.get(2).getDocument().getId().getId().longValue());
		assertEquals(5, results.get(3).getScore(), 0.1);
		assertEquals(7, results.get(3).getDocument().getId().getId().longValue());
		assertEquals(4, results.get(4).getScore(), 0.1);
		assertEquals(3, results.get(4).getDocument().getId().getId().longValue());
		assertEquals(3, results.get(5).getScore(), 0.1);
		assertEquals(5, results.get(5).getDocument().getId().getId().longValue());
		assertEquals(2, results.get(6).getScore(), 0.1);
		assertEquals(2, results.get(6).getDocument().getId().getId().longValue());
		init();
		isIntersected = true;
		query = new GIRQuery(isIntersected, null, null);
		combineScores = comb.combineScores(query, rankings);
		results = combineScores.getResults();
		assertTrue(results.size() == 2);
		assertEquals(12f, results.get(0).getScore(), 0.1);
		assertEquals(4, results.get(0).getDocument().getId().getId().longValue());
		assertEquals(8, results.get(1).getScore(), 0.1);
		assertEquals(1, results.get(1).getDocument().getId().getId().longValue());
	}

}
