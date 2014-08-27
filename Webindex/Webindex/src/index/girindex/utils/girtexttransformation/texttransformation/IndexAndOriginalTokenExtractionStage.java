package index.girindex.utils.girtexttransformation.texttransformation;

import index.girindex.utils.girtexttransformation.ExtractionRequest;
import index.girindex.utils.girtexttransformation.informationextractiontools.ITextInformationExtractor;

public class IndexAndOriginalTokenExtractionStage extends AbstractTextTransformationStage {

	public IndexAndOriginalTokenExtractionStage(ITextInformationExtractor extractor) {
		super(extractor);
	}

	@Override
	public void handleRequest(ExtractionRequest request) {
		this.beforeTransformation = request.getInputText();
		this.afterTransformation = extractor.extractIndexAndOriginalTerms((String) beforeTransformation);
		request.addTransformationStage(this.getClass().getSimpleName(), afterTransformation);

		// Pass request to the next stage
		super.handleRequest(request);
	}
 
}
