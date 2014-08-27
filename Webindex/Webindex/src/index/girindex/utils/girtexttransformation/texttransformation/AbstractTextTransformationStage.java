package index.girindex.utils.girtexttransformation.texttransformation;

import index.girindex.utils.girtexttransformation.AbstractTransformationStage;
import index.textindex.utils.informationextractiontools.ITextInformationExtractor;

public class AbstractTextTransformationStage extends AbstractTransformationStage {

	protected ITextInformationExtractor extractor;

	public AbstractTextTransformationStage(boolean isShowTransformationEnabled,ITextInformationExtractor extractor) { 
		super(isShowTransformationEnabled);
		this.extractor = extractor;
	}

}
