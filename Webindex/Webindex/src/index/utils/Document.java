package index.utils;

import index.utils.identifers.DocumentIdentifier;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class Document {

	private DocumentIdentifier id;
	private String fulltext;
	private int sizeInBytes;
	private int indexedNrOfWords;
	private int rawNrOfWords;
	private float docVectorNorm1;
	private float docVectorNorm2;
	private float docVectorNorm3;

	public Document() {

	}

	public Document(long id, String fulltext, int sizeInBytes, int indexedNrOfWords, int rawNrOfWords, float docVectorNorm1, float docVectorNorm2,
			float docVectorNorm3) {
		this.id = new DocumentIdentifier(id);
		this.fulltext = fulltext;
		this.sizeInBytes = sizeInBytes;
		this.indexedNrOfWords = indexedNrOfWords;
		this.rawNrOfWords = rawNrOfWords;
		this.docVectorNorm1 = docVectorNorm1;
		this.docVectorNorm2 = docVectorNorm2;
		this.docVectorNorm3 = docVectorNorm3;
	}

	public Document(long id) {
		this.id = new DocumentIdentifier(id);
	}

	public DocumentIdentifier getId() {
		return id;
	}

	public void setId(long id) {
		this.id = new DocumentIdentifier(id);
	}

	public String getFulltext() {
		return fulltext;
	}

	public void setFulltext(String fulltext) {
		this.fulltext = fulltext;
	}

	public int getSizeInBytes() {
		return sizeInBytes;
	}

	public void setSizeInBytes(int sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}

	public int getIndexedNrOfWords() {
		return indexedNrOfWords;
	}

	public void setIndexedNrOfWords(int indexedNrOfWords) {
		this.indexedNrOfWords = indexedNrOfWords;
	}

	public int getRawNrOfWords() {
		return rawNrOfWords;
	}

	public void setRawNrOfWords(int rawNrOfWords) {
		this.rawNrOfWords = rawNrOfWords;
	}

	public float getDocVectorNorm1() {
		return docVectorNorm1;
	}

	public void setDocVectorNorm1(float docVectorNorm1) {
		this.docVectorNorm1 = docVectorNorm1;
	}

	public float getDocVectorNorm2() {
		return docVectorNorm2;
	}

	public void setDocVectorNorm2(float docVectorNorm2) {
		this.docVectorNorm2 = docVectorNorm2;
	}

	public float getDocVectorNorm3() {
		return docVectorNorm3;
	}

	public void setDocVectorNorm3(float docVectorNorm3) {
		this.docVectorNorm3 = docVectorNorm3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// public void addHiliteSpans() {
	// Map<Integer, Boolean> hilitedIndexes = new HashMap<Integer, Boolean>();
	//
	// String[] textParts = fulltext.split("\\s");
	// for (int i = 0; i < textParts.length; ++i) {
	// hilitedIndexes.put(i, false);
	// }
	//
	// Map<String, TermDocumentValues> termToDocVals = textIndexDocumentMetaData.getTermDocValues();
	// if (termToDocVals != null) {
	// Set<String> values = termToDocVals.keySet();
	// if (values != null) {
	// for (String term : values) {
	// List<String> originalTerms = termToDocVals.get(term).getOriginalTerms();
	// for (String originalTerm : originalTerms) {
	// hiliteParts(hilitedIndexes, textParts, replaceDoubleQuotes(originalTerm));
	// }
	// }
	//
	// this.fulltext = reassembleText(textParts);
	// }
	// }
	// }
	//
	// private void hiliteParts(Map<Integer, Boolean> hilitedIndexes, String[] textParts, String originalTerm) {
	// for (int i = 0; i < textParts.length; ++i) {
	// String part = textParts[i];
	// if (shouldBeHighlighted(originalTerm, part, i, hilitedIndexes)) {
	// String hilitedOrigTerm = "<span class='highlited'>" + originalTerm + "</span>";
	// textParts[i] = part.replace(originalTerm, hilitedOrigTerm);
	// hilitedIndexes.put(i, true);
	// }
	// }
	// }
	//
	// private String reassembleText(String[] textParts) {
	// String newFulltext = "";
	// for (String part : textParts) {
	// newFulltext += part + " ";
	// }
	// return newFulltext;
	// }
	//
	// private boolean shouldBeHighlighted(String originalTerm, String part, int i, Map<Integer, Boolean> hilitedIndexes) {
	// String tPart = part.replaceAll("\\p{P}", "").trim();
	// String tOrigTerm = originalTerm.replaceAll("\\p{P}", "").trim();
	// return tPart.equals(tOrigTerm) && (!hilitedIndexes.get(i));
	// }
	//
	// private String replaceDoubleQuotes(String originalTerm) {
	// if (originalTerm.contains("''")) {
	// originalTerm = originalTerm.replace("''", "'").replace("[", "").replace("]", "");
	// }
	// return originalTerm;
	// }
	//
	//
	// public boolean hasHigherScoreThan(Document document, String comparableScoreType) {
	// switch (comparableScoreType) {
	// case "text":
	// return textIndexDocumentMetaData.getSimilarity() > document.textIndexDocumentMetaData.getSimilarity();
	// case "space":
	// return spatialIndexDocumentMetaData.getSimilarity() > document.spatialIndexDocumentMetaData.getSimilarity();
	// }
	// return false;
	// }
	//
	// public void setHigherScore(Document toCompare, String comparableScoreType) {
	// switch (comparableScoreType) {
	// case "text":
	// textIndexDocumentMetaData.setSimilarity(toCompare.textIndexDocumentMetaData.getSimilarity());
	// break;
	// case "space":
	// spatialIndexDocumentMetaData.setSpatialScores(toCompare.spatialIndexDocumentMetaData.getSpatialScores());
	// break;
	// }
	// }

}
