package index.spatialindex.utils;

import index.utils.ISimilarityProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.vividsolutions.jts.geom.Geometry;

@XmlRootElement
public class SpatialIndexDocumentMetaData implements ISimilarityProvider {
	/** document identifier, just so that a class can be sorted by id */
	private long docid;

	/** All geometries and (at some point) the associated score with each geometry */
	private Map<Geometry, Float> spatialScores;

	public SpatialIndexDocumentMetaData(long docid) {
		this.docid = docid;
		this.spatialScores = new HashMap<Geometry, Float>();
	}

	/*
	 * CONVENIENCE METHODS
	 */
	public void addGeometries(List<Geometry> geometries) {
		for (Geometry geometry : geometries) {
			addGeometry(geometry);
		}
	}

	public void addGeometry(Geometry geometry) {
		this.spatialScores.put(geometry, 0f);
	}

	public void addSpatialScore(Geometry geometry, Float score) {
		Float sScore = spatialScores.get(geometry);
		if (sScore != null) {
			if (sScore < score) {
				sScore = score;
			} 
		}else{
			sScore = score;
		}
		spatialScores.put(geometry, sScore);
	}

	public List<Geometry> getGeometries() {
		return new ArrayList<Geometry>(spatialScores.keySet());
	}

	/*
	 * TODO: so far, I just take the maximum score of all geometries as the deciding score for the document.
	 *  
	 */
	@Override
	public Float getSimilarity() {
		return getMaxScore().getScore();
	}
	
	public Geometry getGeometryOfMaxScore(){
		return getMaxScore().getGeometry();
	}

	private SpatialScoreTriple getMaxScore() {
		Float maxScore = Float.MIN_VALUE;
		Geometry maxGeometry = null;
		Set<Geometry> keys = spatialScores.keySet();
		for (Geometry geometry : keys) {
			Float score = spatialScores.get(geometry);
			if (maxScore < score) {
				maxScore = score;
				maxGeometry = geometry;
			}
		}
		return new SpatialScoreTriple(docid, maxGeometry, maxScore);
	}

	/*
	 * FINISHED CONVENIENCE METHODS
	 */

	/*
	 * HASH CODE AND EQUALS
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpatialIndexDocumentMetaData other = (SpatialIndexDocumentMetaData) obj;
		if (docid != other.docid)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (docid ^ (docid >>> 32));
		return result;
	}

	/*
	 * FINISHED HASH CODE AND EQUALS
	 */

	/*
	 * GETTERS AND SETTERS
	 */
	public long getDocid() {
		return docid;
	}

	public void setDocid(long docid) {
		this.docid = docid;
	}

	public Map<Geometry, Float> getSpatialScores() {
		return spatialScores;
	}

	public void setSpatialScores(Map<Geometry, Float> spatialScores) {
		this.spatialScores = spatialScores;
	}

	/*
	 * FINISHED GETTERS AND SETTERS
	 */
	/**
	 * Don't use this. It's only for the XMLRootElement
	 */
	public SpatialIndexDocumentMetaData() {

	}

}
