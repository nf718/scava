package org.eclipse.scava.nlp.emotionclassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipException;
import vasttext.Vasttext;

class EmotionClassifierSingleton
{
	private static EmotionClassifierSingleton singleton = new EmotionClassifierSingleton();
	
	private static Vasttext emotionClassifier;
	
	private EmotionClassifierSingleton()
	{
		emotionClassifier=new Vasttext();
		try {
			loadModel();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
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
		File file= new File(path+"model/Sentic_no_lemma_Vasttext_model.zip");
		checkModelFile(file.toPath());
		emotionClassifier.loadModel(file);
	}
	
	public static EmotionClassifierSingleton getInstance()
	{
		return singleton;
	}
	
	public Vasttext getEmotionClassifier()
	{
		return emotionClassifier;
	}
}
