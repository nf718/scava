package org.eclipse.scava.metricprovider.trans.communicationchannel.index.document;

import java.util.Date;

public class ForumPostDocument extends Document {
	
	//Forum
	private String fourmId;
	private String forumName;
	private String forumDescription;
	private String forumUrl;
	private Date forumCreationDate;
	private int topics;
	private int posts;
	
	//Topic 
	private String topicId;
	private String subject;
	private String topicUrl;
	private String rootPost;
	private int replies;
	private int views;
	private Date firstPostDate;
	private Date lastPostDate;
	
	
	//Post
	private String postId;
	private Date postDate;
	private String postUrl;
	private String userUrl;
	
	
	
	
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}


	/**
	 * @return the postId
	 */
	public String getPostId() {
		return postId;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}


	/**
	 * @param postId the postId to set
	 */
	public void setPostId(String postId) {
		this.postId = postId;
	}
	/**
	 * @return the fourmId
	 */
	public String getFourmId() {
		return fourmId;
	}
	/**
	 * @return the forumName
	 */
	public String getForumName() {
		return forumName;
	}
	/**
	 * @return the forumDescription
	 */
	public String getForumDescription() {
		return forumDescription;
	}
	/**
	 * @return the forumUrl
	 */
	public String getForumUrl() {
		return forumUrl;
	}
	/**
	 * @return the forumCreationDate
	 */
	public Date getForumCreationDate() {
		return forumCreationDate;
	}
	/**
	 * @return the topics
	 */
	public int getTopics() {
		return topics;
	}
	/**
	 * @return the posts
	 */
	public int getPosts() {
		return posts;
	}
	/**
	 * @return the topicId
	 */
	public String getTopicId() {
		return topicId;
	}
	/**
	 * @return the topicsubject
	 */
	public String getTopicsubject() {
		return subject;
	}
	/**
	 * @return the topicUrl
	 */
	public String getTopicUrl() {
		return topicUrl;
	}
	/**
	 * @return the rootPost
	 */
	public String getRootPost() {
		return rootPost;
	}
	/**
	 * @return the replies
	 */
	public int getReplies() {
		return replies;
	}
	/**
	 * @return the topicViews
	 */
	public int getTopicViews() {
		return views;
	}
	/**
	 * @return the firstPostDate
	 */
	public Date getFirstPostDate() {
		return firstPostDate;
	}
	/**
	 * @return the lastPostDate
	 */
	public Date getLastPostDate() {
		return lastPostDate;
	}
	/**
	 * @return the postDate
	 */
	public Date getPostDate() {
		return postDate;
	}
	/**
	 * @return the postUrl
	 */
	public String getPostUrl() {
		return postUrl;
	}
	/**
	 * @return the userUrl
	 */
	public String getUserUrl() {
		return userUrl;
	}
	/**
	 * @param fourmId the fourmId to set
	 */
	public void setFourmId(String fourmId) {
		this.fourmId = fourmId;
	}
	/**
	 * @param forumName the forumName to set
	 */
	public void setForumName(String forumName) {
		this.forumName = forumName;
	}
	/**
	 * @param forumDescription the forumDescription to set
	 */
	public void setForumDescription(String forumDescription) {
		this.forumDescription = forumDescription;
	}
	/**
	 * @param forumUrl the forumUrl to set
	 */
	public void setForumUrl(String forumUrl) {
		this.forumUrl = forumUrl;
	}
	/**
	 * @param forumCreationDate the forumCreationDate to set
	 */
	public void setForumCreationDate(Date forumCreationDate) {
		this.forumCreationDate = forumCreationDate;
	}
	/**
	 * @param topics the topics to set
	 */
	public void setTopics(int topics) {
		this.topics = topics;
	}
	/**
	 * @param posts the posts to set
	 */
	public void setPosts(int posts) {
		this.posts = posts;
	}
	/**
	 * @param topicId the topicId to set
	 */
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	/**
	 * @param topicsubject the topicsubject to set
	 */
	public void setTopicsubject(String topicsubject) {
		this.subject = topicsubject;
	}
	/**
	 * @param topicUrl the topicUrl to set
	 */
	public void setTopicUrl(String topicUrl) {
		this.topicUrl = topicUrl;
	}
	/**
	 * @param rootPost the rootPost to set
	 */
	public void setRootPost(String rootPost) {
		this.rootPost = rootPost;
	}
	/**
	 * @param replies the replies to set
	 */
	public void setReplies(int replies) {
		this.replies = replies;
	}
	/**
	 * @param topicViews the topicViews to set
	 */
	public void setTopicViews(int topicViews) {
		this.views = topicViews;
	}
	/**
	 * @param firstPostDate the firstPostDate to set
	 */
	public void setFirstPostDate(Date firstPostDate) {
		this.firstPostDate = firstPostDate;
	}
	/**
	 * @param lastPostDate the lastPostDate to set
	 */
	public void setLastPostDate(Date lastPostDate) {
		this.lastPostDate = lastPostDate;
	}
	/**
	 * @param postDate the postDate to set
	 */
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	/**
	 * @param postUrl the postUrl to set
	 */
	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}
	/**
	 * @param userUrl the userUrl to set
	 */
	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}
	
	//TODO add variables 

}
