/*******************************************************************************
 * Copyright (c) 2019 Edge Hill University
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
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
