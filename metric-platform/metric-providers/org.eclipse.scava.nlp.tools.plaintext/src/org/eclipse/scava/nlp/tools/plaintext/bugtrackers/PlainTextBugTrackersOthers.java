package org.eclipse.scava.nlp.tools.plaintext.bugtrackers;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.eclipse.scava.nlp.tools.plaintext.PlainTextObject;

public class PlainTextBugTrackersOthers
{
	private static Pattern verticalSpacing;
	
	static
	{
		verticalSpacing=Pattern.compile("\\v+");
	}

	public static PlainTextObject process(String text)
	{
		return new PlainTextObject(Arrays.asList(verticalSpacing.split(text)));
	}

}
