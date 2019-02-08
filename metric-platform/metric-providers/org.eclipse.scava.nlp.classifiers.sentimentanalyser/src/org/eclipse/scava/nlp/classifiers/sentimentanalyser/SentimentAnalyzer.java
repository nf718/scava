package org.eclipse.scava.nlp.classifiers.sentimentanalyser;

import java.io.IOException;
import java.util.List;

import org.eclipse.scava.nlp.classifiers.sentimentanalyser.processing.TextProcessor;
import org.eclipse.scava.nlp.tools.predictions.singlelabel.SingleLabelPrediction;
import org.eclipse.scava.nlp.tools.predictions.singlelabel.SingleLabelPredictionCollection;

import vasttext.Vasttext;
import vasttext.datasets.memory.NoLabelMemoryDataSet;

public class SentimentAnalyzer
{
	private static Vasttext sentimentAnalyzer;
	
	static
	{
		sentimentAnalyzer = SentimentAnalyserSigleton.getInstance().getSentimentAnalyzer();
	}
	
	public static SingleLabelPredictionCollection predict(List<String> textCollection) throws IOException, InterruptedException
	{
		NoLabelMemoryDataSet classifierDataSet = new NoLabelMemoryDataSet(textCollection.size());
		SingleLabelPredictionCollection predictionCollection = new SingleLabelPredictionCollection(textCollection.size());
		TextProcessor textProcessed;
		for(String text: textCollection)
		{
			textProcessed = new TextProcessor(text);
			classifierDataSet.addEntry(textProcessed.getProcessedText(), textProcessed.getExtraFeatures());
			predictionCollection.addText(text);
		}
		predictionCollection.setPredictions(sentimentAnalyzer.predictLabels(classifierDataSet));
		return predictionCollection;
	}
	
	public static SingleLabelPredictionCollection predict(SingleLabelPredictionCollection predictionCollection) throws IOException, InterruptedException
	{
		NoLabelMemoryDataSet classifierDataSet = new NoLabelMemoryDataSet();
		TextProcessor textProcessed;
		for(String text: predictionCollection.getTextCollection())
		{
			textProcessed = new TextProcessor(text);
			classifierDataSet.addEntry(textProcessed.getProcessedText(), textProcessed.getExtraFeatures());
		}
		predictionCollection.setPredictions(sentimentAnalyzer.predictLabels(classifierDataSet));
		return predictionCollection;
	}
	
	/**
	 * Note: If a large number of text needs to be predicted, it is recommended to use either {@link #predict(List)}
	 * or {@link #predict(SingleLabelPredictionCollection))}. The reason is that the classifier works with batches of data.
	 * @param text
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static SingleLabelPrediction predict(String text) throws IOException, InterruptedException
	{
		NoLabelMemoryDataSet dataset = new NoLabelMemoryDataSet(1);
		TextProcessor textProcessed;
		textProcessed = new TextProcessor(text);
		dataset.addEntry(textProcessed.getProcessedText(), textProcessed.getExtraFeatures());
		SingleLabelPrediction prediction = new SingleLabelPrediction(text);
		prediction.setLabel((String) sentimentAnalyzer.predictLabels(dataset).get(0));
		return prediction;
	}
}
