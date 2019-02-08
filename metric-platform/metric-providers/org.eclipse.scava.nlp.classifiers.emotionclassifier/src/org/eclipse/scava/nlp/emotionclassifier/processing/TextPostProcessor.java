package org.eclipse.scava.nlp.emotionclassifier.processing;

import java.util.Locale;
import java.util.regex.Pattern;

class TextPostProcessor
{
	private static Pattern spaces;
	private static Pattern spacesStart;
	private static Pattern spacesEnd;
	private static Pattern numbers;
	
	static
	{
		numbers=Pattern.compile("\\b\\d+((\\.|,)\\d+)*\\b");
		spaces=Pattern.compile("\\h+");
		spacesStart=Pattern.compile("^ ");
		spacesEnd=Pattern.compile(" $");
	}
	
	public static String apply (String text)
	{
		//Keep this order order
		text=text.toLowerCase(Locale.ENGLISH);
		text=numbers.matcher(text).replaceAll(" ");
		text=spaces.matcher(text).replaceAll(" ");
		text=spacesStart.matcher(text).replaceAll("");
		text=spacesEnd.matcher(text).replaceAll("");
		return(text);
	}
}
