package org.eclipse.scava.nlp.tools.predictions.singlelabel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SingleLabelPredictionCollection
{
	List<SingleLabelPrediction> data;
	Boolean idsSet=null;
	boolean predictionSet=false;
		
	public SingleLabelPredictionCollection()
	{
		data = new ArrayList<SingleLabelPrediction>();
	}
	
	public SingleLabelPredictionCollection(int numberOfEntries)
	{
		data = new ArrayList<SingleLabelPrediction>(numberOfEntries);
	}
	
	public int size()
	{
		return data.size();
	}
	
	/**
	 * NOTE: This method do not check ID uniqueness. Please ensure that the ID is unique before calling the method.
	 * @param id 
	 * @param text
	 */
	public void addText(String id, String text)
	{
		if(idsSet==null)
			idsSet=true;
		else if(idsSet==false)
			throw new IllegalArgumentException("Can't mix entries with id and without it.");
		data.add(new SingleLabelPrediction(id, text));
	}
	
	public void addText(String text)
	{
		if(idsSet==null)
			idsSet=false;
		else if(idsSet==true)
			throw new IllegalArgumentException("Can't mix entries with id and without it.");
		data.add(new SingleLabelPrediction(text));
	}
	
	public List<String> getTextCollection()
	{
		List<String> output= new ArrayList<String>(data.size());
		data.stream().forEachOrdered(p->{
			output.add(p.getText());
		});
		return output;
	}
	
	public void setPredictions(List<Object> predictions)
	{
		if(predictions.size()!=data.size())
			throw new IllegalArgumentException("The number of predictions ["+ predictions.size()+"] doesn't match the number of data entries ["+data.size()+"]." );
		for(int i=0; i<data.size(); i++)
		{
			data.get(i).setLabel((String) predictions.get(i));
		}
		predictionSet=true;
	}
	
	public void setPredictions(List<Object> predictions, String defaultLabel)
	{
		if(predictions.size()!=data.size())
			throw new IllegalArgumentException("The number of predictions ["+ predictions.size()+"] doesn't match the number of data entries ["+data.size()+"]." );
		for(int i=0; i<data.size(); i++)
		{
			if(data.get(i).getText().isEmpty())
				data.get(i).setLabel(defaultLabel);
			else
				data.get(i).setLabel((String) predictions.get(i));
		}
		predictionSet=true;
	}
	
	public List<SingleLabelPrediction> getPredictionCollection()
	{
		return data;
	}
	
	public List<String> getPredictedLabels()
	{
		if(predictionSet==false)
			throw new UnsupportedOperationException("Please set before the predictions");
		List<String> output= new ArrayList<String>(data.size());
		data.stream().forEachOrdered(p->{
			output.add(p.getLabel());
		});
		return output;
	}
	
	public List<SingleLabelPrediction> getDataPredictedWithLabel(String label)
	{
		if(predictionSet==false)
			throw new UnsupportedOperationException("Please set before the predictions");
		List<SingleLabelPrediction> output = data.stream()
				.filter(p->p.getLabel().equals(label)).collect(Collectors.toList());
		return(output);
	}
	
	public List<SingleLabelPrediction> getDataPredictedWithLabels(List<String> labels)
	{
		if(predictionSet==false)
			throw new UnsupportedOperationException("Please set before the predictions");
		List<SingleLabelPrediction> output = new ArrayList<SingleLabelPrediction>();
		data.stream().forEachOrdered(p->{
			if(labels.contains(p.getLabel()))
				output.add(p);
		});
		return(output);
	}
	
	public List<SingleLabelPrediction> getDataPredictedWithoutLabel(String label)
	{
		if(predictionSet==false)
			throw new UnsupportedOperationException("Please set before the predictions");
		List<SingleLabelPrediction> output = data;
		output.removeIf(p -> p.getLabel()==label);
		return(output);
	}
	
	public List<SingleLabelPrediction> getDataPredictedWithoutLabels(List<String> labels)
	{
		if(predictionSet==false)
			throw new UnsupportedOperationException("Please set before the predictions");
		List<SingleLabelPrediction> output = new ArrayList<SingleLabelPrediction>();
		data.stream().forEachOrdered(p->{
			if(!labels.contains(p.getLabel()))
				output.add(p);
		});
		return(output);
	}
	
	public List<String> getTextsPredictedWithLabel(String label)
	{
		return getFilteredTexts(getDataPredictedWithLabel(label));
	}
	
	public List<String> getTextsPredictedWithLabels(List<String> labels)
	{
		return getFilteredTexts(getDataPredictedWithLabels(labels));
	}	
	
	public List<String> getTextsPredictedWithoutLabel(String label)
	{
		return getFilteredTexts(getDataPredictedWithoutLabel(label));
	}
	
	public List<String> getTextsPredictedWithoutLabels(List<String> labels)
	{
		return getFilteredTexts(getDataPredictedWithoutLabels(labels));
	}
	
	private List<String> getFilteredTexts(List<SingleLabelPrediction> filteredList)
	{
		List<String> output= new ArrayList<String>(filteredList.size());
		filteredList.stream().map(p-> p.getText()).forEachOrdered(p->output.add(p));
		return output;
	}
	
	public List<Object> getIdsPredictedWithLabel(String label)
	{
		if(idsSet==false)
			throw new UnsupportedOperationException("The collection doesn't have IDs.");
		return getFilteredIds(getDataPredictedWithLabel(label));
	}
	
	public List<Object> getIdsPredictedWithLabels(List<String> labels)
	{
		if(idsSet==false)
			throw new UnsupportedOperationException("The collection doesn't have IDs.");
		return getFilteredIds(getDataPredictedWithLabels(labels));
	}
	
	public List<Object> getIdsPredictedWithoutLabel(String label)
	{
		if(idsSet==false)
			throw new UnsupportedOperationException("The collection doesn't have IDs.");
		return getFilteredIds(getDataPredictedWithoutLabel(label));
	}
	
	public List<Object> getIdsPredictedWithoutLabels(List<String> labels)
	{
		if(idsSet==false)
			throw new UnsupportedOperationException("The collection doesn't have IDs.");
		return getFilteredIds(getDataPredictedWithoutLabels(labels));
	}
	
	private List<Object> getFilteredIds(List<SingleLabelPrediction> filteredList)
	{
		List<Object> output= new ArrayList<Object>(filteredList.size());
		filteredList.stream().map(p-> p.getId()).forEachOrdered(p->output.add(p));
		return output;
	}
	
	public HashMap<Object, String> getIdsWithPredictedLabel()
	{
		if(idsSet==false)
			throw new UnsupportedOperationException("The collection doesn't have IDs.");
		HashMap<Object, String> idsWithLabels = new HashMap<Object, String>(data.size());
		data.stream().forEachOrdered(p->{
			idsWithLabels.put(p.getId(), p.getLabel());
		});
		return idsWithLabels;
	}
}
