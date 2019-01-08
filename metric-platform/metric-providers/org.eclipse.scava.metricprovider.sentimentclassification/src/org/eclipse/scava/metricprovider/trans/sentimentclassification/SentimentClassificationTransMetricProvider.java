/*******************************************************************************
 * Copyright (c) 2017 University of Manchester
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.scava.metricprovider.trans.sentimentclassification;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.eclipse.scava.metricprovider.trans.detectingcode.DetectingCodeTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.BugTrackerCommentDetectingCode;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.DetectingCodeTransMetric;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.NewsgroupArticleDetectingCode;
import org.eclipse.scava.metricprovider.trans.sentimentclassification.model.BugTrackerCommentsData;
import org.eclipse.scava.metricprovider.trans.sentimentclassification.model.NewsgroupArticlesData;
import org.eclipse.scava.metricprovider.trans.sentimentclassification.model.SentimentClassificationTransMetric;
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
import org.eclipse.scava.sentimentclassifier.opennlptartarus.libsvm.ClassificationInstance;
import org.eclipse.scava.sentimentclassifier.opennlptartarus.libsvm.Classifier;
import org.eclipse.scava.sentimentclassifier.opennlptartarus.libsvm.EmotionalDimensions;

import com.mongodb.DB;

public class SentimentClassificationTransMetricProvider  implements ITransientMetricProvider<SentimentClassificationTransMetric>{

	protected PlatformBugTrackingSystemManager platformBugTrackingSystemManager;
	protected PlatformCommunicationChannelManager communicationChannelManager;
	
	protected List<IMetricProvider> uses;
	protected MetricProviderContext context;

	@Override
	public String getIdentifier() {
		return SentimentClassificationTransMetricProvider.class.getCanonicalName();
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
		this.platformBugTrackingSystemManager = context.getPlatformBugTrackingSystemManager();
		this.communicationChannelManager = context.getPlatformCommunicationChannelManager();
		this.context = context;
	}

	@Override
	public SentimentClassificationTransMetric adapt(DB db) {
		return new SentimentClassificationTransMetric(db);
	}
	
	@Override
	public void measure(Project project, ProjectDelta projectDelta, 
							SentimentClassificationTransMetric db) {
		final long startTime = System.currentTimeMillis();
		long previousTime = startTime;
		clearDB(db);
		System.err.println("Started " + getIdentifier());
		Classifier classifier = new Classifier();
		
		DetectingCodeTransMetric detectingCodeMetric = ((DetectingCodeTransMetricProvider)uses.get(0)).adapt(context.getProjectDB(project));
		Iterable<BugTrackerCommentDetectingCode> commentsIt = detectingCodeMetric.getBugTrackerComments();
		
		for(BugTrackerCommentDetectingCode comment : commentsIt)
		{
			BugTrackerCommentsData commentInSentiment = findBugTrackerComment(db, comment);
			if(commentInSentiment == null)
			{
				commentInSentiment = new BugTrackerCommentsData();
				commentInSentiment.setBugTrackerId(comment.getBugTrackerId());
				commentInSentiment.setBugId(comment.getBugId());
				commentInSentiment.setCommentId(comment.getCommentId());
				db.getBugTrackerComments().add(commentInSentiment);
			}
			db.sync();
			ClassificationInstance classificationInstance = prepareBugTrackerCommentInstance(comment);
			classifier.add(classificationInstance);
		}
		
		previousTime = printTimeMessage(startTime, previousTime, classifier.instanceListSize(),
										"prepared bug comments");
		
		Iterable<NewsgroupArticleDetectingCode> articlesIt = detectingCodeMetric.getNewsgroupArticles();
		
		for(NewsgroupArticleDetectingCode article : articlesIt)
		{
			NewsgroupArticlesData articleInSentiment = findNewsgroupArticle(db, article);
			if(articleInSentiment == null)
			{
				articleInSentiment = new NewsgroupArticlesData();
				articleInSentiment.setNewsGroupName(article.getNewsGroupName());
				articleInSentiment.setArticleNumber(article.getArticleNumber());
				db.getNewsgroupArticles().add(articleInSentiment);
			}
			db.sync();
			ClassificationInstance classificationInstance = prepareNewsgroupArticleInstance(article);
			classifier.add(classificationInstance);
		}
		
		previousTime = printTimeMessage(startTime, previousTime, classifier.instanceListSize(),
				"prepared newsgroup articles");
		
		classifier.classify();

		previousTime = printTimeMessage(startTime, previousTime, classifier.instanceListSize(),
										"classifier.classify() finished");
		
		for(BugTrackerCommentDetectingCode comment : commentsIt)
		{
			BugTrackerCommentsData commentInSentiment = findBugTrackerComment(db, comment);
			ClassificationInstance classificationInstance = prepareBugTrackerCommentInstance(comment);
			String classificationResult = classifier.getClassificationResult(classificationInstance);
			commentInSentiment.setClassificationResult(classificationResult);
			String emotionalDimensions = EmotionalDimensions.getDimensions(classificationInstance);
			commentInSentiment.setEmotionalDimensions(emotionalDimensions);
			db.sync();
		}

		previousTime = printTimeMessage(startTime, previousTime, classifier.instanceListSize(),
										"stored classified bug comments");
		
		for(NewsgroupArticleDetectingCode article : articlesIt)
		{
			NewsgroupArticlesData articleInSentiment = findNewsgroupArticle(db, article);
			ClassificationInstance classificationInstance = prepareNewsgroupArticleInstance(article);
			String classificationResult = classifier.getClassificationResult(classificationInstance);
			articleInSentiment.setClassificationResult(classificationResult);
			String emotionalDimensions = EmotionalDimensions.getDimensions(classificationInstance);
			articleInSentiment.setEmotionalDimensions(emotionalDimensions);
			db.sync();
		}
		
		previousTime = printTimeMessage(startTime, previousTime, classifier.instanceListSize(),
				"stored classified Newsgroup articles");

 	}
	
	private long printTimeMessage(long startTime, long previousTime, int size, String message) {
		long currentTime = System.currentTimeMillis();
		System.err.println(time(currentTime - previousTime) + "\t" +
						   time(currentTime - startTime) + "\t" +
						   size + "\t" + message);
		return currentTime;
	}

	private String time(long timeInMS) {
		return DurationFormatUtils.formatDuration(timeInMS, "HH:mm:ss,SSS");
	}
	
	private ClassificationInstance prepareBugTrackerCommentInstance(BugTrackerCommentDetectingCode comment)
	{
    	ClassificationInstance classificationInstance = new ClassificationInstance();
        classificationInstance.setBugTrackerId(comment.getBugTrackerId());
        classificationInstance.setBugId(comment.getBugId());
        classificationInstance.setCommentId(comment.getCommentId());
        
        if (comment.getNaturalLanguage() == null) {
        	classificationInstance.setText("");
        } else {
        	classificationInstance.setText(comment.getNaturalLanguage());
        }
        return classificationInstance;
	}
	
	

	private ClassificationInstance prepareNewsgroupArticleInstance(NewsgroupArticleDetectingCode  article)
	{
    	ClassificationInstance classificationInstance = new ClassificationInstance();
        classificationInstance.setNewsgroupName(article.getNewsGroupName());
        classificationInstance.setArticleNumber(article.getArticleNumber());
        //classificationInstance.setSubject(article.getSubject()); //Is it really necessary?
        if(article.getNaturalLanguage() == null)
        {
        	classificationInstance.setText("");
        }
        else
        {
        	classificationInstance.setText(article.getNaturalLanguage());
        }
        return classificationInstance;
	}
	
	private BugTrackerCommentsData findBugTrackerComment(SentimentClassificationTransMetric db, BugTrackerCommentDetectingCode comment)
	{
		BugTrackerCommentsData bugTrackerCommentsData = null;
		Iterable<BugTrackerCommentsData> bugTrackerCommentsDataIt = 
		db.getBugTrackerComments().
			find(BugTrackerCommentsData.BUGID.eq(comment.getBugId()),
					BugTrackerCommentsData.COMMENTID.eq(comment.getCommentId()));
		for (BugTrackerCommentsData bcd:  bugTrackerCommentsDataIt) {
			bugTrackerCommentsData = bcd;
		}
		return bugTrackerCommentsData;
	}

	private NewsgroupArticlesData findNewsgroupArticle(SentimentClassificationTransMetric db, NewsgroupArticleDetectingCode article)
	{
		NewsgroupArticlesData newsgroupArticlesData = null;
		Iterable<NewsgroupArticlesData> newsgroupArticlesDataIt = 
				db.getNewsgroupArticles().
						find(NewsgroupArticlesData.NEWSGROUPNAME.eq(article.getNewsGroupName()), 
								NewsgroupArticlesData.ARTICLENUMBER.eq(article.getArticleNumber()));
		for (NewsgroupArticlesData nad:  newsgroupArticlesDataIt) {
			newsgroupArticlesData = nad;
		}
		return newsgroupArticlesData;
	}

	//Do not delete the articles database, it is used in other metrics
	private void clearDB(SentimentClassificationTransMetric db) {
		db.getBugTrackerComments().getDbCollection().drop();
		db.sync();
	}

	@Override
	public String getShortIdentifier() {
		return "sentimentclassification";
	}

	@Override
	public String getFriendlyName() {
		return "Sentiment Classification";
	}

	@Override
	public String getSummaryInformation() {
		return "This metric computes the sentiment of each bug comment, newsgroup article or forum post";
	}

}
