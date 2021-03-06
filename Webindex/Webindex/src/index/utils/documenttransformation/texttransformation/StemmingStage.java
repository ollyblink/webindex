package index.utils.documenttransformation.texttransformation;

import index.textindex.utils.informationextractiontools.ITextInformationExtractor;
import index.utils.documenttransformation.ExtractionRequest;

public class StemmingStage extends AbstractTextTransformationStage { 
	 
	
	 
	public StemmingStage(boolean isShowTransformationEnabled, ITextInformationExtractor extractor) {
		super(isShowTransformationEnabled,extractor);
	}

	@Override
	public void handleRequest(ExtractionRequest request) {  
		this.beforeTransformation = request.getTransformationStage(precursor.getClass().getSimpleName());
		this.afterTransformation = extractor.applyStemmer((String) beforeTransformation);
		request.addTransformationStage(this.getClass().getSimpleName(), afterTransformation);
		
		//Pass request to the next stage
		super.handleRequest(request);
	}
	
 
}
