package index.utils;

import index.combinationstrategy.ICombinationStrategy;
import index.spatialindex.utils.SpatialIndexDocumentMetaData;
import index.textindex.utils.TermDocumentValues;
import index.textindex.utils.TextIndexDocumentMetaData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class IndexDocument implements Comparable<IndexDocument> {

	private long id;
	private String fulltext;
	private Float overallSimilarity;

	private TextIndexDocumentMetaData textIndexDocumentMetaData;
	private SpatialIndexDocumentMetaData spatialIndexDocumentMetaData;

	public IndexDocument() {

	}

	public IndexDocument(long id) {
		this(id, "");
	}

	public IndexDocument(long id, String fulltext) {
		this.id = id;
		if (fulltext != null) {
			this.fulltext = fulltext.replace("´", "'");
		}
		this.overallSimilarity = 0f;
		this.textIndexDocumentMetaData = new TextIndexDocumentMetaData(id);
		this.spatialIndexDocumentMetaData = new SpatialIndexDocumentMetaData(id);
	}

	public long getId() {
		return id;
	}

	public String getFulltext() {
		return fulltext;
	}

	public SpatialIndexDocumentMetaData getSpatialIndexDocumentMetaData() {
		return spatialIndexDocumentMetaData;
	}

	public void setSpatialIndexDocumentMetaData(SpatialIndexDocumentMetaData spatialIndexDocumentMetaData) {
		this.spatialIndexDocumentMetaData = spatialIndexDocumentMetaData;
	}

	public TextIndexDocumentMetaData getTextIndexDocumentMetaData() {
		return textIndexDocumentMetaData;
	}

	public void setTextIndexDocumentMetaData(TextIndexDocumentMetaData textIndexDocumentMetaData) {
		this.textIndexDocumentMetaData = textIndexDocumentMetaData;
	}

	public float getOverallSimilarity(ICombinationStrategy strategy) {
		return strategy.combineScores(new Float[] { textIndexDocumentMetaData.getSimilarity(), spatialIndexDocumentMetaData.getSimilarity() });
	}

	public void setOverallSimilarity(float overallSimilarity) {
		this.overallSimilarity = overallSimilarity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		IndexDocument other = (IndexDocument) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setFulltext(String fulltext) {
		this.fulltext = fulltext.replace("´", "'");
	}

	public void addHiliteSpans() {
		Map<Integer, Boolean> hilitedIndexes = new HashMap<Integer, Boolean>();

		String[] textParts = fulltext.split("\\s");
		for (int i = 0; i < textParts.length; ++i) {
			hilitedIndexes.put(i, false);
		}

		Map<String, TermDocumentValues> termToDocVals = textIndexDocumentMetaData.getTermDocValues();
		if (termToDocVals != null) {
			Set<String> values = termToDocVals.keySet();
			if (values != null) {
				for (String term : values) {
					List<String> originalTerms = termToDocVals.get(term).getOriginalTerms();
					for (String originalTerm : originalTerms) {
						hiliteParts(hilitedIndexes, textParts, replaceDoubleQuotes(originalTerm));
					}
				}

				this.fulltext = reassembleText(textParts);
			}
		}
	}

	private void hiliteParts(Map<Integer, Boolean> hilitedIndexes, String[] textParts, String originalTerm) {
		for (int i = 0; i < textParts.length; ++i) {
			String part = textParts[i];
			if (shouldBeHighlighted(originalTerm, part, i, hilitedIndexes)) {
				String hilitedOrigTerm = "<span class='highlited'>" + originalTerm + "</span>";
				textParts[i] = part.replace(originalTerm, hilitedOrigTerm);
				hilitedIndexes.put(i, true);
			}
		}
	}

	private String reassembleText(String[] textParts) {
		String newFulltext = "";
		for (String part : textParts) {
			newFulltext += part + " ";
		}
		return newFulltext;
	}

	private boolean shouldBeHighlighted(String originalTerm, String part, int i, Map<Integer, Boolean> hilitedIndexes) {
		String tPart = part.replaceAll("\\p{P}", "").trim();
		String tOrigTerm = originalTerm.replaceAll("\\p{P}", "").trim();
		return tPart.equals(tOrigTerm) && (!hilitedIndexes.get(i));
	}

	private String replaceDoubleQuotes(String originalTerm) {
		if (originalTerm.contains("''")) {
			originalTerm = originalTerm.replace("''", "'").replace("[", "").replace("]", "");
		}
		return originalTerm;
	}

	@Override
	public int compareTo(IndexDocument o) {
		return overallSimilarity.compareTo(o.overallSimilarity);
	}

}
