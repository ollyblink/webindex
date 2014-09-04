package index.girindex.combinationstrategy.utils;

import java.util.ArrayList;

import rest.dao.RESTScore;

public class Normalizer {
	public static ArrayList<RESTScore> normalizeMinMax(final ArrayList<RESTScore> set) { 
		if(set.size()==1 || getMax(set) == getMin(set)){
			return set;
		}
		ArrayList<RESTScore> normalizedValues = new ArrayList<>();

		Float min = getMin(set);
		Float normFactor = getMax(set) - min;
		
		 

		for (RESTScore score : set) {
			float norm =  (score.getScore() - min) / normFactor; 
			normalizedValues.add(new RESTScore(score.getDocument(),norm,score.getGeometry()));
		}
	
		return normalizedValues;
	}

	public static Float getMin(ArrayList<RESTScore> set) {
		Float min = Float.MAX_VALUE;
		for (RESTScore value : set) {
			if (min > value.getScore()) {
				min = value.getScore();
			}
		}
		return min;
	}

	public static Float getMax(ArrayList<RESTScore> set) {
		Float max = Float.MIN_VALUE;
		for (RESTScore value : set) {
			if (max < value.getScore()) {
				max = value.getScore();
			}
		}
		return max;
	}
}
