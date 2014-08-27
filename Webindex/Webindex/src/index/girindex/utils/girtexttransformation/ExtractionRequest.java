package index.girindex.utils.girtexttransformation;

import java.util.TreeMap;

public class ExtractionRequest {
	private String inputText;
	
	/**
	 * Intended to store all the transformations that have been applied to the input text.
	 * The String represents the {@link ITransformationStage} SimpleName. Query it using
	 * (ITransformationStrage-Implementation-Object).getClass().getSimpleName() (Reflection)
	 */
	private TreeMap<String, Object> transformationStages;
	
	private int transformationStageCounter = 1;

	public ExtractionRequest(String inputText) {
		this.inputText = inputText;
		this.transformationStages = new TreeMap<String, Object>();
	}

	public String getInputText() {
		return inputText;
	}

	public void setInputText(String inputText) {
		this.inputText = inputText;
	}

	public Object getTransformationStage(String transformationStage) { 
		for(String key: transformationStages.keySet()){
			if(key.contains(transformationStage)){
				return transformationStages.get(key); 
			}
		}
		return transformationStages.get(transformationStage);
	}

	
	public TreeMap<String, Object> getTransformationStages() {
		return transformationStages;
	}

	public void setTransformationStages(TreeMap<String, Object> transformationStages) {
		this.transformationStages = transformationStages;
	}

	public void addTransformationStage(String stageSimpleName, Object extractedOutput) {
		if(stageSimpleName == null || stageSimpleName == "" || extractedOutput == null){
			throw new NullPointerException();
		}
		this.transformationStages.put((transformationStageCounter++)+" "+stageSimpleName, extractedOutput);
	}
	
}
