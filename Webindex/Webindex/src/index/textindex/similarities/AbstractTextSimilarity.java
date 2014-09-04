package index.textindex.similarities;

import index.utils.Document;
import index.utils.Score;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTextSimilarity implements ITextSimilarity {
	protected Map<Long, Score> scoreMap = new HashMap<Long, Score>();

	protected void updateScore(Document document, float weight) {
		Score score = scoreMap.get(document.getId().getId());
		if (score == null) {
			score = new Score(document, 0f, null);
			scoreMap.put(document.getId().getId(), score);
		}
		float newWeight = score.getScore() + weight;
		score.setScore(newWeight);
	}

	
}
