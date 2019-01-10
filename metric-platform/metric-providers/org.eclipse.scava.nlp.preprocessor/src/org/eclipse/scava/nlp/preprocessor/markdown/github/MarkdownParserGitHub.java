package org.eclipse.scava.nlp.preprocessor.markdown.github;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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
	private static Pattern newline;
	
	static
	{
		List<Extension> extensions = Arrays.asList(TablesExtension.create(),StrikethroughExtension.create());
		parser=Parser.builder()
				.extensions(extensions)
				.build();
		renderer=HtmlRenderer.builder()
				.extensions(extensions)
				.build();
		newline=Pattern.compile("\\v+");
	}
	
	public static String parse(String text)
	{
		/*We need to duplicate the number of new lines as
		newlines by default are softlines and disappear
		in the Markdown parser.
		*/
		text=newline.matcher(text).replaceAll("\n\n");
		Node document = parser.parse(text);
		text=renderer.render(document);
		//We need to delete the extra newlines again before returning the text
		text=newline.matcher(text).replaceAll("");
		return text;
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
