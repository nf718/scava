package org.eclipse.scava.nlp.tools.plaintext.utils;

public class IntermadiatePlainTextObject
{
	private String plainText;
	private boolean hadReplies;
	
	public IntermadiatePlainTextObject(String plainText, boolean hadReplyCharacter)
	{
		this.plainText=plainText;
		this.hadReplies=hadReplyCharacter;
	}
	
	public String getPlainText()
	{
		return plainText;
	}

	public boolean hadReplies()
	{
		return hadReplies;
	}
}
