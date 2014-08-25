package index.girindex.utils;

public final class ScoreTuple {
	private Float textScore;
	private Float spatialScore;

	public ScoreTuple() {
	}

	public ScoreTuple(Float textScore, Float spatialScore) {
		this.textScore = textScore;
		this.spatialScore = spatialScore;
	}

	public void setTextScore(Float textScore) {
		this.textScore = textScore;
	}

	public void setSpatialScore(Float spatialScore) {
		this.spatialScore = spatialScore;
	}

	public Float getTextScore() {
		return textScore;
	}

	public Float getSpatialScore() {
		return spatialScore;
	}

}
