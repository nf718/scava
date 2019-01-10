package org.eclipse.scava.metricprovider.trans.bugs.index.document;

import java.util.Date;
import java.util.List;

import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemComment;

public class MantisIssueDocument extends Document{
	
	String bug_id;
	String description;
	String assignee;
	String operating_system;
	String os_build;
	String platform;
	String prority;
	String reproducibility;
	String resoultion;
	String status;
	String body;
	String view_state;
	
	List<BugTrackingSystemComment> comments;
	List<String> tags;
	/**
	 * @return the bug_id
	 */
	public String getBug_id() {
		return bug_id;
	}
	/**
	 * @param bug_id the bug_id to set
	 */
	public void setBug_id(String bug_id) {
		this.bug_id = bug_id;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the assignee
	 */
	public String getAssignee() {
		return assignee;
	}
	/**
	 * @param assignee the assignee to set
	 */
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	/**
	 * @return the operating_system
	 */
	public String getOperating_system() {
		return operating_system;
	}
	/**
	 * @param operating_system the operating_system to set
	 */
	public void setOperating_system(String operating_system) {
		this.operating_system = operating_system;
	}
	/**
	 * @return the os_build
	 */
	public String getOs_build() {
		return os_build;
	}
	/**
	 * @param os_build the os_build to set
	 */
	public void setOs_build(String os_build) {
		this.os_build = os_build;
	}
	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}
	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	/**
	 * @return the prority
	 */
	public String getPrority() {
		return prority;
	}
	/**
	 * @param prority the prority to set
	 */
	public void setPrority(String prority) {
		this.prority = prority;
	}
	/**
	 * @return the reproducibility
	 */
	public String getReproducibility() {
		return reproducibility;
	}
	/**
	 * @param reproducibility the reproducibility to set
	 */
	public void setReproducibility(String reproducibility) {
		this.reproducibility = reproducibility;
	}
	/**
	 * @return the resoultion
	 */
	public String getResoultion() {
		return resoultion;
	}
	/**
	 * @param resoultion the resoultion to set
	 */
	public void setResoultion(String resoultion) {
		this.resoultion = resoultion;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the view_state
	 */
	public String getView_state() {
		return view_state;
	}
	/**
	 * @param view_state the view_state to set
	 */
	public void setView_state(String view_state) {
		this.view_state = view_state;
	}
	/**
	 * @return the comments
	 */
	public List<BugTrackingSystemComment> getComments() {
		return comments;
	}
	/**
	 * @param list the comments to set
	 */
	public void setComments(List<BugTrackingSystemComment> list) {
		this.comments = list;
	}
	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
