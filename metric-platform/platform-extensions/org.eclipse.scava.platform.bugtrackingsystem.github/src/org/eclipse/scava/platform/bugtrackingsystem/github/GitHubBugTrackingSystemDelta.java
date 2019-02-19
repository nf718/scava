/*******************************************************************************
 * Copyright (c) 2018 Edge Hill University
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.scava.platform.bugtrackingsystem.github;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemDelta;

public class GitHubBugTrackingSystemDelta extends BugTrackingSystemDelta {

	private static final long serialVersionUID = 1L;

	private List<GitHubPullRequest> pullRequests = new ArrayList<GitHubPullRequest>();
	private List<GitHubComment> pullRequestComments = new ArrayList<GitHubComment>();
	
	public List<GitHubPullRequest> getPullRequests() {
		return pullRequests;
	}
	
	public List<GitHubComment> getPullRequestsComments() {
		return pullRequestComments;
	}
	
}
