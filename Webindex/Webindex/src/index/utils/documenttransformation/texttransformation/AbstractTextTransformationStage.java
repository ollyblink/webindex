package index.utils.documenttransformation.texttransformation;

import index.textindex.utils.informationextractiontools.ITextInformationExtractor;
import index.utils.documenttransformation.AbstractTransformationStage;

public class AbstractTextTransformationStage extends AbstractTransformationStage {

	protected ITextInformationExtractor extractor;

	public AbstractTextTransformationStage(boolean isShowTransformationEnabled,ITextInformationExtractor extractor) { 
		super(isShowTransformationEnabled);
		this.extractor = extractor;
	}

}
