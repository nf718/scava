package vasttext.datasets.file;

import java.io.IOException;
import java.nio.file.Path;

public class NoLabelFileDataSet extends FileDataSet
{

	public NoLabelFileDataSet(Path textFilePath, Path extraFilePath) throws IOException
	{
		super(textFilePath, extraFilePath);
		int entriesText;
		int entriesNumeric;
		super.multilabel=false;
		super.labelled=false;
		super.numericFeatures=true;
		entriesText=checkFiles(textFilePath, false);
		entriesNumeric=checkFiles(extraFilePath, false);
		if(entriesText!=entriesNumeric)
			throw new UnsupportedOperationException("Number of text entries ["+ entriesText +"] and numeric features entries ["+ entriesNumeric +"] do not match.");
	}
	
	public NoLabelFileDataSet(Path textFilePath) throws IOException
	{
		super(textFilePath);
		super.multilabel=false;
		super.labelled=false;
		super.numericFeatures=false;
		checkFiles(textFilePath, false);
	}

}
