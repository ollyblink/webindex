package index.girindex.combinationstrategy.utils;

import java.util.ArrayList;
import java.util.List;

public class Normalizer {
	public static List<Float> normalizeMinMax(final List<Float> values) {

		List<Float> normalizedValues = new ArrayList<>();

		Float min = getMin(values);
		Float normFactor = getMax(values) - min;

		for (Float value : values) {
			normalizedValues.add((value - min) / normFactor);
		}
		return normalizedValues;
	}

	private static Float getMin(final List<Float> values) {
		Float min = Float.MAX_VALUE;
		for (Float value : values) {
			if (min > value) {
				min = value;
			}
		}
		return min;
	}

	private static Float getMax(final List<Float> values) {
		Float max = Float.MIN_VALUE;
		for (Float value : values) {
			if (max < value) {
				max = value;
			}
		}
		return max;
	}
}
