package org.eclipse.scava.nlp.requestreplydetector;

import java.util.Locale;
import java.util.regex.Pattern;

class RequestReplyFormater
{
	Pattern label;
	Pattern punctuation;
	Pattern special;
	Pattern spaces;
	Pattern spacesStart;
	Pattern spacesEnd;
	Pattern symbols;
	Pattern numbers;
	
	public RequestReplyFormater()
	{
		special= Pattern.compile("</s>");
		label=Pattern.compile("__label__");
		punctuation=Pattern.compile("\\p{P}");
		symbols=Pattern.compile("\\p{C}|\\p{S}|\\p{Lo}|\\p{No}");
		numbers=Pattern.compile("\\d+");
		spaces=Pattern.compile("\\h+");
		spacesStart=Pattern.compile("^ ");
		spacesEnd=Pattern.compile(" $");
	}
	
	public String apply (String text)
	{
		//Keep this order order
		text=text.toLowerCase(Locale.ENGLISH);
		text=special.matcher(text).replaceAll("<\\/s>");
		text=label.matcher(text).replaceAll(" __\\label__");
		text=numbers.matcher(text).replaceAll(" ");
		text=symbols.matcher(text).replaceAll(" ");
		text=punctuation.matcher(text).replaceAll(" ");
		text=spaces.matcher(text).replaceAll(" ");
		text=spacesStart.matcher(text).replaceAll("");
		text=spacesEnd.matcher(text).replaceAll("");
		return(text);
	}
}
