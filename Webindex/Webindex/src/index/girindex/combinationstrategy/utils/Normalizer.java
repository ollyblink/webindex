package index.girindex.combinationstrategy.utils;

import index.utils.Score;

import java.util.ArrayList;

public class Normalizer {
	public static ArrayList<Score> normalizeMinMax(final ArrayList<Score> set) { 
		if(set.size()==1 || getMax(set) == getMin(set)){
			return set;
		}
		ArrayList<Score> normalizedValues = new ArrayList<>();

		Float min = getMin(set);
		Float normFactor = getMax(set) - min;
		
		 

		for (Score score : set) {
			float norm =  (score.getScore() - min) / normFactor; 
			normalizedValues.add(new Score(score.getDocument(),norm,score.getGeometry()));
		}
	
		return normalizedValues;
	}

	public static Float getMin(ArrayList<Score> set) {
		Float min = Float.MAX_VALUE;
		for (Score value : set) {
			if (min > value.getScore()) {
				min = value.getScore();
			}
		}
		return min;
	}

	public static Float getMax(ArrayList<Score> set) {
		Float max = Float.MIN_VALUE;
		for (Score value : set) {
			if (max < value.getScore()) {
				max = value.getScore();
			}
		}
		return max;
	}
}
