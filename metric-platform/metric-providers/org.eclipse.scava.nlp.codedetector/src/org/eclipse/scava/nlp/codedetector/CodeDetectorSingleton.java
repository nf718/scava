/*******************************************************************************
 * Copyright (C) 2018 Edge Hill University
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.scava.nlp.codedetector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import cc.fasttext.FastText;

class CodeDetectorSingleton
{
	private static CodeDetectorSingleton singleton = new CodeDetectorSingleton();

	private static FastText codeDetector;
	private static CodeDetectorFormater formatter;
	
	private CodeDetectorSingleton()
	{
		formatter = new CodeDetectorFormater();
		try
		{
			codeDetector = getModelBin();
		} catch (IllegalArgumentException | IOException | ModelExceptions e)
		{
			e.printStackTrace();
		}
	}
	
	private void checkModelFile(Path path) throws ModelExceptions
	{
		if(!Files.exists(path))
        {
        	throw new ModelExceptions("The file "+path+" has not been found"); 
        }
	}
	
	private FastText getModelBin() throws IllegalArgumentException, IOException, ModelExceptions
	{
		FastText.Factory factory = FastText.DEFAULT_FACTORY;
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		if (path.endsWith("bin/"))
			path = path.substring(0, path.lastIndexOf("bin/"));
		File file= new File(path+"model/model_Mixed_sentences_all.bin");
		checkModelFile(file.toPath());
		return factory.load(file.toString());
	}
	
	public static CodeDetectorSingleton getInstance()
	{
		return singleton;
	}
	
	public CodeDetectorFormater getFormatter()
	{
		return formatter;
	}
	
	public FastText getCodeDetector()
	{
		return codeDetector;
	}
}
