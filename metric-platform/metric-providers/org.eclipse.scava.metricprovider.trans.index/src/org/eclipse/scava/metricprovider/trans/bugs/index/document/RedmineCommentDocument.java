package org.eclipse.scava.metricprovider.trans.bugs.index.document;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.platform.bugtrackingsystem.redmine.api.RedmineCommentDetails;

public class RedmineCommentDocument extends Document {
	
	private Integer creator_id;
	private List<RedmineCommentDetails> details;
	/**
	 * @return the creatorId
	 */
	public Integer getCreatorId() {
		return creator_id;
	}
	/**
	 * @return the details
	 */
	public List<RedmineCommentDetails> getDetails() {
		return details;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(Integer creatorId) {
		this.creator_id = creatorId;
	}
	/**
	 * @param details the details to set
	 */
	public void setDetails(List<RedmineCommentDetails> details) {
		this.details = details;
	}

}
