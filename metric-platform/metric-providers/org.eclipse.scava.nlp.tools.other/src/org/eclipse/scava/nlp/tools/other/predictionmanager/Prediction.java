/*******************************************************************************
 * Copyright (C) 2018 Edge Hill University
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.scava.nlp.tools.other.predictionmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * It is a class that manages a {@code Prediction} object. A {@code Prediction} object is composed of a Text {@code String}, a Label {@code String} and a probability figure {@code Float}.
 * In the future, apart from the text, it might be represented as a Map<String, Object> and become a class that will be extended by every classifier.  
 */
public class Prediction
{
	String text=null;
	String label=null;
	Double probability=null;
	
	public Prediction() {	}

	public void set(String text, String label, Double probability)
	{
		this.text=text;
		this.label=label;
		this.probability=probability;
	}
	
	public void set(String text, String label, Float probability)
	{
		this.text=text;
		this.label=label;
		this.probability=(double) probability;
	}
	
	public void set(String text, String label)
	{
		this.text=text;
		this.label=label;
		this.probability=null;
	}
	
	public String getText()
	{
		return text;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public Double getProb()
	{
		return probability;
	}

	/**
	 * 
	 * @param input The output of a classifier that has to be analyzed
	 * @param label The searched <i>label</i>
	 * @return A {@code List<Prediction>} filtered by the <i>label</i>
	 */
	public static List<Prediction> getPredictionsByLabel(List<Prediction> input, String label)
	{
		List<Prediction> output = input.stream()
									.filter(p->p.getLabel().equals(label)).collect(Collectors.toList());
		return(output);
	}
	
	/**
	 * 
	 * @param input The output of a classifier that has to be analyzed
	 * @param label The searched <i>label</i>
	 * @return  A {@code List<String>} filtered by the <i>label</i>
	 */
	public static List<String> getTextsbyLabel(List<Prediction> input, String label)
	{
		List<Prediction> listPrediction=getPredictionsByLabel(input, label);
		List<String> output= new ArrayList<String>();
		listPrediction.stream().map(p->p.getText()).forEachOrdered(t->output.add(t));
		return(output);
	}
	
	/**
	 * Method that substitutes the text of every prediction entry classified as {@code label} with {@code substitution} 
	 * @param input The output of a classifier that has to be analyzed
	 * @param label The <i>label</i> that has to be match for doing the substitution
	 * @param substitution The string that will substitute the text used for the prediction
	 * @return A modified {@code List<String>} that contains the text with the necessary substitutions
	 */
	public static List<String> substituteTextWithLabel(List<Prediction> input, String label, String substitution)
	{
		List<String> output= new ArrayList<String>();
		input.stream().forEachOrdered(p->{
			if(p.getLabel().equals(label))
				output.add(substitution);
			else
				output.add(p.getText());
			} );
		return output;
	}
	
	/**
	 * Method that substitutes the text of every prediction entry classified as {@code label} with {@code substitution}.
	 * {@code label} and {@code substitution} are found in a {@code labelAndSubstition}.
	 * @param input The output of a classifier that has to be analyzed
	 * @param labelAndSubstitution It contains the labels and the corresponding substitution to do. The structure is
	 * {@code Map<String,String>} where the key correspond to the {@code label} and the value to the {@code substitution}
	 * @return A modified {@code List<String>} that contains the text with the necessary substitutions
	 */
	public static List<String> substituteTextWithLabel(List<Prediction> input, Map<String,String> labelAndSubstitution)
	{
		List<String> output= new ArrayList<String>();
		input.stream().forEachOrdered(p->{
			if(labelAndSubstitution.containsKey(p.getLabel()))
				output.add(labelAndSubstitution.get(p.getLabel()));
			else
				output.add(p.getText());
			} );
		return output;
	}
}
