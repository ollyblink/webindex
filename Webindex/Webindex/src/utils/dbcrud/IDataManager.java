package utils.dbcrud;

import index.spatialindex.utils.SpatialDocument;
import index.textindex.utils.Term;
import index.textindex.utils.TermDocs;
import index.utils.Document;
import index.utils.indexmetadata.OverallTextIndexMetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public interface IDataManager { 
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
	public ArrayList<Document> getDocuments(List<Long> docids); 
	public OverallTextIndexMetaData getOverallTextIndexMetaData();
	
	//Two convenience methods, not very efficient
	public Map<Document, List<Term>> getDocsAndTerms();
	public Map<Term, List<Document>> getTermAndDocOccurrences();
	
	//Spatial index querying
	/**
	 * Used by spatial indexes. this method returns all docs if the docids parameter is not specified or null. Else only returns the docs specified.
	 * 
	 * @param docids
	 * @return
	 */
	public ArrayList<SpatialDocument> getLocations(List<Long> docids);
	
	//Closing any opened connection
	public void close(); 
	//Check if the db is up to date. May be not if not all docs have been put into the db
//	public boolean isUpdated(); 
	//Check if the queue holding the documents to insert is empty already
	public boolean isDocumentQueueEmptyEmpty();
	public DBTablesManager getDBTablesManager();
	 
	
}

