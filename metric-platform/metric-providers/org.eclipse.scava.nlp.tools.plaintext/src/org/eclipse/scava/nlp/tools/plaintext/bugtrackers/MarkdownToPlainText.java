package org.eclipse.scava.nlp.tools.plaintext.bugtrackers;

import java.util.regex.Pattern;

import org.eclipse.scava.nlp.tools.plaintext.PlainTextObject;
import org.eclipse.scava.nlp.tools.plaintext.utils.IntermadiatePlainTextObject;
import org.eclipse.scava.nlp.tools.plaintext.utils.ReplyLineDetection;
import org.eclipse.scava.nlp.tools.preprocessor.htmlparser.HtmlParser;
import org.eclipse.scava.nlp.tools.preprocessor.markdown.MarkdownParser;


class MarkdownToPlainText
{
	private static Pattern escapedNewline;
	
	static
	{
		escapedNewline = Pattern.compile("(\\\\n|\\\\r)");
	}
	
	public static PlainTextObject process(String text)
	{
		IntermadiatePlainTextObject intermadiatePlainTextObject= ReplyLineDetection.process(text);
		text= MarkdownParser.parse(intermadiatePlainTextObject.getPlainText());
		//In case the text contain escaped newlines
		text=escapedNewline.matcher(text).replaceAll("");
		return new PlainTextObject(HtmlParser.parse(text), intermadiatePlainTextObject.hadReplies());
	}
}
