package tryouts;

import index.textindex.utils.informationextractiontools.GermanTextInformationExtractor;
import index.textindex.utils.informationextractiontools.ITextInformationExtractor;
import index.utils.documenttransformation.ExtractionRequest;
import index.utils.documenttransformation.texttransformation.StemmingStage;
import index.utils.documenttransformation.texttransformation.StopwordRemovalStage;
import index.utils.documenttransformation.texttransformation.TokenizationStage;

public class TermTransformationChainExample {
	private static boolean showTransformations = true;
	private static ITextInformationExtractor tokenizer = new GermanTextInformationExtractor();

	public static void main(String[] args) {
		String text =  "Obschon der Piz d'Err 3378m neben dem Piz Calderas 3397m nur der zweithöchste Berg der Err-Gruppe ist, ist er der namengebende Hauptgipfel des Massiv.";
		ExtractionRequest request = new ExtractionRequest(text);
		// Terms
		StopwordRemovalStage stopwordRemovalStage = new StopwordRemovalStage(showTransformations, tokenizer );
		StemmingStage stemmingStage = new StemmingStage(showTransformations, tokenizer);
		TokenizationStage tokenizationStage = new TokenizationStage(showTransformations, tokenizer);
		
		
		stopwordRemovalStage.setSuccessor(stemmingStage);
		stemmingStage.setPrecursor(stopwordRemovalStage);
		stemmingStage.setSuccessor(tokenizationStage);
		tokenizationStage.setPrecursor(stemmingStage);
		
		stopwordRemovalStage.handleRequest(request);
	}
}
