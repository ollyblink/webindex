package index.utils.indexmetadata;

import index.textindex.utils.TermDocs;
import index.utils.identifers.TermDocsIdentifier;

import java.util.Map;

public class TextIndexMetaData { 
	private Map<TermDocsIdentifier, TermDocs> termDocRelationship;
	private OverallTextIndexMetaData overallIndexMetaData;
	
	public TextIndexMetaData(Map<TermDocsIdentifier, TermDocs> termDocRelationship, OverallTextIndexMetaData overallIndexMetaData) {
		this.termDocRelationship = termDocRelationship;
		this.overallIndexMetaData = overallIndexMetaData;
	}

	public Map<TermDocsIdentifier, TermDocs> getTermDocRelationship() {
		return termDocRelationship;
	}

	public void setTermDocRelationship(Map<TermDocsIdentifier, TermDocs> termDocRelationship) {
		this.termDocRelationship = termDocRelationship;
	}

	public OverallTextIndexMetaData getOverallIndexMetaData() {
		return overallIndexMetaData;
	}

	public void setOverallIndexMetaData(OverallTextIndexMetaData overallIndexMetaData) {
		this.overallIndexMetaData = overallIndexMetaData;
	}

 
}
