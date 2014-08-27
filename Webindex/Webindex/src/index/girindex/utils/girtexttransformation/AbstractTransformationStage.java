package index.girindex.utils.girtexttransformation;


public abstract class AbstractTransformationStage implements ITransformationStage {
	/** The stage before this transformation stage, or null if it's the first */
	protected ITransformationStage precursor;
	/** Next Transformation Stage. Or null if it is the last */
	protected ITransformationStage successor; 
	

	protected Object beforeTransformation;
	protected Object afterTransformation;

	public AbstractTransformationStage( ) {
	}

	public ITransformationStage getPrecursor() {
		return precursor;
	}

	public void setPrecursor(ITransformationStage precursor) {
		this.precursor = precursor;
	}

	public ITransformationStage getSuccessor() {
		return successor;
	}
 
	public void setSuccessor(ITransformationStage successor) {
		this.successor = successor;
	}

	@Override
	public void handleRequest(ExtractionRequest request) {
		showTransformation();
		if (successor != null) {
			successor.handleRequest(request);
		}
	}
	
	@Override
	public void showTransformation() {
		System.out.println("====================================================");
		System.out.println(this.getClass().getSimpleName()); 
		System.out.println("----------------------------------------------------");
		System.out.println("Before: " +beforeTransformation);
		System.out.println("After: " +afterTransformation);
		System.out.println("====================================================");
		System.out.println();
	}
}
