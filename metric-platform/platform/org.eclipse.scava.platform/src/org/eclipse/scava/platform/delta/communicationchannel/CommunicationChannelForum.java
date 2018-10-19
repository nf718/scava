package org.eclipse.scava.platform.delta.communicationchannel;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.scava.repository.model.CommunicationChannel;

public class CommunicationChannelForum implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String forum_id;
	private String name;
	private String description;
	private String url;
	private Date creation_date;
	

	transient private CommunicationChannel communicationChannel;


	public String getFourum_id() {
		return forum_id;
	}


	public void setForum_id(String forum_id) {
		this.forum_id = forum_id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public Date getCreation_date() {
		return creation_date;
	}


	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}


	public CommunicationChannel getCommunicationChannel() {
		return communicationChannel;
	}


	public void setCommunicationChannel(CommunicationChannel communicationChannel) {
		this.communicationChannel = communicationChannel;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
