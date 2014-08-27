package index.girindex;

import index.girindex.utils.GIRDocument;
import index.spatialindex.implementations.ISpatialIndexNoInsertion;
import index.textindex.implementations.ITextIndexNoInsertion;
import index.utils.Ranking;
import index.utils.query.GIRQuery;

public interface IGIRIndex extends ITextIndexNoInsertion, ISpatialIndexNoInsertion{
	
	
	public Ranking queryIndex(GIRQuery query);
	
	public void addDocument(GIRDocument document);
	 
}
