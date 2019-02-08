package org.eclipse.scava.nlp.tools.predictions.multilabel;

import java.util.List;

public class MultiLabelPrediction
{
	private List<String> labels;
	private Object id;
	private String text;
	
	public MultiLabelPrediction(Object id, String text)
	{
		this.id=id;
		this.text=text;
	}
	
	public MultiLabelPrediction(String text)
	{
		this.text=text;
	}
	
	public void setLabels(List<String> labels)
	{
		this.labels=labels;
	}
	
	public List<String> getLabels()
	{
		return labels;
	}

	public Object getId()
	{
		return id;
	}
	
	public String getText()
	{
		return text;
	}
}
