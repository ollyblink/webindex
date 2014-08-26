package index.textindex.similarities;

import index.utils.Document;
import index.utils.Score;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTextSimilarity implements ITextSimilarity{
	protected Map<Long, Score> scoreMap = new HashMap<Long, Score>();

	protected void updateScore(Document document, float sumWeight) {
		Score score = scoreMap.get(document);
		if (score == null) {
			score = new Score(document.getId().getId(), 0f);
			scoreMap.put(document.getId().getId(), score);
		}
		score.setScore(score.getScore() + sumWeight);
	}
}
