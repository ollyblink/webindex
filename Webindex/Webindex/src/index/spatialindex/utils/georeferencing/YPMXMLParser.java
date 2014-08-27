package index.spatialindex.utils.georeferencing;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * This class is intended to parse a given xml file containing the retrieved locations found with placemaker. The list it retrieves is not validated. If you want to apply validation rules/ validation coordinate bounds, use <code>PlaceMakerValidityChecker</code> instead to parse the file (it uses this class, but additionally, there are methods to validate the locations found)
 * 
 * @see <code>PlaceMakerValidtyChecker</code>
 * 
 * @author Oliver Zihler
 * @version 0.1
 * 
 */
public class YPMXMLParser {

	/**
	 * Extracts candidate place names and retrieves them from an xml YPM file.
	 * 
	 * @param fileLocation
	 *            where the YPM file is stored
	 * @return places found within the YPM xml file
	 */
	public static List<YPMPlace> extractCandidatePlaces(String fileLocation) {
		List<YPMPlace> placesFound = new ArrayList<YPMPlace>();

		try {
			Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(fileLocation));

			NodeList root = dom.getElementsByTagName("localScope");

			for (int i = 0; i < root.getLength(); ++i) {
				YPMPlace place = new YPMPlace();
				NodeList thisLocalScopeElement = root.item(i).getChildNodes();

				place.setObjectWoeid(Integer.parseInt(getNodeValue("woeId", thisLocalScopeElement)));
				place.setObjectType(getNodeValue("type", thisLocalScopeElement));

				place.setObjectName(extractName(thisLocalScopeElement, "name"));

				place.setCentroid(getCoordinates("centroid", thisLocalScopeElement));
				place.setNorthEast(getCoordinates("northEast", thisLocalScopeElement));
				place.setSouthWest(getCoordinates("southWest", thisLocalScopeElement));

				List<YPMAncestor> ancestorList = new ArrayList<YPMAncestor>();
				Node ancestors = getNode("ancestors", thisLocalScopeElement);

				NodeList childAncestors = ancestors.getChildNodes();

				for (int j = 0; j < childAncestors.getLength(); ++j) {
					if (childAncestors.item(j).getNodeName().equals("ancestor")) {
						Node ancestor = childAncestors.item(j);
						NodeList ancestorData = ancestor.getChildNodes();
						int aWoeid = Integer.parseInt(getNodeValue("woeId", ancestorData));
						String aType = getNodeValue("type", ancestorData);
						String aName = extractName(ancestorData, "name");
						YPMAncestor pMAncestor = new YPMAncestor(aWoeid, aType, aName);
						ancestorList.add(pMAncestor);
					}
				}

				place.setAncestors(ancestorList);
				placesFound.add(place);
			}

		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return placesFound;
	}

	/**
	 * Extracts a name from a node.
	 * 
	 * @param nodes
	 *            all the nodes to search.
	 */
	private static String extractName(NodeList nodes, String sName) {
		String finalName = "";
		for (int j = 0; j < nodes.getLength(); ++j) {
			if (nodes.item(j).getNodeName().toLowerCase().equalsIgnoreCase(sName)) {
				Node name = nodes.item(j);
				NodeList nameChildren = name.getChildNodes();

				for (int k = 0; k < nameChildren.getLength(); ++k) {
					finalName = (nameChildren.item(k).getNodeValue());
				}
			}
		}

		return finalName;
	}

	/**
	 * Retrieve coordinates from xml.
	 * 
	 * @param name
	 *            the name of the tag to find.
	 * @param nodeList
	 *            the list of nodes to travers.
	 * @return a coordinate pair of the location found.
	 */
	private static Coordinate getCoordinates(String name, NodeList nodeList) {
		Node coordNode = getNode(name, nodeList);
		NodeList coordList = coordNode.getChildNodes();
		double lat = Double.parseDouble(getNodeValue("latitude", coordList));
		double lon = Double.parseDouble(getNodeValue("longitude", coordList));
		Coordinate coordinatePair = new Coordinate(lon, lat);
		return coordinatePair;
	}

	/**
	 * Retrieve a node from xml structure.
	 * 
	 * @param tagName
	 *            the tag to find
	 * @param nodes
	 *            the nodes to travers.
	 * @return
	 */
	private static Node getNode(String tagName, NodeList nodes) {
		for (int x = 0; x < nodes.getLength(); x++) {
			Node node = nodes.item(x);
			if (node.getNodeName().equalsIgnoreCase(tagName)) {
				return node;
			}
		}

		return null;
	}

	/**
	 * Retrieve the value from a node.
	 * 
	 * @param tagName
	 *            the node to find.
	 * @param nodes
	 *            the nodes to travers.
	 * @return the node's value.
	 */
	private static String getNodeValue(String tagName, NodeList nodes) {
		for (int x = 0; x < nodes.getLength(); x++) {
			Node node = nodes.item(x);
			if (node.getNodeName().equalsIgnoreCase(tagName)) {
				NodeList childNodes = node.getChildNodes();
				for (int y = 0; y < childNodes.getLength(); y++) {
					Node data = childNodes.item(y);
					if (data.getNodeType() == Node.TEXT_NODE)
						return data.getNodeValue();
				}
			}
		}
		return "";
	}

}
