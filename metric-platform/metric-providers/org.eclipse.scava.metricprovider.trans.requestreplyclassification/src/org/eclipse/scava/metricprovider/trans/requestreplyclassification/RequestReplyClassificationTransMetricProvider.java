/*******************************************************************************
 * Copyright (c) 2017 University of Manchester
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
//Adrián was here
package org.eclipse.scava.metricprovider.trans.requestreplyclassification;

import java.util.Arrays;
import java.util.List;

import org.eclipse.scava.metricprovider.trans.detectingcode.DetectingCodeTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.BugTrackerCommentDetectingCode;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.DetectingCodeTransMetric;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.ForumPostDetectingCode;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.NewsgroupArticleDetectingCode;
import org.eclipse.scava.metricprovider.trans.requestreplyclassification.model.BugTrackerComments;
import org.eclipse.scava.metricprovider.trans.requestreplyclassification.model.ForumsPosts;
import org.eclipse.scava.metricprovider.trans.requestreplyclassification.model.NewsgroupArticles;
import org.eclipse.scava.metricprovider.trans.requestreplyclassification.model.RequestReplyClassificationTransMetric;
import org.eclipse.scava.nlp.requestreplydetector.RequestReplyDetector;
import org.eclipse.scava.nlp.tools.other.predictionmanager.Prediction;
import org.eclipse.scava.platform.Date;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.ITransientMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;
import org.eclipse.scava.platform.delta.ProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemComment;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.PlatformBugTrackingSystemManager;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelForumPost;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelArticle;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelDelta;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelProjectDelta;
import org.eclipse.scava.platform.delta.communicationchannel.PlatformCommunicationChannelManager;
import org.eclipse.scava.repository.model.BugTrackingSystem;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum;
import org.eclipse.scava.repository.model.cc.nntp.NntpNewsGroup;
import org.eclipse.scava.repository.model.sourceforge.Discussion;

import com.mongodb.DB;

public class RequestReplyClassificationTransMetricProvider  implements ITransientMetricProvider<RequestReplyClassificationTransMetric>{

	protected PlatformBugTrackingSystemManager platformBugTrackingSystemManager;
	protected PlatformCommunicationChannelManager communicationChannelManager;
	
	protected List<IMetricProvider> uses;
	protected MetricProviderContext context;

	@Override
	public String getIdentifier() {
		return RequestReplyClassificationTransMetricProvider.class.getCanonicalName();
	}

	@Override
	public boolean appliesTo(Project project) {
		for (CommunicationChannel communicationChannel: project.getCommunicationChannels()) {
			if (communicationChannel instanceof NntpNewsGroup) return true;
			if (communicationChannel instanceof Discussion) return true;
			if (communicationChannel instanceof EclipseForum) return true;
		}
		return !project.getBugTrackingSystems().isEmpty();	   
	}

	@Override
	public void setUses(List<IMetricProvider> uses) {
		this.uses=uses;
	}

	@Override
	public List<String> getIdentifiersOfUses() {
		return Arrays.asList(DetectingCodeTransMetricProvider.class.getCanonicalName());
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.context=context;
		this.platformBugTrackingSystemManager = context.getPlatformBugTrackingSystemManager();
		this.communicationChannelManager = context.getPlatformCommunicationChannelManager();
	}

	@Override
	public RequestReplyClassificationTransMetric adapt(DB db) {
		return new RequestReplyClassificationTransMetric(db);
	}
	
	@Override
	public void measure(Project project, ProjectDelta projectDelta, RequestReplyClassificationTransMetric db)
	{
		
		clearDB(db);
		DetectingCodeTransMetric detectingCodeMetric = ((DetectingCodeTransMetricProvider)uses.get(0)).adapt(context.getProjectDB(project));
		
		BugTrackingSystemProjectDelta btspDelta = projectDelta.getBugTrackingSystemDelta();
		for (BugTrackingSystemDelta bugTrackingSystemDelta : btspDelta.getBugTrackingSystemDeltas())
		{
			BugTrackingSystem bugTracker = bugTrackingSystemDelta.getBugTrackingSystem();
			for (BugTrackingSystemComment comment: bugTrackingSystemDelta.getComments())
			{
				BugTrackerComments commentInRequestReply = findBugTrackerComment(db, comment);
				if(commentInRequestReply == null)
				{
					commentInRequestReply = new BugTrackerComments();
					commentInRequestReply.setBugTrackerId(bugTracker.getOSSMeterId());
					commentInRequestReply.setBugId(comment.getBugId());
					commentInRequestReply.setCommentId(comment.getCommentId());
					commentInRequestReply.setDate(new Date(comment.getCreationTime()).toString());
					db.getBugTrackerComments().add(commentInRequestReply);
				}
				applyRequestReplyDetector(commentInRequestReply, detectingCodeMetric);
				db.sync();
			}
			
		}
		
		
		CommunicationChannelProjectDelta ccpDelta = projectDelta.getCommunicationChannelDelta();
		for ( CommunicationChannelDelta communicationChannelDelta: ccpDelta.getCommunicationChannelSystemDeltas())
		{
			CommunicationChannel communicationChannel = communicationChannelDelta.getCommunicationChannel();
			//Process for forums
			if(communicationChannel instanceof EclipseForum)
			{
				for(CommunicationChannelForumPost post : communicationChannelDelta.getPosts())
				{
					ForumsPosts postsDataInRequestReply = findForumPost(db, post);
					if(postsDataInRequestReply == null)
					{
						postsDataInRequestReply = new ForumsPosts();
						postsDataInRequestReply.setTopicId(post.getForumId());
						postsDataInRequestReply.setPostId(post.getPostId());
						db.getForumPosts().add(postsDataInRequestReply);
					}
					applyRequestReplyDetector(postsDataInRequestReply, detectingCodeMetric);
					db.sync();
				}
			}
			else
			{
				String communicationChannelName;
				if (!(communicationChannel instanceof NntpNewsGroup))
					communicationChannelName = communicationChannel.getUrl();
				else {
					NntpNewsGroup newsgroup = (NntpNewsGroup) communicationChannel;
					communicationChannelName = newsgroup.getNewsGroupName();
				}
				for (CommunicationChannelArticle article: communicationChannelDelta.getArticles())
				{
					NewsgroupArticles articleDataInRequestReply = findNewsgroupArticle(db,communicationChannelName, article);
					if(articleDataInRequestReply == null)
					{
						articleDataInRequestReply = new NewsgroupArticles();
						articleDataInRequestReply.setNewsgroupName(communicationChannelName);
						articleDataInRequestReply.setArticleNumber(article.getArticleNumber());
						articleDataInRequestReply.setDate(new Date(article.getDate()).toString());
						db.getNewsgroupArticles().add(articleDataInRequestReply);
					}
					applyRequestReplyDetector(articleDataInRequestReply, communicationChannelName, detectingCodeMetric);
					db.sync();
				}
			}
		}
		
 	}
	
	

	private String naturalLanguageBugTrackerComment(DetectingCodeTransMetric db, BugTrackerComments comment) {
		BugTrackerCommentDetectingCode bugtrackerCommentInDetectionCode = null;
		Iterable<BugTrackerCommentDetectingCode> bugtrackerCommentIt = db.getBugTrackerComments().
				find(BugTrackerCommentDetectingCode.BUGTRACKERID.eq(comment.getBugTrackerId()),
						BugTrackerCommentDetectingCode.BUGID.eq(comment.getBugId()),
						BugTrackerCommentDetectingCode.COMMENTID.eq(comment.getCommentId()));
		for (BugTrackerCommentDetectingCode btcdc:  bugtrackerCommentIt) {
			bugtrackerCommentInDetectionCode = btcdc;
		}
		if(bugtrackerCommentInDetectionCode.getNaturalLanguage() == null)
			return "";
		else
			return bugtrackerCommentInDetectionCode.getNaturalLanguage();
	}
	
	private String naturalLanguageNewsgroupArticle(DetectingCodeTransMetric db, String communicationChannelName, NewsgroupArticles article) {
		NewsgroupArticleDetectingCode newsgroupArticleInDetectionCode = null;
		Iterable<NewsgroupArticleDetectingCode> newsgroupArticleIt = db.getNewsgroupArticles().
				find(NewsgroupArticleDetectingCode.NEWSGROUPNAME.eq(communicationChannelName),
						NewsgroupArticleDetectingCode.ARTICLENUMBER.eq(article.getArticleNumber()));
		for (NewsgroupArticleDetectingCode nadc:  newsgroupArticleIt) {
			newsgroupArticleInDetectionCode = nadc;
		}
		if(newsgroupArticleInDetectionCode.getNaturalLanguage() == null)
			return "";
		else
			return newsgroupArticleInDetectionCode.getNaturalLanguage();
	}
	
	private String naturalLanguageForumPost(DetectingCodeTransMetric db, ForumsPosts post) {
		ForumPostDetectingCode forumPostInDetectionCode = null;
		Iterable<ForumPostDetectingCode> forumPostIt = db.getForumPosts().
				find(ForumPostDetectingCode.TOPICID.eq(post.getTopicId()),
						ForumPostDetectingCode.POSTID.eq(post.getPostId()));
		for (ForumPostDetectingCode fpdc:  forumPostIt) {
			forumPostInDetectionCode = fpdc;
		}
		if(forumPostInDetectionCode.getNaturalLanguage() == null)
			return "";
		else
			return forumPostInDetectionCode.getNaturalLanguage();
	}
	
	
	private void applyRequestReplyDetector(BugTrackerComments comment, DetectingCodeTransMetric db)
	{
		comment.setClassificationResult(applyRequestReplyDetector(naturalLanguageBugTrackerComment(db, comment)));
	}
	
	private void applyRequestReplyDetector(NewsgroupArticles article, String communicationChannelName, DetectingCodeTransMetric db)
	{
		article.setClassificationResult(applyRequestReplyDetector(naturalLanguageNewsgroupArticle(db, communicationChannelName, article)));
	}
	
	private void applyRequestReplyDetector(ForumsPosts post, DetectingCodeTransMetric db)
	{
		post.setClassificationResult(applyRequestReplyDetector(naturalLanguageForumPost(db, post)));
	}
	
	private String applyRequestReplyDetector(String naturalLanguage)
	{
		Prediction prediction = RequestReplyDetector.predict(naturalLanguage);
		if(prediction.getLabel().equals("__label__Question"))
			return "Request";
		else if(prediction.getLabel().equals("__label__Reply"))
			return "Reply";			
		return null;
	}
	
	private BugTrackerComments findBugTrackerComment(RequestReplyClassificationTransMetric db, BugTrackingSystemComment comment)
	{
		BugTrackerComments bugTrackerCommentsData = null;
		Iterable<BugTrackerComments> bugTrackerCommentsDataIt = 
		db.getBugTrackerComments().
			find(BugTrackerComments.BUGID.eq(comment.getBugId()),
					BugTrackerComments.COMMENTID.eq(comment.getCommentId()));
		for (BugTrackerComments bcrr:  bugTrackerCommentsDataIt) {
			bugTrackerCommentsData = bcrr;
		}
		return bugTrackerCommentsData;
	}
	
	private NewsgroupArticles findNewsgroupArticle(RequestReplyClassificationTransMetric db, String communicationChannelName,
			CommunicationChannelArticle article) {
		
		NewsgroupArticles newsgroupArticles = null;
		Iterable<NewsgroupArticles> newsgroupArticlesIt = 
				db.getNewsgroupArticles().
						find(NewsgroupArticles.NEWSGROUPNAME.eq(communicationChannelName), 
								NewsgroupArticles.ARTICLENUMBER.eq(article.getArticleNumber()));
		for (NewsgroupArticles narr:  newsgroupArticlesIt)
		{
			newsgroupArticles = narr;
		}
		return newsgroupArticles;
	}
	
	private ForumsPosts findForumPost(RequestReplyClassificationTransMetric db, CommunicationChannelForumPost post) {
		ForumsPosts forumPosts = null;
		Iterable<ForumsPosts> forumPostsIt = 
				db.getForumPosts().
						find(ForumsPosts.TOPICID.eq(post.getForumId()), 
								ForumsPosts.POSTID.eq(post.getPostId()));
		for (ForumsPosts fprr:  forumPostsIt)
		{
			forumPosts = fprr;
		}
		return forumPosts;
	}

	private void clearDB(RequestReplyClassificationTransMetric db) {
		db.getBugTrackerComments().getDbCollection().drop();
	}

	@Override
	public String getShortIdentifier() {
		return "requestreplyclassification";
	}

	@Override
	public String getFriendlyName() {
		return "Request Reply Classification";
	}

	@Override
	public String getSummaryInformation() {
		return "This metric computes if each bug comment or newsgroup article is a " +
				"request of a reply.";
	}

}
