/*******************************************************************************
 * Copyright (c) 2017 University of Manchester
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.scava.metricprovider.trans.requestreplyclassification;

import java.util.Arrays;
import java.util.List;

import org.eclipse.scava.metricprovider.trans.detectingcode.DetectingCodeTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.BugTrackerCommentDetectingCode;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.DetectingCodeTransMetric;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.NewsgroupArticleDetectingCode;
import org.eclipse.scava.metricprovider.trans.requestreplyclassification.model.BugTrackerComments;
import org.eclipse.scava.metricprovider.trans.requestreplyclassification.model.NewsgroupArticles;
import org.eclipse.scava.metricprovider.trans.requestreplyclassification.model.RequestReplyClassificationTransMetric;
import org.eclipse.scava.nlp.predictionmanager.Prediction;
import org.eclipse.scava.nlp.requestreplydetector.RequestReplyDetector;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.ITransientMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;
import org.eclipse.scava.platform.delta.ProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.PlatformBugTrackingSystemManager;
import org.eclipse.scava.platform.delta.communicationchannel.PlatformCommunicationChannelManager;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.Project;
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
		Iterable<BugTrackerCommentDetectingCode> commentsIt = detectingCodeMetric.getBugTrackerComments();
		
		for(BugTrackerCommentDetectingCode comment : commentsIt)
		{
			BugTrackerComments commentInRequestReply = findBugTrackerComment(db, comment);
			if(commentInRequestReply == null)
			{
				commentInRequestReply = new BugTrackerComments();
				commentInRequestReply.setBugTrackerId(comment.getBugTrackerId());
				commentInRequestReply.setBugId(comment.getBugId());
				commentInRequestReply.setCommentId(comment.getCommentId());
				//This information is never used hopefully
				//commentInRequestReply.setDate(new Date(comment.getCreationTime()).toString());
				db.getBugTrackerComments().add(commentInRequestReply);
			}
			applyRequestReplyDetector(comment.getNaturalLanguage(), commentInRequestReply);
			db.sync();
		}
		
		Iterable<NewsgroupArticleDetectingCode> articlesIt = detectingCodeMetric.getNewsgroupArticles();
		
		for (NewsgroupArticleDetectingCode article : articlesIt)
		{
			NewsgroupArticles articleDataInRequestReply = findNewsgroupArticle(db, article);
			if(articleDataInRequestReply == null)
			{
				articleDataInRequestReply = new NewsgroupArticles();
				articleDataInRequestReply.setNewsgroupName(article.getNewsGroupName());
				articleDataInRequestReply.setArticleNumber(article.getArticleNumber());
				//This information is never used hopefully
				//articleDataInRequestReply.setDate(new Date(comment.getCreationTime()).toString());
				db.getNewsgroupArticles().add(articleDataInRequestReply);
			}
			applyRequestReplyDetector(article.getNaturalLanguage(), articleDataInRequestReply);
			db.sync();
		}
		
 	}
	
	private void applyRequestReplyDetector(String naturalLanguage, BugTrackerComments comment)
	{
		comment.setClassificationResult(applyRequestReplyDetector(naturalLanguage));
	}
	
	private void applyRequestReplyDetector(String naturalLanguage, NewsgroupArticles article)
	{
		article.setClassificationResult(applyRequestReplyDetector(naturalLanguage));
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
	
	private BugTrackerComments findBugTrackerComment(RequestReplyClassificationTransMetric db, BugTrackerCommentDetectingCode comment)
	{
		BugTrackerComments bugTrackerCommentsData = null;
		Iterable<BugTrackerComments> bugTrackerCommentsDataIt = 
		db.getBugTrackerComments().
			find(BugTrackerComments.BUGID.eq(comment.getBugId()),
					BugTrackerComments.COMMENTID.eq(comment.getCommentId()));
		for (BugTrackerComments bcd:  bugTrackerCommentsDataIt) {
			bugTrackerCommentsData = bcd;
		}
		return bugTrackerCommentsData;
	}
	
	private NewsgroupArticles findNewsgroupArticle(RequestReplyClassificationTransMetric db, 
			NewsgroupArticleDetectingCode article) {
		
		NewsgroupArticles newsgroupArticles = null;
		Iterable<NewsgroupArticles> newsgroupArticlesIt = 
				db.getNewsgroupArticles().
						find(NewsgroupArticles.NEWSGROUPNAME.eq(article.getNewsGroupName()), 
								NewsgroupArticles.ARTICLENUMBER.eq(article.getArticleNumber()));
		for (NewsgroupArticles nad:  newsgroupArticlesIt)
		{
			newsgroupArticles = nad;
		}
		return newsgroupArticles;
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
