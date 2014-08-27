package index.girindex.utils.girtexttransformation.texttransformation;

import index.girindex.utils.girtexttransformation.ExtractionRequest;
import index.textindex.utils.informationextractiontools.ITextInformationExtractor;

public class IndexAndOriginalTokenExtractionStage extends AbstractTextTransformationStage {

	public IndexAndOriginalTokenExtractionStage(boolean isShowTransformationEnabled, ITextInformationExtractor extractor) {
		super(isShowTransformationEnabled,extractor);
	}

	@Override
	public void handleRequest(ExtractionRequest request) {
		this.beforeTransformation = request.getTransformationStage(precursor.getClass().getSimpleName());
		this.afterTransformation = extractor.extractIndexAndOriginalTerms((String) beforeTransformation);
		request.addTransformationStage(this.getClass().getSimpleName(), afterTransformation);

		// Pass request to the next stage
		super.handleRequest(request);
	}
 
}
