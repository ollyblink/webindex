package index.girindex.utils.girtexttransformation.texttransformation;

import index.girindex.utils.girtexttransformation.ExtractionRequest;
import index.girindex.utils.girtexttransformation.informationextractiontools.ITextInformationExtractor;

import java.util.ArrayList;
import java.util.Map;

public class TermFrequencyExtractionStage extends AbstractTextTransformationStage {

	public TermFrequencyExtractionStage(ITextInformationExtractor tokenizer) {
		super(tokenizer); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleRequest(ExtractionRequest request) {
		this.beforeTransformation = request.getTransformationStage(precursor.getClass().getSimpleName());
		this.afterTransformation = extractor.getTermFrequencies((Map<String, ArrayList<String>>) beforeTransformation);
 
		request.addTransformationStage(this.getClass().getSimpleName(), afterTransformation);

		// Pass request to the next stage
		super.handleRequest(request);
	}

	

}
