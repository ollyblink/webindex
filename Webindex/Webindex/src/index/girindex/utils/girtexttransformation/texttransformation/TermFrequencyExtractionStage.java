package index.girindex.utils.girtexttransformation.texttransformation;

import index.girindex.utils.girtexttransformation.ExtractionRequest;
import index.textindex.utils.informationextractiontools.ITextInformationExtractor;

import java.util.ArrayList;
import java.util.Map;

public class TermFrequencyExtractionStage extends AbstractTextTransformationStage {

	public TermFrequencyExtractionStage(boolean isShowTransformationEnabled, ITextInformationExtractor extractor) {
		super(isShowTransformationEnabled,extractor);
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
