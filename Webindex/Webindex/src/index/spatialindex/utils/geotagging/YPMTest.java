package index.spatialindex.utils.geotagging;

import index.spatialindex.utils.georeferencing.IPlaceExtractor;
import index.spatialindex.utils.georeferencing.PEFactory;
import index.spatialindex.utils.georeferencing.PEFactory.PEType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

public class YPMTest {
	public static void main(String[] args) {
		IPlaceExtractor extractor = PEFactory.createPlaceExtractor(PEType.YPM, "ollyblink",
				"C:/Users/rsp/git/Webindex/Webindex/src/files/yahooxml.xml");
		String content = "";
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
					"C:/Users/rsp/git/Webindex/Webindex/src/index/spatialindex/utils/placeextractor/calais/test.txt"), "ISO8859_1"));
			while ((line = reader.readLine()) != null) {
				content += line + "\n";
			}
			reader.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ArrayList<Geometry> locations = extractor.extract(content);
		for (Geometry b : locations) {
			System.out.println(b);
		}

		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document parse = builder.parse(new File("C:/Users/rsp/git/Webindex/Webindex/src/files/yahooxml.xml"));
			NodeList nodes = parse.getElementsByTagName("localScope");
			for (int i = 0; i < nodes.getLength(); ++i) {
				System.out.println("=============================================");
				System.out.println(nodes.item(i).getTextContent());
				System.out.println("=============================================");
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
}
