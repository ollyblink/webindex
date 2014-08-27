package index.spatialindex.utils.geolocating.georeferencing.ypmutils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Helper class for YPM place extractor xml parsing.
 * 
 * @author Oliver Zihler
 * @version 0.1
 * 
 */
public class YPMPlace {
	private int objectWoeid;
	private String objectType;
	private String objectName;
	private Coordinate centroid;
	private Coordinate southWest;
	private Coordinate northEast;
	private List<YPMAncestor> ancestors;

	public YPMPlace() {
	}

	public int getObjectWoeid() {
		return objectWoeid;
	}

	public String getObjectType() {
		return objectType;
	}

	public String getObjectName() {
		return objectName;
	}

 
	public List<YPMAncestor> getAncestors() {
		return ancestors;
	}

	public void setObjectWoeid(int objectWoeid) {
		this.objectWoeid = objectWoeid;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

 

 
	public YPMAncestor getAncestors(int i) {
		return (YPMAncestor) ancestors.get(i);
	}

	/**
	 * Returns an iterator over the elements in this list in proper sequence.
	 * 
	 * @return an iterator over the elements in this list in proper sequence.
	 */
	public Iterator<YPMAncestor> ancestorsIterator() {
		return ancestors.iterator();
	}

	/**
	 * Returns <tt>true</tt> if this list contains no elements.
	 * 
	 * @return <tt>true</tt> if this list contains no elements.
	 */
	public boolean isAncestorsEmpty() {
		return ancestors.isEmpty();
	}

	/**
	 * Returns <tt>true</tt> if this list contains the specified element.
	 * 
	 * @param element
	 *            element whose presence in this list is to be tested.
	 * @return <tt>true</tt> if this list contains the specified element.
	 */
	public boolean containsAncestors(YPMAncestor placeMakerAncestor) {
		return ancestors.contains(placeMakerAncestor);
	}

	/**
	 * Returns <tt>true</tt> if this list contains all of the elements of the
	 * specified collection.
	 * 
	 * @param elements
	 *            collection to be checked for containment in this list.
	 * @return <tt>true</tt> if this list contains all of the elements of the
	 *         specified collection.
	 */
	public boolean containsAllAncestors(Collection<YPMAncestor> ancestors) {
		return this.ancestors.containsAll(ancestors);
	}

	/**
	 * Returns the number of elements in this list.
	 * 
	 * @return the number of elements in this list.
	 */
	public int ancestorsSize() {
		return ancestors.size();
	}

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence.
	 * 
	 * @return an array containing all of the elements in this list in proper
	 *         sequence.
	 */
	public YPMAncestor[] ancestorsToArray() {
		return (YPMAncestor[]) ancestors.toArray(new YPMAncestor[ancestors.size()]);
	}

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence; the runtime type of the returned array is that of the specified
	 * array.
	 * 
	 * @param a
	 *            the array into which the elements of this list are to be
	 *            stored.
	 * @return an array containing all of the elements in this list in proper
	 *         sequence.
	 */
	public YPMAncestor[] ancestorsToArray(YPMAncestor[] ancestors) {
		return (YPMAncestor[]) this.ancestors.toArray(ancestors);
	}

	/**
	 * Inserts the specified element at the specified position in this list
	 * (optional operation)
	 * 
	 * @param index
	 *            index at which the specified element is to be inserted.
	 * @param element
	 *            element to be inserted.
	 */
	public void addAncestors(int index, YPMAncestor placeMakerAncestor) {
		ancestors.add(index, placeMakerAncestor);
	}

	/**
	 * Appends the specified element to the end of this list (optional
	 * operation).
	 * 
	 * @param element
	 *            element to be appended to this list.
	 * @return <tt>true</tt> (as per the general contract of the
	 *         <tt>Collection.add</tt> method).
	 */
	public boolean addAncestors(YPMAncestor placeMakerAncestor) {
		return ancestors.add(placeMakerAncestor);
	}

	/**
	 * Removes the element at the specified position in this list (optional
	 * operation).
	 * 
	 * @param index
	 *            the index of the element to removed.
	 * @return the element previously at the specified position.
	 */
	public Object removeAncestors(int index) {
		return ancestors.remove(index);
	}

	/**
	 * Removes the first occurrence in this list of the specified element
	 * (optional operation).
	 * 
	 * @param element
	 *            element to be removed from this list, if present.
	 * @return <tt>true</tt> if this list contained the specified element.
	 */
	public boolean removeAncestors(YPMAncestor placeMakerAncestor) {
		return ancestors.remove(placeMakerAncestor);
	}

	/**
	 * Removes all of the elements from this list (optional operation).
	 */
	public void clearAncestors() {
		ancestors.clear();
	}

	/**
	 * @param ancestors
	 *            the ancestors to set
	 */
	public void setAncestors(List<YPMAncestor> ancestors) {
		this.ancestors = ancestors;
	}

	public Coordinate getCentroid() {
		return centroid;
	}

	public void setCentroid(Coordinate centroid) {
		this.centroid = centroid;
	}

	public Coordinate getSouthWest() {
		return southWest;
	}

	public void setSouthWest(Coordinate southWest) {
		this.southWest = southWest;
	}

	public Coordinate getNorthEast() {
		return northEast;
	}

	public void setNorthEast(Coordinate northEast) {
		this.northEast = northEast;
	}

	
}
