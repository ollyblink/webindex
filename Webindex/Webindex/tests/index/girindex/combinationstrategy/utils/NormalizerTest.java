package index.girindex.combinationstrategy.utils;

import static org.junit.Assert.*;
import index.girindex.combinationstrategy.utils.Normalizer;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class NormalizerTest {

	private static ArrayList<Float> values;
	private static float minMax;
	private static float min;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		values = new ArrayList<Float>();

		values.add(1f);
		values.add(2f);
		values.add(4f);
		values.add(8f);
		values.add(16f);
		values.add(32f);

		minMax = 31f;
		min = 1f;
	}

	@Test
	public void test() {
		List<Float> normalizedValues = Normalizer.normalizeMinMax(values);
		assertTrue(normalizedValues != null);
		assertTrue(normalizedValues != values);
		assertTrue(normalizedValues.size() == values.size());
		for (int i = 0; i < normalizedValues.size(); ++i) {
			assertTrue(normalizedValues.get(i) == ((values.get(i) - min) / minMax));
		}
	}

}
