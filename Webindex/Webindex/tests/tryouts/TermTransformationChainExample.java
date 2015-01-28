package tryouts;

import java.util.List;

import utils.dbconnection.AbstractDBConnector;
import utils.dbconnection.PGDBConnector;

import com.vividsolutions.jts.geom.Geometry;

import index.spatialindex.utils.geolocating.georeferencing.LocationFinder;
import index.textindex.utils.informationextractiontools.GermanTextInformationExtractor;
import index.textindex.utils.informationextractiontools.ITextInformationExtractor;
import index.utils.documenttransformation.ExtractionRequest;
import index.utils.documenttransformation.spatialtransformation.GeoReferencingStage;
import index.utils.documenttransformation.spatialtransformation.GeoTaggingStage;
import index.utils.documenttransformation.texttransformation.StemmingStage;
import index.utils.documenttransformation.texttransformation.StopwordRemovalStage;
import index.utils.documenttransformation.texttransformation.TokenizationStage;

public class TermTransformationChainExample {
	private static boolean showTransformations = true;
	private static ITextInformationExtractor tokenizer = new GermanTextInformationExtractor();

	public static void main(String[] args) {
		String text = "Herrlich abgelegene Tour am Rande des Nationalpark. Start am Ofenpass 2149m und in nördlicher Richtung nach \"Munt da la Bescha\". Über die Südflanke auf den Gipfel des Piz Vallatscha 3021m. Über den Grat zum Nordgipfel und Abfahrt über die schöne steile Nordflanke ins Val S-Charl nach S-Charl. Ein \"ruhiger und einsamer\" Gipfel, der relativ selten besucht wird. Ideal für Liebhaber von einsamen Berge.";
		ExtractionRequest request = new ExtractionRequest(text);
		// // Terms
		// StopwordRemovalStage stopwordRemovalStage = new StopwordRemovalStage(showTransformations, tokenizer );
		// StemmingStage stemmingStage = new StemmingStage(showTransformations, tokenizer);
		// TokenizationStage tokenizationStage = new TokenizationStage(showTransformations, tokenizer);
		//
		//
		// stopwordRemovalStage.setSuccessor(stemmingStage);
		// stemmingStage.setPrecursor(stopwordRemovalStage);
		// stemmingStage.setSuccessor(tokenizationStage);
		// tokenizationStage.setPrecursor(stemmingStage);
		//
		// stopwordRemovalStage.handleRequest(request);

		// Chaining the stages together in an appropriate way
		String host = "localhost";
		String port = "5432";
		String database = "girindex";
		String user = "postgres";
		String password = "postgres";

		AbstractDBConnector conn = new PGDBConnector(host, port, database, user, password);

		GeoTaggingStage geoTaggingStage = new GeoTaggingStage(showTransformations, conn);
		 GeoReferencingStage geoReferencingStage = new GeoReferencingStage(new LocationFinder(conn), showTransformations);
		// spatial extraction
		 geoTaggingStage.setSuccessor(geoReferencingStage);
		 geoReferencingStage.setPrecursor(geoTaggingStage);
		geoTaggingStage.handleRequest(request);
 
	}
}
