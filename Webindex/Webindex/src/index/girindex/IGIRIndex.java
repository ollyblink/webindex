package index.girindex;

import index.girindex.combinationstrategy.ICombinationStrategy;
import index.girindex.utils.GIRDocument;
import index.spatialindex.implementations.ISpatialIndexNoInsertion;
import index.textindex.implementations.ITextIndexNoInsertion;
import index.utils.query.GIRQuery;
import rest.dao.Ranking;

public interface IGIRIndex extends ITextIndexNoInsertion, ISpatialIndexNoInsertion{
	
	
	public Ranking queryIndex(GIRQuery query);
	
	public void addDocument(GIRDocument document);

	public void setCombinationStrategy(ICombinationStrategy create);
	 
}
