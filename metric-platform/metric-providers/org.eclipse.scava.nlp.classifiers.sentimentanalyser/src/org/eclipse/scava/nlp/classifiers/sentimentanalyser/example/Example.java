package org.eclipse.scava.nlp.classifiers.sentimentanalyser.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.nlp.classifiers.sentimentanalyser.SentimentAnalyzer;
import org.eclipse.scava.nlp.tools.predictions.singlelabel.SingleLabelPredictionCollection;

public class Example
{
	public static void main(String[] args)
	{
		List<String> textCollection = new ArrayList<String>();
		
		textCollection.add("Why the developers are taking so long for fixing this problem?!?! :(");
		textCollection.add("Thank you for the update!");
		textCollection.add("Does this work with the newest version of Deeplearning4j?");
		textCollection.add("I like the new community manager of this project, continue like that!");
		textCollection.add("I don't like either this new update, but I'm glad that the developers are listening us and think on fixing the major problems.");
		textCollection.add("");
		
		try {
			SingleLabelPredictionCollection output = SentimentAnalyzer.predict(textCollection);
			System.out.println(output.getPredictedLabels());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
