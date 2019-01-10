package org.eclipse.scava.platform.bugtrackingsystem.mantis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scava.repository.model.mantis.*;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemBug;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemComment;
import org.eclipse.scava.repository.model.BugTrackingSystem;

public class MantisIssue extends BugTrackingSystemBug {
	
	private static final long serialVersionUID = 1L;
	

	String description;
	Date updated_at;
	String handler;
	String view_state;
	String reproducibility;
	String platform;
	String os_build;
	List<BugTrackingSystemComment> notes;
	List<String> tags;
	/**
	 * @return the sticky
	 */

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
	 * @return the updated_at
	 */
	public Date getUpdated_at() {
		return updated_at;
	}
	/**
	 * @param updated_at the updated_at to set
	 */
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	/**
	 * @return the handler
	 */
	public String getHandler() {
		return handler;
	}
	/**
	 * @param handler the handler to set
	 */
	public void setHandler(String handler) {
		this.handler = handler;
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

	public List<BugTrackingSystemComment> getNotes() {
		return notes;
	}
	public void setNotes(List<BugTrackingSystemComment> notes) {
		this.notes = notes;
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