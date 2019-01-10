//dan was here
package org.eclipse.scava.platform.delta.communicationchannel;


import java.io.Serializable;
import java.util.Date;

import org.eclipse.scava.repository.model.CommunicationChannel;

public class CommunicationChannelForumPost implements Serializable {
	
	private static final long serialVersionUID = 1L; 
	
	private String subject;
	private String text;
	private String user;
	private Date date;
	private String PostId;
	private String forumId;
	
	transient private CommunicationChannel communicationChannel;

	public CommunicationChannel getCommunicationChannel() {
		return communicationChannel;
	}
	public void setCommunicationChannel(CommunicationChannel communicationChannel) {
		this.communicationChannel = communicationChannel;
	}
	
	public String getPostId() {
		return PostId;
	}
	public void setPostId(String postId) {
		PostId = postId;
	}
	
	public String getForumId() {
		return forumId;
	}
	public void setForumId(String forumId) {
		this.forumId = forumId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CommunicationChannelForumPost) {

			if (this.PostId != ((CommunicationChannelForumPost) obj).getPostId()) {
				return false;
			}
			return true;
		}
		
		return false;
	}
	

}
