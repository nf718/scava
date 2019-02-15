package org.eclipse.scava.nlp.tools.plaintext.bugtrackers;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.eclipse.scava.nlp.tools.plaintext.PlainTextObject;

public class PlainTextBugzilla
{
	private static Pattern horizontalSpacing;
	
	static
	{
		horizontalSpacing=Pattern.compile("\\h\\h+");
	}
	
	//This method should be temporal while the new reader for Bugzilla arrives
	public static PlainTextObject process(String text)
	{
		return new PlainTextObject(Arrays.asList(horizontalSpacing.split(text)));
	}
			
}
