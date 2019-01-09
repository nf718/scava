/*******************************************************************************
 * Copyright (C) 2018 Edge Hill University
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.scava.nlp.htmlparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;

public class HtmlParser
{
	
	private static OutputSettings outputSettings= new OutputSettings();
	private static String[] tagsToRemove= {"b","em","i","small","strong","sub","sup","ins","del","mark","span","a"};
	
	static
	{
		outputSettings.prettyPrint(false);
	}
	
	private static Whitelist whitelist()
	{		
		return Whitelist.basic().removeTags(tagsToRemove);
	}
	
	private static Whitelist whitelist(String extraTagToRemove)
	{		
		return Whitelist.basic().removeTags(tagsToRemove).removeTags(extraTagToRemove);
	}
	
	private static Whitelist whitelist(String[] extraTagToRemove)
	{		
		return Whitelist.basic().removeTags(tagsToRemove).removeTags(extraTagToRemove);
	}
	
	
	public static List<String> parse(Path path) throws IOException
	{
		return parse(readFile(path), whitelist());
	}
	
	private static String readFile(Path path) throws IOException
	{
		byte[] encoded = Files.readAllBytes(path);
		return new String(encoded, "UTF-8");
	}
	
	/**
	 * 
	 * @param extraTagsToRemove is a set of HTML tag that should be deleted as well, like p or pre.
	 */
	public static List<String> parse(Path path, String extraTagToRemove) throws IOException 
	{
		return parse(readFile(path), whitelist(extraTagToRemove));
	}
	
	/**
	 * 
	 * @param extraTagsToRemove is a set of HTML tag that should be deleted as well, like p or pre.
	 */
	public static List<String> parse(Path path, String[] extraTagToRemove) throws IOException 
	{
		return parse(readFile(path), whitelist(extraTagToRemove));
	}
	

	public static List<String> parse(String input)
	{
		return parse(input, whitelist());
	}
	
	/**
	 * 
	 * @param extraTagsToRemove is a set of HTML tag that should be deleted as well, like p or pre.
	 */
	public static List<String> parse(String input, String extraTagToRemove)
	{
		return parse(input, whitelist(extraTagToRemove));
	}
	
	/**
	 * 
	 * @param extraTagsToRemove is a set of HTML tag that should be deleted as well, like p or pre.
	 */
	public static List<String> parse(String input, String[] extraTagToRemove)
	{
		return parse(input, whitelist(extraTagToRemove));
	}
	
	
	
	private static List<String> parse(String input, Whitelist wl)
	{
		String cleanInput=Jsoup.clean(input, "", wl, outputSettings);
		//System.out.println(input);
		Document document = Jsoup.parse(cleanInput);
		
		document.outputSettings(outputSettings);
		
		List<String> textList = new ArrayList<String>();

		readNodes(document.body().childNodes(), textList);
		return textList;
	}
	
	
	private static void readNodes(List<Node> nodeList, List<String> textList)
	{
		for(Node node : nodeList)
		{
			if(node.childNodeSize()>0)
			{
				readNodes(node.childNodes(), textList);
			}
			else
			{
				if(node.nodeName().equals("#text"))
				{
					textList.add(((TextNode) node).getWholeText());
				}
			}
		}
	}
	
	public static List<Map.Entry<String,String>> parseWithTags(Path path) throws IOException 
	{
		return parseWithTags(readFile(path),whitelist());
	}
	
	/**
	 * 
	 * @param extraTagsToRemove is a set of HTML tag that should be deleted as well, like p or pre.
	 */
	public static List<Map.Entry<String,String>> parseWithTags(Path path, String extraTagToRemove) throws IOException 
	{
		return parseWithTags(readFile(path),whitelist(extraTagToRemove));
	}
	
	/**
	 * 
	 * @param extraTagsToRemove is a set of HTML tag that should be deleted as well, like p or pre.
	 */
	public static List<Map.Entry<String,String>> parseWithTags(Path path, String[] extraTagToRemove) throws IOException 
	{
		return parseWithTags(readFile(path),whitelist(extraTagToRemove));
	}
	
	public static List<Map.Entry<String,String>> parseWithTags(String input)
	{
		return parseWithTags(input, whitelist());
	}
	
	/**
	 * 
	 * @param extraTagsToRemove is a set of HTML tag that should be deleted as well, like p or pre.
	 */
	public static List<Map.Entry<String,String>> parseWithTags(String input, String extraTagToRemove)
	{
		return parseWithTags(input, whitelist(extraTagToRemove));
	}
	
	/**
	 * 
	 * @param extraTagsToRemove is a set of HTML tag that should be deleted as well, like p or pre.
	 */
	public static List<Map.Entry<String,String>> parseWithTags(String input, String[] extraTagToRemove)
	{
		return parseWithTags(input, whitelist(extraTagToRemove));
	}
	
	
	
	private static List<Map.Entry<String,String>> parseWithTags(String input, Whitelist wl)
	{
		String cleanInput=Jsoup.clean(input, "", wl, outputSettings);
		Document document = Jsoup.parse(cleanInput);
		
		document.outputSettings(outputSettings);
		
		List<Map.Entry<String,String>> textListMap = new ArrayList<Map.Entry<String,String>>();

		readNodesWithTags(document.body().childNodes(), textListMap,"body");
		return textListMap;
	}
	
	private static void readNodesWithTags(List<Node> nodeList, List<Map.Entry<String,String>> textListMap, String tag)
	{
		for(Node node : nodeList)
		{
			if(node.childNodeSize()>0)
			{
				readNodesWithTags(node.childNodes(), textListMap, node.nodeName());
			}
			else
			{
				if(node.nodeName().equals("#text"))
				{
					if(tag.equalsIgnoreCase("body"))
						tag="p";
					textListMap.add(new AbstractMap.SimpleEntry<String,String>(tag, ((TextNode) node).getWholeText() ));
				}
			}
		}
	}
	
	/**
	 * 
	 * @param ParsedHtmlWithTags is a {@code List<Map.Entry<String,String>>} containing pairs composed of an HTML tag and the content. 
	 * @param tag array indicating which tags should be used in the filtering
	 * @return A {@code List<String>} with the filters designated previously 
	 */
	public static List<String> filterParsedHtmlWithTags (List<Map.Entry<String,String>> ParsedHtmlWithTags, String tag)
	{
		return filterParsedHtmlWithTags(ParsedHtmlWithTags, tag, false);
	}
	
	
	/**
	 * 
	 * @param ParsedHtmlWithTags is a {@code List<Map.Entry<String,String>>} containing pairs composed of an HTML tag and the content. 
	 * @param tag array indicating which tags should be used in the filtering
	 * @param negation if {@code true}, it stipulates that it should be used to filter only the tags different from {@code tag}
	 * @return A {@code List<String>} with the filters designated previously 
	 */
	public static List<String> filterParsedHtmlWithTags (List<Map.Entry<String,String>> ParsedHtmlWithTags, String tag, Boolean negation)
	{
		if(negation)
		{
			return ParsedHtmlWithTags.stream().filter(pair->!pair.getKey().equalsIgnoreCase(tag)).map(pair->pair.getValue()).collect(Collectors.toList());
		}
		else
		{
			return ParsedHtmlWithTags.stream().filter(pair->pair.getKey().equalsIgnoreCase(tag)).map(pair->pair.getValue()).collect(Collectors.toList());
		}
		
	}
	
	/**
	 * 
	 * @param ParsedHtmlWithTags is a {@code List<Map.Entry<String,String>>} containing pairs composed of an HTML tag and the content. 
	 * @param tags array indicating which tags should be used in the filtering
	 * @return A {@code List<String>} with the filters designated previously 
	 */
	public List<String> filterParsedHtmlWithTags (List<Map.Entry<String,String>> ParsedHtmlWithTags, String [] tags)
	{
		return filterParsedHtmlWithTags(ParsedHtmlWithTags, tags, false);
	}
	
	/**
	 * 
	 * @param ParsedHtmlWithTags is a {@code List<Map.Entry<String,String>>} containing pairs composed of an HTML tag and the content. 
	 * @param tags array indicating which tags should be used in the filtering
	 * @param negation if {@code true}, it stipulates that it should be used to filter only the tags different from {@code tags}   
	 * @return A {@code List<String>} with the filters designated previously 
	 */
	public List<String> filterParsedHtmlWithTags (List<Map.Entry<String,String>> ParsedHtmlWithTags, String [] tags, Boolean negation)
	{
		List<String> tagsList = Arrays.asList(tags);
		tagsList.replaceAll(tag->tag.toLowerCase());
		if(negation)
		{
			return ParsedHtmlWithTags.stream().filter(pair->!tagsList.contains(pair.getKey())).map(pair->pair.getValue()).collect(Collectors.toList());
		}
		else
		{
			return ParsedHtmlWithTags.stream().filter(pair->tagsList.contains(pair.getKey())).map(pair->pair.getValue()).collect(Collectors.toList());
		}
		
	}
}
