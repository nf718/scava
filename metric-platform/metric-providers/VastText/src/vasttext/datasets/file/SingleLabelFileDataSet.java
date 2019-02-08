package vasttext.datasets.file;

import java.io.IOException;
import java.nio.file.Path;

public class SingleLabelFileDataSet extends FileDataSet
{

	public SingleLabelFileDataSet(Path textFilePath, Path extraFilePath) throws IOException
	{
		super(textFilePath, extraFilePath);
		int entriesText;
		int entriesNumeric;
		super.multilabel=false;
		super.labelled=true;
		super.numericFeatures=true;
		entriesText=checkFiles(textFilePath, true);
		entriesNumeric=checkFiles(extraFilePath, false);
		if(entriesText!=entriesNumeric)
			throw new UnsupportedOperationException("Number of text entries ["+ entriesText +"] and numeric features entries ["+ numericFeaturesSize() +"] do not match.");
	}
	
	public SingleLabelFileDataSet(Path textFilePath) throws IOException
	{
		super(textFilePath);
		super.multilabel=false;
		super.labelled=true;
		super.numericFeatures=false;
		checkFiles(textFilePath, true);
	}

}
