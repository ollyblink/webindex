package utils.dbcrud;

import index.spatialindex.utils.SpatialDocument;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocs;
import index.utils.Document;
import index.utils.indexmetadata.OverallTextIndexMetaData;

import java.util.ArrayList;
import java.util.List;


public interface IDataProvider { 
	//Adding documents
	public void addDocuments(final List<String> texts);  
	/**
	 * Use this if documents are added using REST
	 * @param pureText
	 */
	public void addDocumentDeferred(final String pureText); 
	 
	
	//Querying tables
	//Text index querying
	public ArrayList<TermDocs> getTermDocs();
	public ArrayList<Term> getTerms();
	public ArrayList<Document> getDocuments();
	public ArrayList<SpatialDocument> getLocations();
	public OverallTextIndexMetaData getOverallTextIndexMetaData();
	
	//Spatial index querying
	/**
	 * Used by spatial indexes. this method returns all docs if the docids parameter is not specified or null. Else only returns the docs specified.
	 * 
	 * @param docids
	 * @return
	 */
	public ArrayList<SpatialDocument> getSpatialDocuments(List<Long> docids);
	
	//Closing any opened connection
	public void close();

	 
	
}
