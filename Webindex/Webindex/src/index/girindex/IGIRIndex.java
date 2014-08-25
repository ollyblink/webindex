package index.girindex;

import java.util.List;

import index.utils.Ranking;
import index.utils.query.SpatialIndexQuery;
import index.utils.query.TextIndexQuery;

public interface IGIRIndex {
	public Ranking queryIndex(TextIndexQuery textQuery, SpatialIndexQuery spatialQuery);

	public void addDocument(String text);

	public void addDocuments(List<String> texts);
}
