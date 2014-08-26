package index.girindex.combinationstrategy.combfamily;

import index.utils.Score;

import java.util.ArrayList;

public class CombMax extends AbstractComb{

	@Override
	protected Score calculateCombinedScore(ArrayList<Score> scoresOfDoc) {
		float max = Float.MIN_VALUE;
		Long docid = null;
		for (Score score : scoresOfDoc) {
			if(max < score.getScore()){
				max = score.getScore();
			}
			if (docid == null) {
				docid = score.getDocid();
			}
		}
 
		return new Score(docid, max);
	}

}
