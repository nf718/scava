package org.eclipse.scava.metricprovider.trans.bugs.index.document;


public class RedmineIssueDocument extends Document {
	
	private String issue_id;
	private Integer project_Id;
	private String tracker;
	private Integer tracker_Id;
	private Integer status_Id;
	private Integer priority_Id;
	private String category;
	private Integer category_Id;
	private String subject;
	/**
	 * @return the issue_id
	 */
	public String getIssue_id() {
		return issue_id;
	}
	/**
	 * @return the project_Id
	 */
	public Integer getProject_Id() {
		return project_Id;
	}
	/**
	 * @return the tracker
	 */
	public String getTracker() {
		return tracker;
	}
	/**
	 * @return the tracker_Id
	 */
	public Integer getTracker_Id() {
		return tracker_Id;
	}
	/**
	 * @return the status_Id
	 */
	public Integer getStatus_Id() {
		return status_Id;
	}
	/**
	 * @return the priority_Id
	 */
	public Integer getPriority_Id() {
		return priority_Id;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @return the category_Id
	 */
	public Integer getCategory_Id() {
		return category_Id;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param issue_id the issue_id to set
	 */
	public void setIssue_id(String issue_id) {
		this.issue_id = issue_id;
	}
	/**
	 * @param project_Id the project_Id to set
	 */
	public void setProject_Id(Integer project_Id) {
		this.project_Id = project_Id;
	}
	/**
	 * @param tracker the tracker to set
	 */
	public void setTracker(String tracker) {
		this.tracker = tracker;
	}
	/**
	 * @param tracker_Id the tracker_Id to set
	 */
	public void setTracker_Id(Integer tracker_Id) {
		this.tracker_Id = tracker_Id;
	}
	/**
	 * @param status_Id the status_Id to set
	 */
	public void setStatus_Id(Integer status_Id) {
		this.status_Id = status_Id;
	}
	/**
	 * @param priority_Id the priority_Id to set
	 */
	public void setPriority_Id(Integer priority_Id) {
		this.priority_Id = priority_Id;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @param category_Id the category_Id to set
	 */
	public void setCategory_Id(Integer category_Id) {
		this.category_Id = category_Id;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}


}
