package org.eclipse.scava.repository.model.mantis;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class MantisSeverity extends Pongo {
	
	
	
	public MantisSeverity() { 
		super();
		_ID.setOwningType("org.eclipse.scava.repository.model.mantis.MantisSeverity");
		NAME.setOwningType("org.eclipse.scava.repository.model.mantis.MantisSeverity");
		LABEL.setOwningType("org.eclipse.scava.repository.model.mantis.MantisSeverity");
	}
	
	public static StringQueryProducer _ID = new StringQueryProducer("_id"); 
	public static StringQueryProducer NAME = new StringQueryProducer("name"); 
	public static StringQueryProducer LABEL = new StringQueryProducer("label"); 
	
	
	public String get_id() {
		return parseString(dbObject.get("_id")+"", "");
	}
	
	public MantisSeverity set_id(String _id) {
		dbObject.put("_id", _id);
		notifyChanged();
		return this;
	}
	public String getName() {
		return parseString(dbObject.get("name")+"", "");
	}
	
	public MantisSeverity setName(String name) {
		dbObject.put("name", name);
		notifyChanged();
		return this;
	}
	public String getLabel() {
		return parseString(dbObject.get("label")+"", "");
	}
	
	public MantisSeverity setLabel(String label) {
		dbObject.put("label", label);
		notifyChanged();
		return this;
	}
	
	
	
	
}