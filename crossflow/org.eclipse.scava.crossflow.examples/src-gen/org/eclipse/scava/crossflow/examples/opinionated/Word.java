package org.eclipse.scava.crossflow.examples.opinionated;

import java.io.Serializable;
import java.util.UUID;
import org.eclipse.scava.crossflow.runtime.Job;

public class Word extends Job {
	
	protected String w;
	
	public void setW(String w) {
		this.w = w;
	}
	
	public String getW() {
		return w;
	}
	
}