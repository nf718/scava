package org.eclipse.scava.nlp.emotionclassifier.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.nlp.emotionclassifier.EmotionClassifier;
import org.eclipse.scava.nlp.tools.predictions.multilabel.MultiLabelPredictionCollection;

public class Example
{
	public static void main(String[] args)
	{
		List<String> textCollection = new ArrayList<String>();
		
		textCollection.add("But then I don't get how your CPU usage is 100% on all core my concern as now it not being able to establish a diagnostic especially when there is nothing in logs");
		textCollection.add("");
		textCollection.add("I'll need some help from you, but first I want to give you an initial \"version/template\", I'm on it");
		textCollection.add("I like the new community manager of this project, continue like that!");
		textCollection.add("I don't like either this new update, but I'm glad that the developers are listening us and think on fixing the major problems.");
		
		try {
			MultiLabelPredictionCollection output = EmotionClassifier.predict(textCollection);
			System.out.println(output.getPredictedLabels());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
