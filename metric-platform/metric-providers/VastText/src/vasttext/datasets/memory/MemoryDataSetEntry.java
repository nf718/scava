package vasttext.datasets.memory;

import java.util.List;

public class MemoryDataSetEntry
{
	String text;
	String labels;
	List<Double> numericFeatures;
		
	public MemoryDataSetEntry(List<String> labels, String text, List<Double> numericFeatures)
	{
		if(labels!=null)
			this.labels=convertLabels(labels);
		else
			this.labels=null;
		this.text=text;
		this.numericFeatures=numericFeatures;
	}
	
	private String convertLabels(List<String> labels)
	{
		return String.join(" ", labels);
	}

	public String getText()
	{
		return text;
	}

	public String getLabels()
	{
		return labels;
	}
	
	public int getSizeNumericFeatures()
	{
		return numericFeatures.size();
	}

	public List<Double> getNumericFeatures()
	{
		return numericFeatures;
	}
	
	
}
