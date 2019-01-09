package org.eclipse.scava.nlp.requestreplydetector;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.eclipse.scava.nlp.predictionmanager.Prediction;
import org.eclipse.scava.nlp.preprocessor.normalizer.Normalizer;

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
	public static Prediction predict (String text)
	{
		String formattedText=formatter(text);
		//The new line is added in order to have the same output that the C++ version. In fact the new line character is used to predict unseen words.
		formattedText += "\n";
		InputStream formattedTextAsIStream = new ByteArrayInputStream(formattedText.getBytes());
		Stream<Map<String, Float>> prediction = requestReplyDetector.predict(formattedTextAsIStream, 1);
		return PredictionConsumeStream(text, prediction);
	}
	
	
	/**
	 * Predicts for each element of a list of texts whether it is Code or English.
	 * @param textList Input to analyze.
     * @return {@code List<Prediction>}, where it is kept, for each entry of <b>textList</b>, the input text, 
     * its label (<i>__label__Question</i> or <i>__label__Reply</i>), and label's probabilities (float).
     * 
     * @see Prediction
	 */
	public static List<Prediction> predict(List <String> textList)
	{
		List<Prediction> predictionList = new ArrayList<Prediction>();
		//It must be forEachOrdered otherwise, the output may not keep the same input order
		textList.stream().forEachOrdered(text->predictionList.add(predict(text)));
		return(predictionList);
	}
	
	private static Prediction PredictionConsumeStream(String originalInput, Stream<Map<String, Float>> output)
	{
		Prediction prediction= new Prediction();
		output.map(map->map.entrySet().stream().findFirst().get()).forEachOrdered(e->prediction.set(originalInput, e.getKey(),e.getValue()));
		return prediction;
	}

}
