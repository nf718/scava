package org.eclipse.scava.nlp.requestreplydetector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import cc.fasttext.FastText;

class RequestReplyDetectorSingleton
{
	private static RequestReplyDetectorSingleton singleton = new RequestReplyDetectorSingleton();
	
	private static FastText requestReplyDetector;
	private static RequestReplyFormater formatter;
	
	private RequestReplyDetectorSingleton()
	{
		formatter = new RequestReplyFormater();
		try 
		{
			requestReplyDetector=getModelBin();
		} catch (IllegalArgumentException | IOException e)
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
	
	private FastText getModelBin() throws IllegalArgumentException, IOException
	{
		FastText.Factory factory = FastText.DEFAULT_FACTORY;
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		if (path.endsWith("bin/"))
			path = path.substring(0, path.lastIndexOf("bin/"));
		File file= new File(path+"model/requestreply_model_huge.bin");
		checkModelFile(file.toPath());
		return factory.load(file.toString());
	}
	
	public static RequestReplyDetectorSingleton getInstance()
	{
		return singleton;
	}
	
	public FastText getRequestReplyDetector()
	{
		return requestReplyDetector;
	}
	
	public RequestReplyFormater getFormater()
	{
		return formatter;
	}

}
