package org.eclipse.scava.nlp.preprocessor.markdown.github;

import java.util.Arrays;
import java.util.List;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;


public class MarkdownParserGitHub
{
	private static Parser parser;
	private static HtmlRenderer renderer;
	
	static
	{
		List<Extension> extensions = Arrays.asList(TablesExtension.create(),StrikethroughExtension.create());
		parser=Parser.builder()
				.extensions(extensions)
				.build();
		renderer=HtmlRenderer.builder()
				.extensions(extensions)
				.build();
	}
	
	public static String parse(String text)
	{
		Node document = parser.parse(text);
		return renderer.render(document);
	}
	
	public static void main(String[] args)
	{
		String input ="In directory `knowledge-base/org.eclipse.scava.knowledgebase`\n" + 
				"When I run `mvn test` I get the following ~~error~~:\n" + 
				"\n" + 
				"```\n" + 
				"[INFO] ------------------------------------------------------------------------\n" + 
				"[INFO] BUILD FAILURE\n" + 
				"[INFO] ------------------------------------------------------------------------\n" + 
				"[INFO]\n"+
				"```\n"+
				"| Command | Description |\n" + 
				"| --- | --- |\n" + 
				"| `git status` | List all *new or modified* files |\n" + 
				"| `git diff` | Show file differences that **haven't been** staged |\n";
		System.out.println(parse(input));
	}

}
