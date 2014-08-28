package index.girindex.utils.girtexttransformation.spatialtransformation;

import index.girindex.utils.girtexttransformation.AbstractTransformationStage;
import index.girindex.utils.girtexttransformation.ExtractionRequest;
import index.spatialindex.utils.geolocating.georeferencing.YPMPlaceExtractor;
import index.spatialindex.utils.geolocating.geotagging.CalaisLocator;
import index.spatialindex.utils.geolocating.geotagging.HikrGazetteerLocator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import utils.dbconnection.PGDBConnector;

/**
 * Geotagging uses calais, but also placemaker. For learning purposes, this stage only extracts possible place names or features, but does not save any georeferencing information, although it would be easily possible to do so using placemaker.
 * 
 * Why placemaker is used too is because test have shown that Calais does not know many small towns like "Kloten", whereas placemaker does.
 * 
 * @author rsp
 *
 */
public class GeoTaggingStage extends AbstractTransformationStage {

	private static final String YPM_XML = "ypm.xml";
	private static final String CALAIS_LICENSE_KEY = "d56tq64rbar8rk9waa38wnyy";
	private static final String CALAIS_PARAMS_XML = "<c:params xmlns:c=\"http://s.opencalais.com/1/pred/\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">" + "<c:processingDirectives c:contentType=\"TEXT/RAW\" c:enableMetadataType=\"GenericRelations\" c:outputFormat=\"Text/Simple\">" + "</c:processingDirectives>" + "<c:userDirectives c:allowDistribution=\"true\" c:allowSearch=\"true\" c:externalID=\"17cabs901\" c:submitter=\"ABC\">" + "</c:userDirectives>" + "<c:externalMetadata>" + "</c:externalMetadata>" + "</c:params>";

	private static final String[] CALAIS_ENTITIES = { "City", "Continent", "Country", "NaturalFeature", "ProvinceOrState", "Region" };
	private static final String host = "localhost";
	private static final String port = "5432";
	private static final String database = "girindex";
	private static final String user = "postgres";
	private static final String password = "32qjivkd";
	private static final String dictionaryLocation =  "de-pos-maxent.bin";
	private static final String tokenLocation = "de-token.bin";

	private YPMPlaceExtractor ypmExtractor;
	private CalaisLocator calaisLocator;
	private HikrGazetteerLocator hikrExtractor;

	public GeoTaggingStage(boolean isShowTransformationEnabled) {

		super(isShowTransformationEnabled);

		calaisLocator = new CalaisLocator();
		ypmExtractor = new YPMPlaceExtractor(YPM_XML);
		this.hikrExtractor = new HikrGazetteerLocator(new PGDBConnector(host, port, database, user, password), dictionaryLocation, tokenLocation);

	}

	@Override
	public void handleRequest(ExtractionRequest request) {
		List<String> foundLocations = new ArrayList<>();
		this.beforeTransformation = new ArrayList<>();

		// looking for locations
		foundLocations.addAll(queryCalais(request.getInputText()));
		foundLocations.addAll(queryYPM(request.getInputText()));
		foundLocations.addAll(queryHikrGazetteer(request.getInputText()));

		this.afterTransformation = foundLocations;
		request.addTransformationStage(this.getClass().getSimpleName(), foundLocations);
		super.handleRequest(request);
	}

	private Set<String> queryHikrGazetteer(String inputText) {
		return hikrExtractor.parse(inputText);
	}

	private Set<String> queryYPM(String text) {
		Set<String> locations = new HashSet<String>();
		try {
			ypmExtractor.extract(text);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new FileInputStream(new File(YPM_XML)));
			NodeList localScopes = doc.getElementsByTagName("text");

			for (int i = 0; i < localScopes.getLength(); ++i) {
				locations.add(localScopes.item(i).getTextContent());
			}
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		return locations;
	}

	private Set<String> queryCalais(String text) {
		Set<String> locations = new HashSet<String>();
		try {
			String enlighten = calaisLocator.getcalaisSoap().enlighten(CALAIS_LICENSE_KEY, text, CALAIS_PARAMS_XML);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(enlighten));
			Document doc = builder.parse(is);

			for (String entity : CALAIS_ENTITIES) {
				NodeList nodes = doc.getElementsByTagName(entity);
				for (int i = 0; i < nodes.getLength(); i++) {
					Node item = nodes.item(i);
					if (item != null) {
						NamedNodeMap nodeMap = item.getAttributes();
						if (nodeMap != null) {
							Node namedItem = nodeMap.getNamedItem("normalized");
							if (namedItem != null) {
								locations.add(namedItem.getTextContent());
							}
						}

					}

				}
			}
		} catch (ServiceException | ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return locations;
	}

	public static void main(String[] args) {

		String content = "Try to find Kloten, Switzerland that in London, or maybe you like the New Jersey, New York more.";

		GeoTaggingStage t = new GeoTaggingStage(true);
		t.handleRequest(new ExtractionRequest(content));
	}
}
