package org.eclipse.scava.nlp.classifiers.sentimentanalyser.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ExtraSenticNetFeatures
{
	
	private static List<String> axes;
	
	static
	{
		axes=new ArrayList<String>(6);
		axes.add("Pos");
		axes.add("Neg");
		axes.add("Pleasantness");
		axes.add("Attention");
		axes.add("Sensitivity");
		axes.add("Aptitude");
	}

	public static List<Double> getFeatures(HashMap<String, Double> senticScoresSummary)
	{
		List<Double> extraFeatures = new ArrayList<Double>();
		for(String axe : axes)
		{
			extraFeatures.add(safeDivision(senticScoresSummary.get(axe.toLowerCase()), senticScoresSummary.get("total" + axe)));
			extraFeatures.add(safeDivision(senticScoresSummary.get("total" + axe), senticScoresSummary.get("total")));
			extraFeatures.add(senticScoresSummary.get("max" + axe));
			if (senticScoresSummary.containsKey("min" + axe))
				extraFeatures.add(senticScoresSummary.get("min" + axe));
			extraFeatures.add(senticScoresSummary.get("last" + axe));
		}
		return extraFeatures;
	}

	private static double safeDivision(Double a, Double b)
	{
		Double c = a / b;
		if (c.isNaN())
			return 0;
		else
			return c;
	}

}
