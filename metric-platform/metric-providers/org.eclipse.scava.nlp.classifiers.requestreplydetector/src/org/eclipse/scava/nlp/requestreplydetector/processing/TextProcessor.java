package org.eclipse.scava.nlp.requestreplydetector.processing;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.nlp.requestreplydetector.RequestReplyExternalExtraFeatures;
import org.eclipse.scava.nlp.tools.core.analyzer.NLPCoreAnalyzer;
import org.eclipse.scava.nlp.tools.other.emoticons.EmoticonConverter;
import org.eclipse.scava.nlp.tools.other.symbolconverter.SymbolConverter;
import org.eclipse.scava.nlp.tools.preprocessor.normalizer.Normalizer;

public class TextProcessor
{
	private List<Double> extraFeatures;
	private String processedText;
	
	public TextProcessor(String text, RequestReplyExternalExtraFeatures extra)
	{
		text = Normalizer.normalize(text);
		text = EmoticonConverter.transform(text);
		text = SymbolConverter.transform(text);
		
		extraFeatures = new ArrayList<Double>();
		extraFeatures=ExtraTextualFeaturesExtractor.getExtraFeatures(text);
		extraFeatures.add(extra.hasCode() ? 1.0 : 0.0);
		extraFeatures.add(extra.hadReplies() ? 1.0 : 0.0);
		
		NLPCoreAnalyzer coreAnalyzedText = new NLPCoreAnalyzer(text);
		
		processedText=TextPostProcessor.apply(coreAnalyzedText.lemmatizeAsText());
	}
	
	public TextProcessor(String text)
	{
		
		text = EmoticonConverter.transform(text);
		text = SymbolConverter.transform(text);
		
		extraFeatures = new ArrayList<Double>();
		extraFeatures=ExtraTextualFeaturesExtractor.getExtraFeatures(text);
		extraFeatures.add(0.0);
		extraFeatures.add(0.0);
		
		NLPCoreAnalyzer coreAnalyzedText = new NLPCoreAnalyzer(text);
		
		processedText=TextPostProcessor.apply(coreAnalyzedText.lemmatizeAsText());
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
