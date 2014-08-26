package utils.dbcrud;

import index.spatialindex.utils.Location;
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
	public ArrayList<TermDocs> getTermDocs();
	public ArrayList<Term> getTerms();
	public ArrayList<Document> getDocuments();
	public ArrayList<Location> getLocations();
	public OverallTextIndexMetaData getOverallTextIndexMetaData();
	
	//Closing any opened connection
	public void close();

	 
	
}
