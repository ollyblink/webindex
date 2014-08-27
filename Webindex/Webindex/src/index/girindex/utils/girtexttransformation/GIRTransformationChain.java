package index.girindex.utils.girtexttransformation;

import index.girindex.utils.girtexttransformation.informationextractiontools.EnglishTextInformationExtractor;
import index.girindex.utils.girtexttransformation.informationextractiontools.ITextInformationExtractor;
import index.girindex.utils.girtexttransformation.texttransformation.IndexAndOriginalTokenExtractionStage;
import index.girindex.utils.girtexttransformation.texttransformation.StemmingStage;
import index.girindex.utils.girtexttransformation.texttransformation.StopwordRemovalStage;
import index.girindex.utils.girtexttransformation.texttransformation.TermFrequencyExtractionStage;
import index.girindex.utils.girtexttransformation.texttransformation.TokenizationStage;

import java.util.List;
import java.util.Map;

/** 
 * Applies the text transformation in the given order
 * 
 * @author rsp
 *
 */
public class GIRTransformationChain {  
	private StopwordRemovalStage stopwordRemovalStage;

	public GIRTransformationChain(ITextInformationExtractor extractor) { 
		
		stopwordRemovalStage = new StopwordRemovalStage(extractor);
		StemmingStage stemmingStage = new StemmingStage(extractor);
		TokenizationStage tokenizationStage = new TokenizationStage(extractor);
		IndexAndOriginalTokenExtractionStage indexAndOriginalTokenExtractionStage = new IndexAndOriginalTokenExtractionStage(extractor);
		TermFrequencyExtractionStage termFrequencyExtractionStage = new TermFrequencyExtractionStage(extractor);
		
		stopwordRemovalStage.setSuccessor(stemmingStage);
		
		stemmingStage.setPrecursor(stopwordRemovalStage);
		stemmingStage.setSuccessor(tokenizationStage);
		
		tokenizationStage.setPrecursor(stemmingStage);
		tokenizationStage.setSuccessor(indexAndOriginalTokenExtractionStage);
		
		indexAndOriginalTokenExtractionStage.setPrecursor(tokenizationStage);
		indexAndOriginalTokenExtractionStage.setSuccessor(termFrequencyExtractionStage);
		 
		termFrequencyExtractionStage.setPrecursor(indexAndOriginalTokenExtractionStage);
	}
	
	public void submitRequest(ExtractionRequest request){
		this.stopwordRemovalStage.handleRequest(request);
	}
	
	public static void main(String[] args) {
		GIRTransformationChain chain = new GIRTransformationChain(new EnglishTextInformationExtractor());
		ExtractionRequest request = new ExtractionRequest("This is the text which should be texted and extracted and analysed and");
		chain.submitRequest(request);
		
//		Map<String, Object> transformationStages = request.getTransformationStages();
//		for(String stage:transformationStages.keySet()){
//			System.out.println(stage+": " +transformationStages.get(stage));
//		}
		
		
	}
	
}
