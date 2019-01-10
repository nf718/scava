package org.eclipse.scava.metricprovider.trans.bugs.index.document;

import java.util.Date;

public class GitHubCommentDocument {
	
	private String comment_id;
	private String url;
	private String body;
	private String creator;
	private Date created_at;
	private Date updated_at;
	
	private String emotion;
	private String sentiment;
	private String severity;
	private String plain_text;
	private Boolean request;
	private Boolean contains_code;
	/**
	 * @return the comment_id
	 */
	public String getComment_id() {
		return comment_id;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param comment_id the comment_id to set
	 */
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
