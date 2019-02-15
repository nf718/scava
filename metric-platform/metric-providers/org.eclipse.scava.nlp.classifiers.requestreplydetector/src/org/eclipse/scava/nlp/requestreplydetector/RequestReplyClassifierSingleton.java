package org.eclipse.scava.nlp.requestreplydetector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipException;

import vasttext.Vasttext;

class RequestReplyClassifierSingleton
{
	private static RequestReplyClassifierSingleton singleton = new RequestReplyClassifierSingleton();
	
	private static Vasttext requestReplyClassifier;
	
	private RequestReplyClassifierSingleton()
	{
		requestReplyClassifier = new Vasttext();
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
		File file= new File(path+"model/VastText_Code_lemma_model.zip");
		checkModelFile(file.toPath());
		requestReplyClassifier.loadModel(file);
	}
	
	public static RequestReplyClassifierSingleton getInstance()
	{
		return singleton;
	}
	
	public Vasttext getRequestReplyClassifier()
	{
		return requestReplyClassifier;
	}

}
