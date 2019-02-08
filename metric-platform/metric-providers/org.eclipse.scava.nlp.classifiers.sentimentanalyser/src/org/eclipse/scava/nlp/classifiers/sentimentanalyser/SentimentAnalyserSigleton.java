package org.eclipse.scava.nlp.classifiers.sentimentanalyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipException;

import vasttext.Vasttext;

class SentimentAnalyserSigleton
{
	private static SentimentAnalyserSigleton singleton = new SentimentAnalyserSigleton();
	
	private static Vasttext sentimentAnalyzer;
	
	private SentimentAnalyserSigleton()
	{	
		sentimentAnalyzer=new Vasttext();
		try
		{
			loadModel();
		}
		catch (ClassNotFoundException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void checkModelFile(Path path) throws FileNotFoundException
	{
		if(!Files.exists(path))
        {
        	throw new FileNotFoundException("The file "+path+" has not been found"); 
        }
	}
	
	private void loadModel() throws ZipException, ClassNotFoundException, IOException
	{
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		if (path.endsWith("bin/"))
			path = path.substring(0, path.lastIndexOf("bin/"));
		File file= new File(path+"model/Sentic_lemma_Vasttext_model.zip");
		checkModelFile(file.toPath());
		sentimentAnalyzer.loadModel(file);
	}
	
	public static SentimentAnalyserSigleton getInstance()
	{
		return singleton;
	}
	
	public Vasttext getSentimentAnalyzer()
	{
		return sentimentAnalyzer;
	}
}
