package index.utils.indexmetadata;


import index.textindex.utils.TermDocs;
import index.utils.identifers.TermDocsIdentifier;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TextIndexMetaData { 
	private HashMap<TermDocsIdentifier, TermDocs> termDocRelationship;
	private OverallTextIndexMetaData overallIndexMetaData;
	
	public TextIndexMetaData(HashMap<TermDocsIdentifier, TermDocs> termDocRelationship, OverallTextIndexMetaData overallIndexMetaData) {
		this.termDocRelationship = termDocRelationship;
		this.overallIndexMetaData = overallIndexMetaData;
	}

	public Map<TermDocsIdentifier, TermDocs> getTermDocRelationship() {
		return termDocRelationship;
	}

	public void setTermDocRelationship(HashMap<TermDocsIdentifier, TermDocs> termDocRelationship) {
		this.termDocRelationship = termDocRelationship;
	}

	public OverallTextIndexMetaData getOverallIndexMetaData() {
		return overallIndexMetaData;
	}

	public void setOverallIndexMetaData(OverallTextIndexMetaData overallIndexMetaData) {
		this.overallIndexMetaData = overallIndexMetaData;
	}

 
}
