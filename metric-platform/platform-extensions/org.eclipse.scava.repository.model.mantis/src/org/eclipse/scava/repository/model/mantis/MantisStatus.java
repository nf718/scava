package org.eclipse.scava.repository.model.mantis;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class MantisStatus extends Pongo {
	
	
	
	public MantisStatus() { 
		super();
		_ID.setOwningType("org.eclipse.scava.repository.model.mantis.MantisStatus");
		NAME.setOwningType("org.eclipse.scava.repository.model.mantis.MantisStatus");
		LABEL.setOwningType("org.eclipse.scava.repository.model.mantis.MantisStatus");
		COLOUR.setOwningType("org.eclipse.scava.repository.model.mantis.MantisStatus");
	}
	
	public static StringQueryProducer _ID = new StringQueryProducer("_id"); 
	public static StringQueryProducer NAME = new StringQueryProducer("name"); 
	public static StringQueryProducer LABEL = new StringQueryProducer("label"); 
	public static StringQueryProducer COLOUR = new StringQueryProducer("colour"); 
	
	
	public String get_id() {
		return parseString(dbObject.get("_id")+"", "");
	}
	
	public MantisStatus set_id(String _id) {
		dbObject.put("_id", _id);
		notifyChanged();
		return this;
	}
	public String getName() {
		return parseString(dbObject.get("name")+"", "");
	}
	
	public MantisStatus setName(String name) {
		dbObject.put("name", name);
		notifyChanged();
		return this;
	}
	public String getLabel() {
		return parseString(dbObject.get("label")+"", "");
	}
	
	public MantisStatus setLabel(String label) {
		dbObject.put("label", label);
		notifyChanged();
		return this;
	}
	public String getColour() {
		return parseString(dbObject.get("colour")+"", "");
	}
	
	public MantisStatus setColour(String colour) {
		dbObject.put("colour", colour);
		notifyChanged();
		return this;
	}
	
	
	
	
}