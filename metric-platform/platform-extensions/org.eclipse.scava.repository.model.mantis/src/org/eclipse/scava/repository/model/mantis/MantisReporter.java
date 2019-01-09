package org.eclipse.scava.repository.model.mantis;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class MantisReporter extends Pongo {
	
	
	
	public MantisReporter() { 
		super();
		_ID.setOwningType("org.eclipse.scava.repository.model.mantis.MantisReporter");
		NAME.setOwningType("org.eclipse.scava.repository.model.mantis.MantisReporter");
		EMAIL.setOwningType("org.eclipse.scava.repository.model.mantis.MantisReporter");
	}
	
	public static StringQueryProducer _ID = new StringQueryProducer("_id"); 
	public static StringQueryProducer NAME = new StringQueryProducer("name"); 
	public static StringQueryProducer EMAIL = new StringQueryProducer("email"); 
	
	
	public String get_id() {
		return parseString(dbObject.get("_id")+"", "");
	}
	
	public MantisReporter set_id(String _id) {
		dbObject.put("_id", _id);
		notifyChanged();
		return this;
	}
	public String getName() {
		return parseString(dbObject.get("name")+"", "");
	}
	
	public MantisReporter setName(String name) {
		dbObject.put("name", name);
		notifyChanged();
		return this;
	}
	public String getEmail() {
		return parseString(dbObject.get("email")+"", "");
	}
	
	public MantisReporter setEmail(String email) {
		dbObject.put("email", email);
		notifyChanged();
		return this;
	}
	
	
	
	
}