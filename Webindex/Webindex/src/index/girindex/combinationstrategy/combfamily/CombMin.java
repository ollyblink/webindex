package index.girindex.combinationstrategy.combfamily;

import index.utils.Score;

import java.util.ArrayList;

public class CombMin extends AbstractComb {

	@Override
	protected Score calculateCombinedScore(ArrayList<Score> scoresOfDoc) {
		float min = Float.MAX_VALUE;
		Long docid = null;
		for (Score score : scoresOfDoc) {
			if(min > score.getScore()){
				min = score.getScore();
			}
			if (docid == null) {
				docid = score.getDocid();
			}
		}
 
		return new Score(docid, min);
	}

}
