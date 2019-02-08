package org.eclipse.scava.nlp.tools.predictions.singlelabel;

public class SingleLabelPrediction
{
	private String label;
	private Object id;
	private String text;
	
	public SingleLabelPrediction(Object id, String text)
	{
		this.id=id;
		this.text=text;
	}
	
	public SingleLabelPrediction(String text)
	{
		this.text=text;
	}
	
	public void setLabel(String label)
	{
		this.label=label;
	}

	public String getLabel()
	{
		return label;
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
