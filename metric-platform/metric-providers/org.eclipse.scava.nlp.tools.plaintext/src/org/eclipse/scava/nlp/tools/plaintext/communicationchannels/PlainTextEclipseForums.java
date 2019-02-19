package org.eclipse.scava.nlp.tools.plaintext.communicationchannels;

import java.util.regex.Pattern;

import org.eclipse.scava.nlp.tools.plaintext.PlainTextObject;
import org.eclipse.scava.nlp.tools.preprocessor.htmlparser.HtmlParser;

public class PlainTextEclipseForums
{
	private static Pattern escapedNewline;
	
	static
	{
		escapedNewline = Pattern.compile("(\\\\n|\\\\r)+");
	}
	
	public static PlainTextObject process(String text)
	{
		text=escapedNewline.matcher(text).replaceAll("\n");
		return new PlainTextObject(HtmlParser.parse(text));
	}
	
}
