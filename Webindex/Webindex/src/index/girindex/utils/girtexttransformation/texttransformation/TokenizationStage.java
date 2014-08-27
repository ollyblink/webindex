package index.girindex.utils.girtexttransformation.texttransformation;

import index.girindex.utils.girtexttransformation.ExtractionRequest;
import index.girindex.utils.girtexttransformation.informationextractiontools.ITextInformationExtractor;

public class TokenizationStage extends AbstractTextTransformationStage {

	 
 


	public TokenizationStage(ITextInformationExtractor extractor) {
		super(extractor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleRequest(ExtractionRequest request) {
		this.beforeTransformation = request.getTransformationStage(precursor.getClass().getSimpleName());
		this.afterTransformation = extractor.tokenize((String) beforeTransformation);
		request.addTransformationStage(this.getClass().getSimpleName(), afterTransformation);

		// Pass request to the next stage
		super.handleRequest(request);
	}

	 

}
