package index.girindex.combinationstrategy.combfamily;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import index.utils.Document;
import index.utils.Ranking;
import index.utils.Score;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

public class CombMNZTest {

	private static Ranking[] rankings;
	private static CombMNZ comb;
	@BeforeClass
	public static void init(){
		ArrayList<Score> scores = new ArrayList<Score>();
		rankings = new Ranking[3];
		comb = new CombMNZ();
		
		scores.add(new Score(new Document(4l), 14.5f,null));
		scores.add(new Score(new Document(3l), 12f,null));
		scores.add(new Score(new Document(5l), 8.7f,null));
		scores.add(new Score(new Document(1l), 0.5f,null));
		rankings[0] = new Ranking(scores, null);
		

		ArrayList<Score> scores2 = new ArrayList<Score>();
		scores2.add(new Score(new Document(6l), 150f,null));
		scores2.add(new Score(new Document(1l), 120f,null));
		scores2.add(new Score(new Document(4l), 80f,null));
		scores2.add(new Score(new Document(7l), -10f,null));
		scores2.add(new Score(new Document(2l), -30f,null));
		rankings[1] = new Ranking(scores2,null); 
		

		ArrayList<Score> scores3 = new ArrayList<Score>();
		scores3.add(new Score(new Document(6l), 1f,null));
		scores3.add(new Score(new Document(4l), 0.7f,null));
		scores3.add(new Score(new Document(7l), 0.5f,null));
		scores3.add(new Score(new Document(1l), 0.5f,null));
		scores3.add(new Score(new Document(2l), 0.5f,null));
		rankings[2] = new Ranking(scores3,null);  
	}
	
	@Test
	public void testCombination() { 
		boolean isIntersected = false;
		 
		Ranking combineScores = comb.combineScores(isIntersected, rankings) ;
		ArrayList<Score> results = combineScores.getResults();
		assertTrue(results.size() == 7);
		assertEquals(results.get(0).getScore(), 6.03f,0.01); 
		assertEquals(results.get(0).getDocument().getId().getId().longValue(), 4);
		assertEquals(results.get(1).getScore(), 4.00f,0.01); 
		assertEquals(results.get(1).getDocument().getId().getId().longValue(), 6);
		assertEquals(results.get(2).getScore(), 2.50f,0.01); 
		assertEquals(results.get(2).getDocument().getId().getId().longValue(), 1);
		assertEquals(results.get(3).getScore(), 0.82f,0.01); 
		assertEquals(results.get(3).getDocument().getId().getId().longValue(), 3);
		assertEquals(results.get(4).getScore(), 0.59f,0.01); 
		assertEquals(results.get(4).getDocument().getId().getId().longValue(), 5);
		assertEquals(results.get(5).getScore(), 0.22f,0.01); 
		assertEquals(results.get(5).getDocument().getId().getId().longValue(), 7);
		assertEquals(results.get(6).getScore(), 0.00f,0.01); 
		assertEquals(results.get(6).getDocument().getId().getId().longValue(), 2);
		
		isIntersected = true;
		combineScores = comb.combineScores(isIntersected, rankings) ;
		results = combineScores.getResults();
		assertTrue(results.size() == 2);
		assertEquals(results.get(0).getScore(), 6.03f,0.01);  
		assertEquals(results.get(0).getDocument().getId().getId().longValue(), 4);
		assertEquals(results.get(1).getScore(), 2.50f,0.01); 
		assertEquals(results.get(1).getDocument().getId().getId().longValue(), 1);
	}

}
