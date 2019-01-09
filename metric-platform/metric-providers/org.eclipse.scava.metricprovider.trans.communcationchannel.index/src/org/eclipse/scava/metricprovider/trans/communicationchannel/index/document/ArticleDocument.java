package org.eclipse.scava.metricprovider.trans.communicationchannel.index.document;


public class ArticleDocument extends Document {
	
	private String article_id;
	private int articleNumber;
	private String message_thread_id;
	private String subject;
	private String text;
	private String user;
	/**
	 * @return the article_id
	 */
	public String getArticle_id() {
		return article_id;
	}
	/**
	 * @return the articleNumber
	 */
	public int getArticleNumber() {
		return articleNumber;
	}
	/**
	 * @return the message_thread_id
	 */
	public String getMessage_thread_id() {
		return message_thread_id;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param article_id the article_id to set
	 */
	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}
	/**
	 * @param articleNumber the articleNumber to set
	 */
	public void setArticleNumber(int articleNumber) {
		this.articleNumber = articleNumber;
	}
	/**
	 * @param message_thread_id the message_thread_id to set
	 */
	public void setMessage_thread_id(String message_thread_id) {
		this.message_thread_id = message_thread_id;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
}
