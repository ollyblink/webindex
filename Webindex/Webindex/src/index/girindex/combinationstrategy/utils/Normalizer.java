package index.girindex.combinationstrategy.utils;

import index.utils.Score;

import java.util.ArrayList;

public class Normalizer {
	public static ArrayList<Score> normalizeMinMax(final ArrayList<Score> scores) { 
		ArrayList<Score> normalizedValues = new ArrayList<>();

		Float min = getMin(scores);
		Float normFactor = getMax(scores) - min;

		for (Score score : scores) {
			float norm =  (score.getScore() - min) / normFactor ;
 
			normalizedValues.add(new Score(score.getDocid(),norm));
		}
	
		return normalizedValues;
	}

	private static Float getMin(ArrayList<Score> values) {
		Float min = Float.MAX_VALUE;
		for (Score value : values) {
			if (min > value.getScore()) {
				min = value.getScore();
			}
		}
		return min;
	}

	private static Float getMax(ArrayList<Score> values) {
		Float max = Float.MIN_VALUE;
		for (Score value : values) {
			if (max < value.getScore()) {
				max = value.getScore();
			}
		}
		return max;
	}
}
