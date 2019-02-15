package org.eclipse.scava.nlp.tools.predictions.singlelabel;

import org.eclipse.scava.nlp.tools.predictions.externalExtra.ExternalExtraFeaturesObject;

public class SingleLabelPrediction
{
	private String label;
	private Object id;
	private String text;
	private ExternalExtraFeaturesObject externalExtra;
	
	public SingleLabelPrediction(Object id, String text, ExternalExtraFeaturesObject externalExtra)
	{
		this.id=id;
		this.text=text;
		this.externalExtra=externalExtra;
	}
	
	public SingleLabelPrediction(Object id, String text)
	{
		this.id=id;
		this.text=text;
		this.externalExtra=null;
	}
	
	public SingleLabelPrediction(String text, ExternalExtraFeaturesObject externalExtra)
	{
		this.text=text;
		this.externalExtra=externalExtra;
	}
	
	public SingleLabelPrediction(String text)
	{
		this.text=text;
		this.externalExtra=null;
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

	public ExternalExtraFeaturesObject getExternalExtra()
	{
		return externalExtra;
	}
	
	
}
