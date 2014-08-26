package index.girindex.combinationstrategy.combfamily;

import index.utils.Ranking;
import index.utils.Score;

import java.util.ArrayList;

public class CombMNZ extends AbstractComb{

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

		float finalScore = sum *= scoresOfDoc.size();
		return new Score(docid, finalScore);
	}

	 
}
