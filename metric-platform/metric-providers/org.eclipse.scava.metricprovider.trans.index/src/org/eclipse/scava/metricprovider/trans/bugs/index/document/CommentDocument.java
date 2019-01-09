package org.eclipse.scava.metricprovider.trans.bugs.index.document;

import java.util.Date;

public class CommentDocument {
	

	private String uid;
	private String comment_Id;
	private String bug_Id;
	private String project;
	private String body;
	private String creator;
	private String emotion;
	private String sentiment;
	private String severity;
	private String plain_text;
	private Date created_at;
	private Date updated_at;	
	private Boolean request;
	private Boolean contains_code;
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @return the comment_Id
	 */
	public String getComment_Id() {
		return comment_Id;
	}
	/**
	 * @return the bug_Id
	 */
	public String getBug_Id() {
		return bug_Id;
	}
	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @return the emotion
	 */
	public String getEmotion() {
		return emotion;
	}
	/**
	 * @return the sentiment
	 */
	public String getSentiment() {
		return sentiment;
	}
	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}
	/**
	 * @return the plain_text
	 */
	public String getPlain_text() {
		return plain_text;
	}
	/**
	 * @return the created_at
	 */
	public Date getCreated_at() {
		return created_at;
	}
	/**
	 * @return the updated_at
	 */
	public Date getUpdated_at() {
		return updated_at;
	}
	/**
	 * @return the request
	 */
	public Boolean getRequest() {
		return request;
	}
	/**
	 * @return the contains_code
	 */
	public Boolean getContains_code() {
		return contains_code;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @param comment_Id the comment_Id to set
	 */
	public void setComment_Id(String comment_Id) {
		this.comment_Id = comment_Id;
	}
	/**
	 * @param bug_Id the bug_Id to set
	 */
	public void setBug_Id(String bug_Id) {
		this.bug_Id = bug_Id;
	}
	/**
	 * @param project the project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @param emotion the emotion to set
	 */
	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}
	/**
	 * @param sentiment the sentiment to set
	 */
	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}
	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	/**
	 * @param plain_text the plain_text to set
	 */
	public void setPlain_text(String plain_text) {
		this.plain_text = plain_text;
	}
	/**
	 * @param created_at the created_at to set
	 */
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	/**
	 * @param updated_at the updated_at to set
	 */
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	/**
	 * @param request the request to set
	 */
	public void setRequest(Boolean request) {
		this.request = request;
	}
	/**
	 * @param contains_code the contains_code to set
	 */
	public void setContains_code(Boolean contains_code) {
		this.contains_code = contains_code;
	}

	
}
