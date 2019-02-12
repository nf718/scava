package org.eclipse.scava.nlp.tools.other.symbolconverter;

import java.util.regex.Pattern;

public class SymbolConverter {
	
	private static Pattern upVoteNumber;
	//private static Pattern upVoteWord;
	private static Pattern downVoteNumber;
	//private static Pattern downVoteWord;
	private static Pattern ellipsis;
	private static Pattern question;
	private static Pattern exclamation;
	private static Pattern spacing;
	
	
	static
	{
		upVoteNumber=Pattern.compile("(?<!\\w|\\d)(\\+1)(?!\\w|\\.|\\d)");
		//upVoteWord=Pattern.compile("(?i)(?<!\\w|\\d)(up-vot((e(d|s)?))|ing)(?!\\w|\\d)");
		downVoteNumber=Pattern.compile("(?<!\\w|\\d)(-1)(?!\\w|\\.|\\d)");
		//downVoteWord=Pattern.compile("(?i)(?<!\\w|\\d)(down-vot((e(d|s)?))|ing))(?!\\w|\\d)");
		ellipsis=Pattern.compile("(?<=\\w)(\\.( ?\\.)+)(?!\\w|\\d)");
		question=Pattern.compile("\\?");
		exclamation=Pattern.compile("!");
		spacing=Pattern.compile("\\h+");
	}
	
	public static String transform(String text)
	{
		text=upVoteNumber.matcher(text).replaceAll("upvote");
		//text=upVoteWord.matcher(text).replaceAll("upvote");
		text=downVoteNumber.matcher(text).replaceAll("downvote");
		//text=downVoteWord.matcher(text).replaceAll("downvote");
		text=ellipsis.matcher(text).replaceAll("…");
		text=question.matcher(text).replaceAll(" ? ");
		text=exclamation.matcher(text).replaceAll(" ! ");
		text=spacing.matcher(text).replaceAll(" ");
		
		return text;
	}

}
