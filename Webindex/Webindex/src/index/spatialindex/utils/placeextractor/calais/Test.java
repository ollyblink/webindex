package index.spatialindex.utils.placeextractor.calais;


public class Test {
//	public static void main(String[] args) {
//		String licenseID = "d56tq64rbar8rk9waa38wnyy";
//		String content = "";
//		String line = null;
//		try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\rsp\\Desktop\\javaEEdev\\Webindex\\src\\com\\clearforest\\test.txt"), "ISO8859_1"));
//			while ((line = reader.readLine()) != null) {
//				content += line + "\n";
//			}
//			reader.close();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		String paramsXML = "<c:params xmlns:c=\"http://s.opencalais.com/1/pred/\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"><c:processingDirectives c:contentType=\"TEXT/RAW\" c:enableMetadataType=\"GenericRelations\" c:outputFormat=\"Text/Simple\"></c:processingDirectives><c:userDirectives c:allowDistribution=\"true\" c:allowSearch=\"true\" c:externalID=\"17cabs901\" c:submitter=\"ABC\"></c:userDirectives><c:externalMetadata></c:externalMetadata></c:params>";
//		try {
//			String enlighten = new CalaisLocator().getcalaisSoap().enlighten(licenseID, content, paramsXML);
//			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			InputSource is = new InputSource();
//			is.setCharacterStream(new StringReader(enlighten));
//
//			Document doc = builder.parse(is);
//			// System.out.println(enlighten);
//			NodeList nodes = doc.getElementsByTagName("City");
//
//			IPlaceExtractor extractor = PEFactory.createPlaceExtractor(PEType.YPM, "", "ollyblink", "C:/Users/rsp/Desktop/javaEEdev/Webindex/src/utils/placeextractor/yahoopm.xml", null, null);
//			String cities = "";
//			for (int i = 0; i < nodes.getLength(); i++) {
//				cities += (nodes.item(i).getTextContent())+" ";
//			}
//			List<BoundingRectangle> locations = extractor.extract(cities);
//			for(BoundingRectangle b: locations){
//				System.out.println(b);
//			}
//		} catch (ServiceException | ParserConfigurationException | SAXException | IOException e) {
//			e.printStackTrace();
//		}
//	}
}
