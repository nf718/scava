/*******************************************************************************
 * Copyright (c) 2018 University of York
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.scava.platform.delta.communicationchannel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.platform.Date;
import org.eclipse.scava.platform.delta.NoManagerFoundException;
import org.eclipse.scava.platform.delta.bugtrackingsystem.IBugTrackingSystemManager;
import org.eclipse.scava.repository.model.BugTrackingSystem;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.Project;

import com.mongodb.DB;


public class CommunicationChannelProjectDelta {
	
	protected List<CommunicationChannelDelta> communicationChannelDeltas = new ArrayList<CommunicationChannelDelta>();
	
	
	
	
	
	/**
	 * 	public BugTrackingSystemProjectDelta(DB db, Project project, Date date, 
			IBugTrackingSystemManager bugTrackingSystemManager) throws Exception {
		for (BugTrackingSystem bugTrackingSystem : project.getBugTrackingSystems()) {
			try {
				bugTrackingSystemDeltas.add(bugTrackingSystemManager.getDelta(db, bugTrackingSystem, date));
			} catch (NoManagerFoundException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	 * 
	 * 
	 * @param db
	 * @param project
	 * @param date
	 * @param communicationChannelManager
	 * @throws Exception
	 */
	
	public CommunicationChannelProjectDelta(DB db, Project project, Date date, ICommunicationChannelManager communicationChannelManager) throws Exception {
		
		for (CommunicationChannel communicationChannel: project.getCommunicationChannels()) {
			try {
				
				communicationChannelDeltas.add(communicationChannelManager.getDelta(db, communicationChannel, date));
				
				
				
			} catch (NoManagerFoundException e) {
				System.err.println(e.getMessage());
			}
			
			
	
		}
	}
	
	public List<CommunicationChannelDelta> getCommunicationChannelSystemDeltas() {
		return communicationChannelDeltas;
	}
	
	public void setRepoDeltas(List<CommunicationChannelDelta> communicationChannelDeltas) {
		this.communicationChannelDeltas = communicationChannelDeltas;
	}
}
