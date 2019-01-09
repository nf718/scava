package org.eclipse.scava.repository.model.mantis;

import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class MantisCategory extends Pongo {
	
	
	public MantisCategory() { 
		super();
		_ID.setOwningType("org.eclipse.scava.repository.model.mantis.MantisCategory");
		NAME.setOwningType("org.eclipse.scava.repository.model.mantis.MantisCategory");
	}
	
	public static StringQueryProducer _ID = new StringQueryProducer("_id"); 
	public static StringQueryProducer NAME = new StringQueryProducer("name"); 
	
	
	public String get_id() {
		return parseString(dbObject.get("_id")+"", "");
	}
	
	public MantisCategory set_id(String _id) {
		dbObject.put("_id", _id);
		notifyChanged();
		return this;
	}
	public String getName() {
		return parseString(dbObject.get("name")+"", "");
	}
	
	public MantisCategory setName(String name) {
		dbObject.put("name", name);
		notifyChanged();
		return this;
	}
	
	
	
	
}