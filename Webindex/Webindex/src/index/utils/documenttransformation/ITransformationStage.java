package index.utils.documenttransformation;

public interface ITransformationStage {
	/**
	 * Handles the request and passes it further to the next Transformation stage  
	 * @param request
	 */
	public void handleRequest(ExtractionRequest request);
	 
	public void showTransformation();
}
