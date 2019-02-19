/*******************************************************************************
 * Copyright (c) 2018 Edge Hill University
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.scava.platform.bugtrackingsystem.github.utils;
import java.util.Map;

import org.eclipse.scava.workflow.restmule.core.session.ISession;
import org.eclipse.scava.workflow.restmule.generated.client.github.api.GitHubApi;
import org.eclipse.scava.workflow.restmule.generated.client.github.api.IGitHubApi;
import org.eclipse.scava.workflow.restmule.generated.client.github.cache.GitHubCacheManager;
import org.eclipse.scava.workflow.restmule.generated.client.github.session.GitHubSession;

public class GitHubSessionUtil {

	private static ISession session;
	private static IGitHubApi gitHubApi;
	private static String clientName = "GitHub";

	
	//basic authentication using username and password
	public GitHubSessionUtil(Map<String, String> AuthentificationData) {
		// creates a new instance of GitHubSession using basic authentication
		if (AuthentificationData.get("SECURITY_TYPE").equals("password"))
		{
			session = GitHubSession.createWithBasicAuth(AuthentificationData.get("LOGIN"), AuthentificationData.get("PASSWORD"));
		}
		else if(AuthentificationData.get("SECURITY_TYPE").equals("token"))
		{
			session = GitHubSession.createWithBasicAuth(AuthentificationData.get("TOKEN"));
		}
		else
		{
			System.err.println("No authentification data has ben set");
		}
		
		if (session != null) {
			// creates a new gitHub Api that is associated with the session
			gitHubApi = GitHubApi.create().setSession(session).setActiveCaching(false).build();
			System.out.println("INFO : " + clientName + " Session Created.");
		} else {

			System.err.println("Something went wrong during the creation of a Git Hub Session");

		}
	}
	
	public IGitHubApi getSession(){
		
		return gitHubApi;
	}
	
	public static void clearGitHubCache(){
		GitHubCacheManager.getInstance().clear();
		// TODO add to log
		System.err.println( clientName + " Cache Cleared!");;
	}
	

}
