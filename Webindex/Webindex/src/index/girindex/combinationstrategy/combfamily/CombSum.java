package index.girindex.combinationstrategy.combfamily;

import index.utils.Score;

import java.util.ArrayList;

public class CombSum extends AbstractComb{

	@Override
	protected Score calculateCombinedScore(ArrayList<Score> scoresOfDoc) {
		float sum = 0f;
		Long docid = null;
		for (Score score : scoresOfDoc) {
			sum += score.getScore();
			if (docid == null) {
				docid = score.getDocid();
			}
		}
 
		return new Score(docid, sum);
	}

}
