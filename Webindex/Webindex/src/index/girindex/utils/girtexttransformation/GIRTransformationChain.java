package index.girindex.utils.girtexttransformation;

import index.girindex.utils.girtexttransformation.spatialtransformation.GeoReferencingStage;
import index.girindex.utils.girtexttransformation.spatialtransformation.GeoTaggingStage;
import index.girindex.utils.girtexttransformation.texttransformation.IndexAndOriginalTokenExtractionStage;
import index.girindex.utils.girtexttransformation.texttransformation.StemmingStage;
import index.girindex.utils.girtexttransformation.texttransformation.StopwordRemovalStage;
import index.girindex.utils.girtexttransformation.texttransformation.TermFrequencyExtractionStage;
import index.girindex.utils.girtexttransformation.texttransformation.TokenizationStage;
import index.textindex.utils.informationextractiontools.EnglishTextInformationExtractor;
import index.textindex.utils.informationextractiontools.ITextInformationExtractor;

/**
 * Applies the text transformation in the given order
 * 
 * @author rsp
 *
 */
public class GIRTransformationChain {
	private StopwordRemovalStage stopwordRemovalStage;

	public GIRTransformationChain(ITextInformationExtractor extractor) {
		// Terms
		stopwordRemovalStage = new StopwordRemovalStage(extractor);
		StemmingStage stemmingStage = new StemmingStage(extractor);
		TokenizationStage tokenizationStage = new TokenizationStage(extractor);
		IndexAndOriginalTokenExtractionStage indexAndOriginalTokenExtractionStage = new IndexAndOriginalTokenExtractionStage(extractor);
		TermFrequencyExtractionStage termFrequencyExtractionStage = new TermFrequencyExtractionStage(extractor);

		// Spatial
		GeoTaggingStage geoTaggingStage = new GeoTaggingStage();
		GeoReferencingStage geoReferencingStage = new GeoReferencingStage();
		
		//Chaining the stages together in an appropriate way
		
		//First text extraction
		stopwordRemovalStage.setSuccessor(stemmingStage);

		stemmingStage.setPrecursor(stopwordRemovalStage);
		stemmingStage.setSuccessor(tokenizationStage);

		tokenizationStage.setPrecursor(stemmingStage);
		tokenizationStage.setSuccessor(indexAndOriginalTokenExtractionStage);

		indexAndOriginalTokenExtractionStage.setPrecursor(stopwordRemovalStage); // so that only the relevant terms are analysed
		indexAndOriginalTokenExtractionStage.setSuccessor(termFrequencyExtractionStage);

		termFrequencyExtractionStage.setPrecursor(indexAndOriginalTokenExtractionStage);
		//Now spatial extraction
		termFrequencyExtractionStage.setSuccessor(geoTaggingStage);
		
		geoTaggingStage.setPrecursor(termFrequencyExtractionStage);
		geoTaggingStage.setSuccessor(geoReferencingStage);
		
		geoReferencingStage.setPrecursor(geoTaggingStage); 
	}

	public void submitRequest(ExtractionRequest request) {
		this.stopwordRemovalStage.handleRequest(request);
	}

	public static void main(String[] args) {
		GIRTransformationChain chain = new GIRTransformationChain(new EnglishTextInformationExtractor());
		ExtractionRequest request = new ExtractionRequest("Try to find a house in Kloten, Switzerland that is not in London, or maybe you like New Jersey and New York more.");
		chain.submitRequest(request);

		// Map<String, Object> transformationStages = request.getTransformationStages();
		// for(String stage:transformationStages.keySet()){
		// System.out.println(stage+": " +transformationStages.get(stage));
		// }

	}

}
