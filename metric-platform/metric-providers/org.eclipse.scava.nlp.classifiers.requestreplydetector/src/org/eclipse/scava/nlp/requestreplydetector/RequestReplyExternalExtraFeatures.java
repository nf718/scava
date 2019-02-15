package org.eclipse.scava.nlp.requestreplydetector;

import org.eclipse.scava.nlp.tools.predictions.externalExtra.ExternalExtraFeaturesObject;

public class RequestReplyExternalExtraFeatures implements ExternalExtraFeaturesObject
{
	private boolean hasCode =false;
	private boolean hadReplies = false;
	
	
	public RequestReplyExternalExtraFeatures(boolean hasCode, boolean hadReplies)
	{
		this.hasCode=hasCode;
		this.hadReplies=hadReplies;
	}
	
	public boolean hasCode()
	{
		return hasCode;
	}

	public boolean hadReplies()
	{
		return hadReplies;
	}
	
	

}
