package vasttext.datasets.memory;

import java.util.List;

public class MultiLabelMemoryDataSet extends MemoryDataSet
{	
	public MultiLabelMemoryDataSet()
	{
		super();
	}
	
	public MultiLabelMemoryDataSet(int size)
	{
		super(size);
	}
	
	public void addEntry(List<String> labels, String text, List<Double> numericFeatures)
	{
		checkMixNumericFeatures(true);
		checkSizeNumericFeatures(numericFeatures.size());
		dataset.add(new MemoryDataSetEntry(labels, text, numericFeatures));
	}
	
	public void addEntry(List<String> labels, String text)
	{
		checkMixNumericFeatures(false);
		dataset.add(new MemoryDataSetEntry(labels, text, null));
	}
	
	public void modifyEntry(int index, List<String> labels, String text, List<Double> numericFeatures)
	{
		checkMixNumericFeatures(true);
		checkSizeNumericFeatures(numericFeatures.size());
		dataset.set(index, new MemoryDataSetEntry(labels, text, numericFeatures));
	}
	
	public void modifyEntry(int index, List<String> labels, String text)
	{
		checkMixNumericFeatures(false);
		dataset.set(index, new MemoryDataSetEntry(labels, text, null));
	}
}
