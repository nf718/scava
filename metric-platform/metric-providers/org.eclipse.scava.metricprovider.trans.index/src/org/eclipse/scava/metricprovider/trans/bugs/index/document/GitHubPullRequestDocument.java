package org.eclipse.scava.metricprovider.trans.bugs.index.document;

import java.util.Date;

public class GitHubPullRequestDocument extends Document{
	String pull_request_id;
	Boolean mergable;
	Boolean is_merged;
	Date closed_at;
	Date merged_at;
	String title;
	int additions;
	int number_of_comments;
	int number_of_commits;
	int number_of_deletions;
	int number;
	int milestone;
	String diff_url;
	String html_url;
	String issue_url;
	String patch_url;
	String state;
	String url;
	String merged_by;

	/**
	 * @return the pull_request_id
	 */
	public String getPull_request_id() {
		return pull_request_id;
	}
	/**
	 * @return the closed_at
	 */
	public Date getClosed_at() {
		return closed_at;
	}
	/**
	 * @return the merged_at
	 */
	public Date getMerged_at() {
		return merged_at;
	}
	/**
	 * @return the number_of_comments
	 */
	public int getNumber_of_comments() {
		return number_of_comments;
	}
	/**
	 * @return the number_of_commits
	 */
	public int getNumber_of_commits() {
		return number_of_commits;
	}
	/**
	 * @return the number_of_deletions
	 */
	public int getNumber_of_deletions() {
		return number_of_deletions;
	}
	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * @return the milestone
	 */
	public int getMilestone() {
		return milestone;
	}
	/**
	 * @return the diff_url
	 */
	public String getDiff_url() {
		return diff_url;
	}
	/**
	 * @return the html_url
	 */
	public String getHtml_url() {
		return html_url;
	}
	/**
	 * @return the issue_url
	 */
	public String getIssue_url() {
		return issue_url;
	}
	/**
	 * @return the patch_url
	 */
	public String getPatch_url() {
		return patch_url;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @return the merged_by
	 */
	public String getMerged_by() {
		return merged_by;
	}
	/**
	 * @param pull_request_id the pull_request_id to set
	 */
	public void setPull_request_id(String pull_request_id) {
		this.pull_request_id = pull_request_id;
	}
	/**
	 * @return the is_merged
	 */
	public Boolean getIs_merged() {
		return is_merged;
	}
	/**
	 * @param is_merged the is_merged to set
	 */
	public void setIs_merged(Boolean is_merged) {
		this.is_merged = is_merged;
	}
	/**
	 * @param closed_at the closed_at to set
	 */
	public void setClosed_at(Date closed_at) {
		this.closed_at = closed_at;
	}
	/**
	 * @param merged_at the merged_at to set
	 */
	public void setMerged_at(Date merged_at) {
		this.merged_at = merged_at;
	}

	/**
	 * @return the additions
	 */
	public int getAdditions() {
		return additions;
	}
	/**
	 * @param additions the additions to set
	 */
	public void setAdditions(int additions) {
		this.additions = additions;
	}
	/**
	 * @param number_of_comments the number_of_comments to set
	 */
	public void setNumber_of_comments(int number_of_comments) {
		this.number_of_comments = number_of_comments;
	}
	/**
	 * @param number_of_commits the number_of_commits to set
	 */
	public void setNumber_of_commits(int number_of_commits) {
		this.number_of_commits = number_of_commits;
	}
	/**
	 * @param number_of_deletions the number_of_deletions to set
	 */
	public void setNumber_of_deletions(int number_of_deletions) {
		this.number_of_deletions = number_of_deletions;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	/**
	 * @param milestone the milestone to set
	 */
	public void setMilestone(int milestone) {
		this.milestone = milestone;
	}
	/**
	 * @param diff_url the diff_url to set
	 */
	public void setDiff_url(String diff_url) {
		this.diff_url = diff_url;
	}
	/**
	 * @param html_url the html_url to set
	 */
	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}
	/**
	 * @param issue_url the issue_url to set
	 */
	public void setIssue_url(String issue_url) {
		this.issue_url = issue_url;
	}
	/**
	 * @param patch_url the patch_url to set
	 */
	public void setPatch_url(String patch_url) {
		this.patch_url = patch_url;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @param merged_by the merged_by to set
	 */
	public void setMerged_by(String merged_by) {
		this.merged_by = merged_by;
	}
	/**
	 * @return the mergable
	 */
	public Boolean getMergable() {
		return mergable;
	}
	/**
	 * @param mergable the mergable to set
	 */
	public void setMergable(Boolean mergable) {
		this.mergable = mergable;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


}
