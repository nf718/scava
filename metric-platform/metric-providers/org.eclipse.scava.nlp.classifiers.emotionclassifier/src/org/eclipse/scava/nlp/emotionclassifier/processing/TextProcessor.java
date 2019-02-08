package org.eclipse.scava.nlp.emotionclassifier.processing;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.nlp.resources.sentinet5.SenticNet5;
import org.eclipse.scava.nlp.tools.core.NLPCoreAnalyzer;
import org.eclipse.scava.nlp.tools.other.emoticons.EmoticonConverter;
import org.eclipse.scava.nlp.tools.other.symbolconverter.SymbolConverter;

public class TextProcessor
{
	private List<Double> extraFeatures;
	private String processedText;
	
	public TextProcessor(String text)
	{
		text = EmoticonConverter.transform(text);
		text = SymbolConverter.transform(text);
		
		extraFeatures = new ArrayList<Double>();
		extraFeatures=ExtraTextualFeaturesExtractor.getExtraFeatures(text);
		
		NLPCoreAnalyzer coreAnalyzedText = new NLPCoreAnalyzer(text);
		
		extraFeatures.addAll(ExtraSenticNetFeatures.getFeatures(SenticNet5.analyzeTextAndSummaryScores(coreAnalyzedText)));
		
		processedText=TextPostProcessor.apply(coreAnalyzedText.tokenizedText());
	}
	
	public String getProcessedText()
	{
		return processedText;
	}
	
	public List<Double> getExtraFeatures()
	{
		return extraFeatures;
	}
	
	
}
