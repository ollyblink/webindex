package index.girindex.utils.girtexttransformation.texttransformation;

import index.girindex.utils.girtexttransformation.AbstractTransformationStage;
import index.girindex.utils.girtexttransformation.informationextractiontools.ITextInformationExtractor;

public class AbstractTextTransformationStage extends AbstractTransformationStage {

	protected ITextInformationExtractor extractor;

	public AbstractTextTransformationStage(ITextInformationExtractor extractor) { 
		this.extractor = extractor;
	}

}
