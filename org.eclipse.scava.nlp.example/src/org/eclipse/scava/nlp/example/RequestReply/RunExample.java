package org.eclipse.scava.nlp.example.RequestReply;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.nlp.codedetector.CodeDetector;
import org.eclipse.scava.nlp.requestreplydetector.RequestReplyClassifier;
import org.eclipse.scava.nlp.requestreplydetector.RequestReplyExternalExtraFeatures;
import org.eclipse.scava.nlp.tools.plaintext.PlainTextObject;
import org.eclipse.scava.nlp.tools.plaintext.bugtrackers.PlainTextGitHub;
import org.eclipse.scava.nlp.tools.predictions.singlelabel.SingleLabelPredictionCollection;

public class RunExample
{
	public static void main(String[] args)
	{
		List<String> prueba = new ArrayList<String>();
		prueba.add("> SentencePiece uses different model/vocab files from what is included in the pre-trained BERT models.\\r\\n\\r\\nAs in, different format? That's not surprising...\\r\\n\\r\\n> SentencePiece doesn't treat space characters differently.\\r\\n\\r\\nI was under the impression they both had spaces encoded in the \\\"start of word\\\" tokens - in the case of the BERT vocabulary, anything that wasn't a ```##ing``` type token was explicitly a work or word start token.\\r\\nOr are you referring to handling of sentence start vs. non-start tokens?\\r\\nReading this, https://github.com/google/sentencepiece#whitespace-is-treated-as-a-basic-symbol if I understand it correctly, the sentences \\\"Hello World\\\" and \\\"World Hello\\\" would be parsed into different tokens even though they contain the same words just in different order. They could be tokenized into [Hello][__World] and [World][__Hello] respectively - hence no overlap between the two.\\r\\n\\r\\nWhereas WordPiece would split by whitespace first - resulting in the sentences being tokenised to [Hello][World] and [World][Hello]");
		prueba.add("Hello @JohnRojas , I have cloned the repository and tested it in Linux, but I cannot reproduce the bug. I have tried to break the code in line 204 in SummTriver_Exp.perl:\\r\\n\\r\\n```perl\\r\\n%{$param{PR}}=map{$_->{system} => $_->{path}} $set->findnodes('./PR/file');\\r\\n```\\r\\n\\r\\nOr to generate a similar error, but still nothing. Can you post your example.xml file please?");
		prueba.add("Hello @creat89 \\r\\nI have the following XML file:\\r\\n\\r\\n![imagen](https://user-images.githubusercontent.com/17374097/51204842-27b2cb00-18ca-11e9-89ba-5ed6929925fc.png)\\r\\n\\r\\nI replaced the absolute paths of each document or directory. However, I'm not sure if at line 27 is only the directory, or needs to be executed on Linux (This example was executed on Windows)\\r\\n\\r\\nRegards");
		
		
		
		PlainTextObject plainTextObject;
		SingleLabelPredictionCollection dataForClassifier = new SingleLabelPredictionCollection();
		
		for(String entry:prueba)
		{
			plainTextObject = PlainTextGitHub.process(entry);
			
			SingleLabelPredictionCollection singleLabelPredictionCollection = CodeDetector.predict(plainTextObject.getPlainTextAsList());
			boolean hasCode=false;
			if(singleLabelPredictionCollection.getDataPredictedWithLabel("__label__Code").size()>0)
				hasCode = true; 
			
			RequestReplyExternalExtraFeatures extraFeatures = new RequestReplyExternalExtraFeatures(hasCode, plainTextObject.hadReplies());
					
			dataForClassifier.addText(plainTextObject.getPlainTextAsString(), extraFeatures);
		}
		
		
		try {
			dataForClassifier = RequestReplyClassifier.predict(dataForClassifier);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.err.println("End");
	
	}
}
