/*******************************************************************************
 * Copyright (c) 2019 Edge Hill University
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.scava.nlp.tools.plaintext.communicationchannels;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.eclipse.scava.nlp.tools.plaintext.PlainTextObject;
import org.eclipse.scava.nlp.tools.plaintext.utils.IntermadiatePlainTextObject;
import org.eclipse.scava.nlp.tools.plaintext.utils.ReplyLineDetection;
import org.eclipse.scava.nlp.tools.preprocessor.htmlparser.HtmlParser;

public class PlainTextNewsgroups
{
	private static Pattern htmlNewlines;
	private static Pattern nobreakableSpace;
	private static Pattern newline;
	
	static
	{
		htmlNewlines= Pattern.compile("=\n", Pattern.MULTILINE);
		nobreakableSpace=Pattern.compile("(=C2)?=A0");
		newline = Pattern.compile("\n");
	}
	
	public static PlainTextObject process(String text)
	{
		text=htmlNewlines.matcher(text).replaceAll("");
		text=nobreakableSpace.matcher(text).replaceAll(" ");
		text=String.join("\n", HtmlParser.parse(text));
		IntermadiatePlainTextObject intermadiatePlainTextObject= ReplyLineDetection.process(text);
		text=intermadiatePlainTextObject.getPlainText();
		return new PlainTextObject(Arrays.asList(newline.split(text)), intermadiatePlainTextObject.hadReplies());
	}
}
