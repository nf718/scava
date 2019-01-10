/*******************************************************************************
 * Copyright (c) 2018 University of York
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.scava.platform.cache.communicationchannel;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelArticle;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelForumPost;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelTopic;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

public class CommunicationChannelContentsCache implements ICommunicationChannelContentsCache {
	
	protected HTreeMap<CommunicationChannelArticle, String> articleMap;
	protected HTreeMap<CommunicationChannelTopic, String> topicMap;
	protected HTreeMap<CommunicationChannelForumPost, String> forumPostMap;
	
	public CommunicationChannelContentsCache() {
		initialiseDB();
	}
	
	protected void initialiseDB() {
		 articleMap = DBMaker.newTempHashMap();
		 topicMap = DBMaker.newTempHashMap();
		 forumPostMap = DBMaker.newTempHashMap();
		 
	}
	
	@Override
	public String getCachedContents(CommunicationChannelArticle article){
		return articleMap.get(article);
	}
	
	@Override
	public String getCachedContents(CommunicationChannelTopic topic){
		return topicMap.get(topic);
	}
	
	@Override
	public String getCachedContents(CommunicationChannelForumPost post){
		return forumPostMap.get(post);
	}
	
	
	
	
	
	
	@Override
	public void putContents(CommunicationChannelArticle article, String contents) {
		articleMap.put(article, contents);
	}
	
	@Override
	public void putContents(CommunicationChannelTopic topic, String contents) {
		topicMap.put(topic, contents);
	}
	
	@Override
	public void putContents(CommunicationChannelForumPost forum, String contents) {
		forumPostMap.put(forum, contents);
	}
}
