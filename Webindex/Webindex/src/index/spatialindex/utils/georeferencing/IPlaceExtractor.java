package index.spatialindex.utils.georeferencing;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Geometry;



/**
 * Provides the interface for constructing a module to extract places, which can
 * then be added to a spatial index
 * 
 * @author Oliver Zihler
 * @version 0.1
 * 
 */
public interface IPlaceExtractor {
	/**
	 * The interface to formulate a query
	 * 
	 * @param text
	 *            the <code>String</code> containing information about a place
	 *            to extract
	 * @return a list containing all the coordinates found that describe this
	 *         place (e.g. a point or a polygon, minimum bounding rectangle
	 *         etc.)
	 */
	public ArrayList<Geometry> extract(String text);
}
