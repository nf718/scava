package org.eclipse.scava.nlp.tools.plaintext.bugtrackers;

import java.util.regex.Pattern;

import org.eclipse.scava.nlp.tools.plaintext.PlainTextObject;

public class PlainTextGitHub
{
	private static Pattern newlines;
	
	static
	{
		newlines=Pattern.compile("\\\\r\\\\n");
	}
	
	public static PlainTextObject process(String text)
	{
		text=newlines.matcher(text).replaceAll("\n");
		return MarkdownToPlainText.process(text);
	}

}
