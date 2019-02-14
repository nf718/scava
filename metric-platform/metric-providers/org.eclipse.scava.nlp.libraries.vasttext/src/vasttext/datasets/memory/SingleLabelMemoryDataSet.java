package vasttext.datasets.memory;

import java.util.ArrayList;
import java.util.List;

public class SingleLabelMemoryDataSet extends MemoryDataSet
{
	public SingleLabelMemoryDataSet()
	{
		super();
	}
	
	public SingleLabelMemoryDataSet(int size)
	{
		super(size);
	}
	
	public void addEntry(String label, String text, List<Double> numericFeatures)
	{
		checkMixNumericFeatures(true);
		checkSizeNumericFeatures(numericFeatures.size());
		dataset.add(new MemoryDataSetEntry(stringToLabel(label), text, numericFeatures));
	}
	
	public void addEntry(String label, String text)
	{
		checkMixNumericFeatures(false);
		dataset.add(new MemoryDataSetEntry(stringToLabel(label), text, null));
	}
	
	public void modifyEntry(int index, String label, String text, List<Double> numericFeatures)
	{
		checkMixNumericFeatures(true);
		checkSizeNumericFeatures(numericFeatures.size());
		dataset.set(index, new MemoryDataSetEntry(stringToLabel(label), text, numericFeatures));
	}
	
	public void modifyEntry(int index, String label, String text)
	{
		checkMixNumericFeatures(false);
		dataset.set(index, new MemoryDataSetEntry(stringToLabel(label), text, null));
	}
	
	private List<String> stringToLabel(String label)
	{
		List<String> labels = new ArrayList<String>();
		labels.add(label);
		return labels;
	}
}
