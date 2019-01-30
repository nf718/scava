package org.eclipse.scava.nlp.tools.core;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.decode.NLPDecoder;

class NLPCoreAnalyzerSingleton
{
	private static NLPCoreAnalyzerSingleton singleton = new NLPCoreAnalyzerSingleton();
	private static NLPDecoder decoder;
	
	private NLPCoreAnalyzerSingleton()
	{
		String configurationFile = "edu/emory/mathcs/nlp/configuration/config-decode-pos.xml";
		decoder = new NLPDecoder(IOUtils.getInputStream(configurationFile));
	}
	
	public static NLPCoreAnalyzerSingleton getInstance()
	{
		return singleton;
	}
	
	public NLPDecoder getDecoder()
	{
		return decoder;
	}
	
}
