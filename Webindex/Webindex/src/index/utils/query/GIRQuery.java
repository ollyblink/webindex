package index.utils.query;

public class GIRQuery {
	private boolean isIntersected;
	private TextIndexQuery textQuery;
	private SpatialIndexQuery spatialQuery;

	public GIRQuery(boolean isIntersected, TextIndexQuery textQuery, SpatialIndexQuery spatialQuery) {
		this.isIntersected = isIntersected;
		this.textQuery = textQuery;
		this.spatialQuery = spatialQuery;
	}

	public boolean isIntersected() {
		return isIntersected;
	}

	public void setIntersected(boolean isIntersected) {
		this.isIntersected = isIntersected;
	}

	public TextIndexQuery getTextQuery() {
		return textQuery;
	}

	public void setTextQuery(TextIndexQuery textQuery) {
		this.textQuery = textQuery;
	}

	public SpatialIndexQuery getSpatialQuery() {
		return spatialQuery;
	}

	public void setSpatialQuery(SpatialIndexQuery spatialQuery) {
		this.spatialQuery = spatialQuery;
	}

}
