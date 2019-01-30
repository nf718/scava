package org.eclipse.scava.nlp.requestreplydetector.example;

import org.eclipse.scava.nlp.requestreplydetector.RequestReplyDetector;
import org.eclipse.scava.nlp.tools.other.predictionmanager.Prediction;

public class RunExample
{
	public static void main(String[] args)
	{
		String test = "Dear @md2manoppello , @davidediruscio and @aabherve, please check our prototype implementation. We appreciate any constructive opinion. Feel free to add (mention), or involve any other interested parties.";
		
		Prediction prediction = RequestReplyDetector.predict(test);
		System.out.println(prediction.getLabel()+"\t"+prediction.getProb());
		test = "@md2manoppello Thanks for the information. We will check your implementation, and if everything fine began to integrate into the plugin. How are we able to run the current version of the KB to test the integration? The tutorial at https://github.com/crossminer/scava/wiki/Docker-Ossmeter is up-to-date? Or do you have a more recent docker image at bay?";
		
		prediction = RequestReplyDetector.predict(test);
		System.out.println(prediction.getLabel()+"\t"+prediction.getProb());
		
		test = "We have integrated Swagger and OpenApi specification on KB. Now, KB automatically generate OpenAPI specification and swagger interface. We attached the generated OpenAPI specification and generated java client.";
		
		prediction = RequestReplyDetector.predict(test);
		System.out.println(prediction.getLabel()+"\t"+prediction.getProb());
		
		test = "Issue no longer exists on current versions";
		
		prediction = RequestReplyDetector.predict(test);
		System.out.println(prediction.getLabel()+"\t"+prediction.getProb());
		
		System.out.println("Finish");
	
	}
}
