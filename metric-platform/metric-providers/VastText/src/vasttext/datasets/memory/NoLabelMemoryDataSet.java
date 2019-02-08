package vasttext.datasets.memory;

import java.util.List;

public class NoLabelMemoryDataSet extends MemoryDataSet
{
	public NoLabelMemoryDataSet(int size)
	{
		super(size);
	}
	
	public NoLabelMemoryDataSet()
	{
		super();
	}
	
	public void addEntry(String text, List<Double> numericFeatures)
	{
		checkMixNumericFeatures(true);
		checkSizeNumericFeatures(numericFeatures.size());
		dataset.add(new MemoryDataSetEntry(null, text, numericFeatures));
	}
	
	public void addEntry(String text)
	{
		checkMixNumericFeatures(false);
		dataset.add(new MemoryDataSetEntry(null, text, null));
	}
	
	public void modifyEntry(int index, String text, List<Double> numericFeatures)
	{
		checkMixNumericFeatures(true);
		checkSizeNumericFeatures(numericFeatures.size());
		dataset.set(index, new MemoryDataSetEntry(null, text, numericFeatures));
	}
	
	public void modifyEntry(int index, String text)
	{
		checkMixNumericFeatures(false);
		dataset.set(index, new MemoryDataSetEntry(null, text, null));
	}
}
