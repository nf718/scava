package org.eclipse.scava.platform.bugtrackingsystem.gitlab;

import org.eclipse.scava.platform.Date;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemBug;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemComment;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.IBugTrackingSystemManager;
import org.eclipse.scava.repository.model.BugTrackingSystem;
import org.eclipse.scava.repository.model.bts.gitlab.GitLabTracker;

import com.mongodb.DB;

public class GitLabManager implements IBugTrackingSystemManager<GitLabTracker>{

	@Override
	public boolean appliesTo(BugTrackingSystem bugTrackingSystem) {
		// TODO Auto-generated method stub
		return bugTrackingSystem instanceof GitLabTracker;
	}

	@Override
	public BugTrackingSystemDelta getDelta(DB db, GitLabTracker bugTrackingSystem, Date date) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getFirstDate(DB db, GitLabTracker bugTrackingSystem) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContents(DB db, GitLabTracker bugTrackingSystem, BugTrackingSystemBug bug) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContents(DB db, GitLabTracker bugTrackingSystem, BugTrackingSystemComment comment) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	/*
	 * TESTING 
	 */
	public static void main(String[] args) {
		
	}

}