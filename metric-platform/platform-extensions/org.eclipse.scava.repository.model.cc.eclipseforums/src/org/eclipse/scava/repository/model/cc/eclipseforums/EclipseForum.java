package org.eclipse.scava.repository.model.cc.eclipseforums;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class EclipseForum extends org.eclipse.scava.repository.model.CommunicationChannel {
	
	
	
	public EclipseForum() { 
		super();
		super.setSuperTypes("org.eclipse.scava.repository.model.cc.eclipseforums.CommunicationChannel");
		CLIENT_ID.setOwningType("org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum");
		CLIENT_SECRET.setOwningType("org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum");
		FORUM_ID.setOwningType("org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum");
		FORUM_NAME.setOwningType("org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum");
	}
	
	public static StringQueryProducer CLIENT_ID = new StringQueryProducer("client_id"); 
	public static StringQueryProducer CLIENT_SECRET = new StringQueryProducer("client_secret"); 
	public static StringQueryProducer FORUM_ID = new StringQueryProducer("forum_id"); 
	public static StringQueryProducer FORUM_NAME = new StringQueryProducer("forum_name"); 
	
	
	public String getClient_id() {
		return parseString(dbObject.get("client_id")+"", "");
	}
	
	public EclipseForum setClient_id(String client_id) {
		dbObject.put("client_id", client_id);
		notifyChanged();
		return this;
	}
	public String getClient_secret() {
		return parseString(dbObject.get("client_secret")+"", "");
	}
	
	public EclipseForum setClient_secret(String client_secret) {
		dbObject.put("client_secret", client_secret);
		notifyChanged();
		return this;
	}
	public String getForum_id() {
		return parseString(dbObject.get("forum_id")+"", "");
	}
	
	public EclipseForum setForum_id(String forum_id) {
		dbObject.put("forum_id", forum_id);
		notifyChanged();
		return this;
	}
	public String getForum_name() {
		return parseString(dbObject.get("forum_name")+"", "");
	}
	
	public EclipseForum setForum_name(String forum_name) {
		dbObject.put("forum_name", forum_name);
		notifyChanged();
		return this;
	}
	
	
	
	
}