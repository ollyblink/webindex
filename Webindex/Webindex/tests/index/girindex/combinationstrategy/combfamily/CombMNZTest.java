package index.girindex.combinationstrategy.combfamily;

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

public class CombMNZTest {

	private static Ranking[] rankings;
	private static CombMNZ comb;

	@BeforeClass
	public static void init() {
		ArrayList<RESTScore> scores = new ArrayList<RESTScore>();
		rankings = new Ranking[3];
		comb = new CombMNZ();

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
		assertEquals(results.get(0).getScore(), 6.03f, 0.01);
		assertEquals(results.get(0).getDocument().getId().getId().longValue(), 4);
		assertEquals(results.get(1).getScore(), 4.00f, 0.01);
		assertEquals(results.get(1).getDocument().getId().getId().longValue(), 6);
		assertEquals(results.get(2).getScore(), 2.50f, 0.01);
		assertEquals(results.get(2).getDocument().getId().getId().longValue(), 1);
		assertEquals(results.get(3).getScore(), 0.82f, 0.01);
		assertEquals(results.get(3).getDocument().getId().getId().longValue(), 3);
		assertEquals(results.get(4).getScore(), 0.59f, 0.01);
		assertEquals(results.get(4).getDocument().getId().getId().longValue(), 5);
		assertEquals(results.get(5).getScore(), 0.22f, 0.01);
		assertEquals(results.get(5).getDocument().getId().getId().longValue(), 7);
		assertEquals(results.get(6).getScore(), 0.00f, 0.01);
		assertEquals(results.get(6).getDocument().getId().getId().longValue(), 2);
		init();
		isIntersected = true;
		query = new GIRQuery(isIntersected, null, null);
		combineScores = comb.combineScores(query, rankings);
		results = combineScores.getResults();
		assertTrue(results.size() == 2);
		assertEquals(results.get(0).getScore(), 6.03f, 0.01);
		assertEquals(results.get(0).getDocument().getId().getId().longValue(), 4);
		assertEquals(results.get(1).getScore(), 2.50f, 0.01);
		assertEquals(results.get(1).getDocument().getId().getId().longValue(), 1);
	}

}
