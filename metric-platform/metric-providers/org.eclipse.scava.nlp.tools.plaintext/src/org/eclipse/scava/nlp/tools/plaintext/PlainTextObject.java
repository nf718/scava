package org.eclipse.scava.nlp.tools.plaintext;

import java.util.List;

public class PlainTextObject
{
	private List<String> plainText;
	private boolean hadReplies;
	
	public PlainTextObject(List<String> plainText)
	{
		this.plainText=plainText;
		this.hadReplies=false;
	}
	
	public PlainTextObject(List<String> plainText, boolean hadReplyCharacter)
	{
		this.plainText=plainText;
		this.hadReplies=hadReplyCharacter;
	}

	public List<String> getPlainTextAsList()
	{
		return plainText;
	}
	
	public String getPlainTextAsString()
	{
		return String.join(" ", plainText);
	}

	public boolean hadReplies()
	{
		return hadReplies;
	}
	
	
}
