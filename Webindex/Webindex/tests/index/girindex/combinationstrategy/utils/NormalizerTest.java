package index.girindex.combinationstrategy.utils;

import static org.junit.Assert.assertTrue;
import index.girindex.combinationstrategy.utils.Normalizer;
import index.utils.Score;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class NormalizerTest {

	private static ArrayList<Score> values;
	private static float minMax;
	private static float min;
	private static int N;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		values = new ArrayList<Score>();

		N = 100;
		for(int i = 0; i < N;++i){
			values.add(new Score(new Long(i), new Float(Math.pow(2, i))));
		}

		minMax = (float)((Math.pow(2, N-1))-1)-min;
		min = 1f;
	}

	@Test
	public void test() {
		List<Score> normalizedValues = Normalizer.normalizeMinMax(values);
		assertTrue(normalizedValues != null);
		assertTrue(normalizedValues != values);
		assertTrue(normalizedValues.size() == values.size()); 
		for (int i = 0; i < normalizedValues.size(); ++i) {  
			assertTrue(normalizedValues.get(i).getScore() == ((values.get(i).getScore() - min) / minMax));
		}
	}

}