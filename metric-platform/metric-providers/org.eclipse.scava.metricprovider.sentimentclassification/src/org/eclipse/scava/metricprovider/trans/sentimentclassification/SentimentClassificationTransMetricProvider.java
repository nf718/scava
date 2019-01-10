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
package org.eclipse.scava.metricprovider.trans.sentimentclassification;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.eclipse.scava.metricprovider.trans.detectingcode.DetectingCodeTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.BugTrackerCommentDetectingCode;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.DetectingCodeTransMetric;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.ForumPostDetectingCode;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.NewsgroupArticleDetectingCode;
import org.eclipse.scava.metricprovider.trans.sentimentclassification.model.BugTrackerCommentsSentimentClassification;
import org.eclipse.scava.metricprovider.trans.sentimentclassification.model.ForumPostDetectingSentimentClassification;
import org.eclipse.scava.metricprovider.trans.sentimentclassification.model.NewsgroupArticlesSentimentClassification;
import org.eclipse.scava.metricprovider.trans.sentimentclassification.model.SentimentClassificationTransMetric;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.ITransientMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;
import org.eclipse.scava.platform.delta.ProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.PlatformBugTrackingSystemManager;
import org.eclipse.scava.platform.delta.communicationchannel.PlatformCommunicationChannelManager;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum;
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
			BugTrackerCommentsSentimentClassification commentInSentiment = findBugTrackerComment(db, comment);
			if(commentInSentiment == null)
			{
				commentInSentiment = new BugTrackerCommentsSentimentClassification();
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
			NewsgroupArticlesSentimentClassification articleInSentiment = findNewsgroupArticle(db, article);
			if(articleInSentiment == null)
			{
				articleInSentiment = new NewsgroupArticlesSentimentClassification();
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
		
		Iterable<ForumPostDetectingCode> postsIt = detectingCodeMetric.getForumPosts();
		
		for(ForumPostDetectingCode post : postsIt)
		{
			ForumPostDetectingSentimentClassification postInSentiment = findForumPost(db, post);
			if(postInSentiment == null)
			{
				postInSentiment = new ForumPostDetectingSentimentClassification();
				postInSentiment.setForumId(post.getForumId());
				postInSentiment.setPostId(post.getPostId());
				db.getForumPosts().add(postInSentiment);
			}
			db.sync();
			ClassificationInstance classificationInstance = prepareForumPostInstance(post);
			classifier.add(classificationInstance);
		}
		
		
		previousTime = printTimeMessage(startTime, previousTime, classifier.instanceListSize(),
				"prepared forum articles");
		
		classifier.classify();

		previousTime = printTimeMessage(startTime, previousTime, classifier.instanceListSize(),
										"classifier.classify() finished");
		
		for(BugTrackerCommentDetectingCode comment : commentsIt)
		{
			BugTrackerCommentsSentimentClassification commentInSentiment = findBugTrackerComment(db, comment);
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
			NewsgroupArticlesSentimentClassification articleInSentiment = findNewsgroupArticle(db, article);
			ClassificationInstance classificationInstance = prepareNewsgroupArticleInstance(article);
			String classificationResult = classifier.getClassificationResult(classificationInstance);
			articleInSentiment.setClassificationResult(classificationResult);
			String emotionalDimensions = EmotionalDimensions.getDimensions(classificationInstance);
			articleInSentiment.setEmotionalDimensions(emotionalDimensions);
			db.sync();
		}
		
		previousTime = printTimeMessage(startTime, previousTime, classifier.instanceListSize(),
				"stored classified newsgroup articles");
		
		for(ForumPostDetectingCode post : postsIt)
		{
			ForumPostDetectingSentimentClassification postInSentiment = findForumPost(db, post);
			ClassificationInstance classificationInstance = prepareForumPostInstance(post);
			String classificationResult = classifier.getClassificationResult(classificationInstance);
			postInSentiment.setClassificationResult(classificationResult);
			String emotionalDimensions = EmotionalDimensions.getDimensions(classificationInstance);
			postInSentiment.setEmotionalDimensions(emotionalDimensions);
			db.sync();
		}
		
		previousTime = printTimeMessage(startTime, previousTime, classifier.instanceListSize(),
				"stored classified forum posts");

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
        //classificationInstance.setSubject(article.getSubject()); //It is not used anywhere...
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
	
	private ClassificationInstance prepareForumPostInstance(ForumPostDetectingCode post) {
		ClassificationInstance classificationInstance = new ClassificationInstance();
        classificationInstance.setForumId(post.getForumId());
        classificationInstance.setPostId(post.getPostId());
        if(post.getNaturalLanguage() == null)
        {
        	classificationInstance.setText("");
        }
        else
        {
        	classificationInstance.setText(post.getNaturalLanguage());
        }
        return classificationInstance;
	}
	
	private BugTrackerCommentsSentimentClassification findBugTrackerComment(SentimentClassificationTransMetric db, BugTrackerCommentDetectingCode comment)
	{
		BugTrackerCommentsSentimentClassification bugTrackerCommentsData = null;
		Iterable<BugTrackerCommentsSentimentClassification> bugTrackerCommentsDataIt = 
		db.getBugTrackerComments().
			find(BugTrackerCommentsSentimentClassification.BUGTRACKERID.eq(comment.getBugTrackerId()),
					BugTrackerCommentsSentimentClassification.BUGID.eq(comment.getBugId()),
					BugTrackerCommentsSentimentClassification.COMMENTID.eq(comment.getCommentId()));
		for (BugTrackerCommentsSentimentClassification bcd:  bugTrackerCommentsDataIt) {
			bugTrackerCommentsData = bcd;
		}
		return bugTrackerCommentsData;
	}

	private NewsgroupArticlesSentimentClassification findNewsgroupArticle(SentimentClassificationTransMetric db, NewsgroupArticleDetectingCode article)
	{
		NewsgroupArticlesSentimentClassification newsgroupArticlesData = null;
		Iterable<NewsgroupArticlesSentimentClassification> newsgroupArticlesDataIt = 
				db.getNewsgroupArticles().
						find(NewsgroupArticlesSentimentClassification.NEWSGROUPNAME.eq(article.getNewsGroupName()), 
								NewsgroupArticlesSentimentClassification.ARTICLENUMBER.eq(article.getArticleNumber()));
		for (NewsgroupArticlesSentimentClassification nad:  newsgroupArticlesDataIt) {
			newsgroupArticlesData = nad;
		}
		return newsgroupArticlesData;
	}
	
	private ForumPostDetectingSentimentClassification findForumPost(SentimentClassificationTransMetric db,
			ForumPostDetectingCode post) {
		ForumPostDetectingSentimentClassification forumPostData = null;
		Iterable<ForumPostDetectingSentimentClassification> forumPostsDataIt = 
				db.getForumPosts().
						find(ForumPostDetectingSentimentClassification.FORUMID.eq(post.getForumId()), 
								ForumPostDetectingSentimentClassification.POSTID.eq(post.getPostId()));
		for (ForumPostDetectingSentimentClassification nad:  forumPostsDataIt) {
			forumPostData = nad;
		}
		return forumPostData;
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
