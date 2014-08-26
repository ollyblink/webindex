package index.girindex.combinationstrategy.combfamily;

import static org.junit.Assert.*;
import index.utils.Ranking;
import index.utils.Score;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

public class CombMNZTest {

	private static Ranking[] rankings;
	private static CombMNZ mnz;
	@BeforeClass
	public static void init(){
		ArrayList<Score> scores = new ArrayList<Score>();
		rankings = new Ranking[3];
		mnz = new CombMNZ();
		
		scores.add(new Score(4l, 14.5f));
		scores.add(new Score(3l, 12f));
		scores.add(new Score(5l, 8.7f));
		scores.add(new Score(1l, 0.5f));
		rankings[0] = new Ranking(scores);
		

		ArrayList<Score> scores2 = new ArrayList<Score>();
		scores2.add(new Score(6l, 150f));
		scores2.add(new Score(1l, 120f));
		scores2.add(new Score(4l, 80f));
		scores2.add(new Score(7l, -10f));
		scores2.add(new Score(2l, -30f));
		rankings[1] = new Ranking(scores2); 
		

		ArrayList<Score> scores3 = new ArrayList<Score>();
		scores3.add(new Score(6l, 1f));
		scores3.add(new Score(4l, 0.7f));
		scores3.add(new Score(7l, 0.5f));
		scores3.add(new Score(1l, 0.5f));
		scores3.add(new Score(2l, 0.5f));
		rankings[2] = new Ranking(scores3);  
	}
	
	@Test
	public void testCombination() { 
		boolean isIntersected = false;
		 
		Ranking combineScores = mnz.combineScores(isIntersected, rankings) ;
		ArrayList<Score> results = combineScores.getResults();
		assertTrue(results.size() == 7);
		assertEquals(results.get(0).getScore(), 6.03f,0.01); 
		assertEquals(results.get(0).getDocid().longValue(), 4);
		assertEquals(results.get(1).getScore(), 4.00f,0.01); 
		assertEquals(results.get(1).getDocid().longValue(), 6);
		assertEquals(results.get(2).getScore(), 2.50f,0.01); 
		assertEquals(results.get(2).getDocid().longValue(), 1);
		assertEquals(results.get(3).getScore(), 0.82f,0.01); 
		assertEquals(results.get(3).getDocid().longValue(), 3);
		assertEquals(results.get(4).getScore(), 0.59f,0.01); 
		assertEquals(results.get(4).getDocid().longValue(), 5);
		assertEquals(results.get(5).getScore(), 0.22f,0.01); 
		assertEquals(results.get(5).getDocid().longValue(), 7);
		assertEquals(results.get(6).getScore(), 0.00f,0.01); 
		assertEquals(results.get(6).getDocid().longValue(), 2);
		
		isIntersected = true;
		combineScores = mnz.combineScores(isIntersected, rankings) ;
		results = combineScores.getResults();
		assertTrue(results.size() == 2);
		assertEquals(results.get(0).getScore(), 6.03f,0.01);  
		assertEquals(results.get(0).getDocid().longValue(), 4);
		assertEquals(results.get(1).getScore(), 2.50f,0.01); 
		assertEquals(results.get(1).getDocid().longValue(), 1);
	}

}
