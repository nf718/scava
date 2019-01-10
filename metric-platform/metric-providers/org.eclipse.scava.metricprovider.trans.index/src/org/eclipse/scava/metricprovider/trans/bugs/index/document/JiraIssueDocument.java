package org.eclipse.scava.metricprovider.trans.bugs.index.document;

import java.util.Date;
import java.util.Set;

public class JiraIssueDocument extends Document {

	private String key;
    private String url;
    private String summary;
    private String issue_type; // id
    private String expandables;
    private String assignee; // id
    private Integer num_watchers;
    private Set<String> sub_tasks;
    private Set<String> comments;
    private Set<String> labels;
    private Set<String> affected_versions;
    private Set<String> fix_versions;
    private Date due_date;
    private Date resolution_date;
    
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @return the issueType
	 */
	public String getIssueType() {
		return issue_type;
	}
	/**
	 * @return the expandables
	 */
	public String getExpandables() {
		return expandables;
	}
	/**
	 * @return the assignee
	 */
	public String getAssignee() {
		return assignee;
	}
	/**
	 * @return the numWatchers
	 */
	public Integer getNumWatchers() {
		return num_watchers;
	}
	/**
	 * @return the subTasks
	 */
	public Set<String> getSubTasks() {
		return sub_tasks;
	}
	/**
	 * @return the labels
	 */
	public Set<String> getLabels() {
		return labels;
	}
	/**
	 * @return the affectedVersions
	 */
	public Set<String> getAffectedVersions() {
		return affected_versions;
	}
	/**
	 * @return the fixVersions
	 */
	public Set<String> getFixVersions() {
		return fix_versions;
	}
	/**
	 * @return the dueDate
	 */
	public Date getDueDate() {
		return due_date;
	}
	/**
	 * @return the resolutionDate
	 */
	public Date getResolutionDate() {
		return resolution_date;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	/**
	 * @param issueType the issueType to set
	 */
	public void setIssueType(String issueType) {
		this.issue_type = issueType;
	}
	/**
	 * @param expandables the expandables to set
	 */
	public void setExpandables(String expandables) {
		this.expandables = expandables;
	}
	/**
	 * @param assignee the assignee to set
	 */
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	/**
	 * @param numWatchers the numWatchers to set
	 */
	public void setNumWatchers(Integer numWatchers) {
		this.num_watchers = numWatchers;
	}
	/**
	 * @param subTasks the subTasks to set
	 */
	public void setSubTasks(Set<String> subTasks) {
		this.sub_tasks = subTasks;
	}
	/**
	 * @param labels the labels to set
	 */
	public void setLabels(Set<String> labels) {
		this.labels = labels;
	}
	/**
	 * @param affectedVersions the affectedVersions to set
	 */
	public void setAffectedVersions(Set<String> affectedVersions) {
		this.affected_versions = affectedVersions;
	}
	/**
	 * @param fixVersions the fixVersions to set
	 */
	public void setFixVersions(Set<String> fixVersions) {
		this.fix_versions = fixVersions;
	}
	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(Date dueDate) {
		this.due_date = dueDate;
	}
	/**
	 * @param resolutionDate the resolutionDate to set
	 */
	public void setResolutionDate(Date resolutionDate) {
		this.resolution_date = resolutionDate;
	}
	/**
	 * @return the comments
	 */
	public Set<String> getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(Set<String> comments) {
		this.comments = comments;
	}

}
