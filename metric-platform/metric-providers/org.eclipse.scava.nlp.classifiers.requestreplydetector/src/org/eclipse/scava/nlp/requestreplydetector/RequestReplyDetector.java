package org.eclipse.scava.nlp.requestreplydetector;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.nlp.tools.predictions.singlelabel.SingleLabelPrediction;
import org.eclipse.scava.nlp.tools.predictions.singlelabel.SingleLabelPredictionCollection;
import org.eclipse.scava.nlp.tools.preprocessor.normalizer.Normalizer;

import cc.fasttext.FastText;

public class RequestReplyDetector
{
	private static FastText requestReplyDetector;
	private static RequestReplyFormater formatter;
	
	static
	{
		RequestReplyDetectorSingleton singleton = RequestReplyDetectorSingleton.getInstance();
		requestReplyDetector = singleton.getRequestReplyDetector();
		formatter = singleton.getFormater();
	}
	
	private static String formatter(String input)
	{
		input=Normalizer.normalize(input);
		return formatter.apply(input);
	}
	
	/**
	 * Predicts whether a text is a Request or a Reply.
	 * @param text Input to analyze. To have better results, text must have passed through the code detector.
     * @return {@link Prediction}, where it is kept the input text, 
     * its label (<i>__label__Question</i> or <i>__label__Reply</i>), and label's probabilities (float).
     * 
     * @see Prediction
	 */
	public static SingleLabelPrediction predict(String text)
	{
		SingleLabelPrediction prediction = new SingleLabelPrediction(text);
		prediction.setLabel(predictGeneral(text));
		return prediction;
	}
	
	
	/**
	 * Predicts for each element of a list of texts whether it is Code or English.
	 * @param textList Input to analyze.
     * @return {@code List<Prediction>}, where it is kept, for each entry of <b>textList</b>, the input text, 
     * its label (<i>__label__Question</i> or <i>__label__Reply</i>), and label's probabilities (float).
     * 
     * @see Prediction
	 */
	public static SingleLabelPredictionCollection predict(List <String> textList)
	{
		SingleLabelPredictionCollection predictionCollection = new SingleLabelPredictionCollection(textList.size());
		List<Object> predictedLabels = new ArrayList<Object>(textList.size());
		//It must be forEachOrdered otherwise, the output may not keep the same input order
		textList.stream().forEachOrdered(text->{
												predictionCollection.addText(text);
												predictedLabels.add(predictGeneral(text));
		});
		predictionCollection.setPredictions(predictedLabels);
		return predictionCollection;
	}
	
	public static SingleLabelPredictionCollection predict(SingleLabelPredictionCollection predictionCollection)
	{
		List<Object> predictedLabels = new ArrayList<Object>(predictionCollection.size());
		for(String text: predictionCollection.getTextCollection())
		{
			predictedLabels.add(predictGeneral(text));
		}
		predictionCollection.setPredictions(predictedLabels);
		return predictionCollection;
	}
	
	private static String predictGeneral(String text)
	{
		String formattedText=formatter(text);
		//The new line is added in order to have the same output that the C++ version. In fact the new line character is used to predict unseen words.
		formattedText += "\n";
		return (String) requestReplyDetector.predictLine(formattedText, 1).keySet().toArray()[0];
	}

}
