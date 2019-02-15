package org.eclipse.scava.nlp.tools.plaintext.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplyLineDetection
{
	private static Pattern replyLine;
	
	static
	{
		replyLine = Pattern.compile("^\\h*>+.*$", Pattern.MULTILINE);
	}
	
	public static IntermadiatePlainTextObject process(String text)
	{
		Matcher m = replyLine.matcher(text);
		boolean reply=false;
		if(m.find())
		{
			reply=true;
			text=m.replaceAll("");
		}
		return new IntermadiatePlainTextObject(text, reply);
	}
}
