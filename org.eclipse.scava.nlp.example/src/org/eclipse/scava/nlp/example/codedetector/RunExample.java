/*******************************************************************************
 * Copyright (C) 2018 Edge Hill University
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.scava.nlp.example.codedetector;


import java.util.Arrays;
import java.util.List;
//import java.util.Scanner;

import org.eclipse.scava.nlp.codedetector.CodeDetector;
import org.eclipse.scava.nlp.tools.predictions.singlelabel.SingleLabelPrediction;
import org.eclipse.scava.nlp.tools.predictions.singlelabel.SingleLabelPredictionCollection;

public class RunExample
{
	
	public static void main(String[] args)
	{
		SingleLabelPrediction prediction;
		SingleLabelPredictionCollection listPredition;
		
		//Analysis done by String
		String word="Reasons for sticking with Java 5:    * only override annotations and HotSpotDiagnosticMXBean require JDK 6    * Mac OS X still has no Java 6    * Europe and Ganymede Eclipse platform should run on Java 5";
        //word = Normalizer.normalize(word);
        prediction=CodeDetector.predict(word);
        System.out.println(prediction.getText()+"\t"+prediction.getLabel());
        System.out.println("---------------");
        
        
        //Analysis done by lists
//        List<String> listWords = Arrays.asList("\n\n  \n", "use strict;\nuse warnings;\n", "system.out.println(word);", "How are you?\r\n I’m fine thanks.",
//        		"This is JUST at test.","System.err.println(\"Irrecognisable classification output: \" + prediction);",
//        		"    At the top level—public, or package-private (no explicit modifier).\n" + 
//        		"    At the member level—public, private, protected, or package-private (no explicit modifier).\n");
        List<String> listWords = Arrays.asList("Build ID: M20071023-1652", "Steps To Reproduce:", "The following EOL is inconsistent. The first two statements return true, and the last two return false.",
        		"-- These return true","'foo'.isTypeOf(String).println();",
        		"'foo'.isKindOf(String).println();", "-- These return false!", "String.isType('foo').println();", "String.isKind('foo').println();", "More information:");
        
        //listWords=Normalizer.normalize(listWords);
        listPredition=CodeDetector.predict(listWords);
        for(SingleLabelPrediction element : listPredition.getPredictionCollection())
        {
        	System.out.println(element.getText()+"\t"+element.getLabel());
        }
        System.out.println("---------------");
        
        //If you want only a list of the strings classified as specific label
        List<String> prueba = listPredition.getTextsPredictedWithLabel("__label__Code");
        prueba.stream().forEach(System.out::println);
        System.out.println("The program has ended");
		
	}
}
