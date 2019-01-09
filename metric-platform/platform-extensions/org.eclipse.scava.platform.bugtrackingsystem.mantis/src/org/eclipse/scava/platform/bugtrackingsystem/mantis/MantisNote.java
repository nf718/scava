package org.eclipse.scava.platform.bugtrackingsystem.mantis;

import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemComment;

public class MantisNote extends BugTrackingSystemComment {

	
	private static final long serialVersionUID = 1L;
	
	String id;
	String name;
	String email;
	String text;
	String created_at;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}


}
