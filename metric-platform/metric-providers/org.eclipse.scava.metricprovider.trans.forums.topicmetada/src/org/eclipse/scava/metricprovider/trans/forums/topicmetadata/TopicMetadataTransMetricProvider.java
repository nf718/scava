package org.eclipse.scava.metricprovider.trans.forums.topicmetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.scava.contentclassifier.opennlptartarus.libsvm.ClassificationInstance;
import org.eclipse.scava.contentclassifier.opennlptartarus.libsvm.Classifier;
import org.eclipse.scava.metricprovider.trans.detectingcode.DetectingCodeTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.DetectingCodeTransMetric;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.ForumPostDetectingCode;
import org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.ForumsTopicMetadataTransMetric;
import org.eclipse.scava.metricprovider.trans.forums.topicmetadata.model.PostData;
import org.eclipse.scava.metricprovider.trans.requestreplyclassification.RequestReplyClassificationTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.requestreplyclassification.model.ForumsPosts;
import org.eclipse.scava.metricprovider.trans.requestreplyclassification.model.RequestReplyClassificationTransMetric;
import org.eclipse.scava.metricprovider.trans.sentimentclassification.SentimentClassificationTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.sentimentclassification.model.SentimentClassificationTransMetric;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.ITransientMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;
import org.eclipse.scava.platform.delta.ProjectDelta;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelForumPost;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelDelta;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelProjectDelta;
import org.eclipse.scava.platform.delta.communicationchannel.PlatformCommunicationChannelManager;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum;

import com.mongodb.DB;

public class TopicMetadataTransMetricProvider implements ITransientMetricProvider<ForumsTopicMetadataTransMetric>
{
	protected PlatformCommunicationChannelManager platformCommunicationChannelManager;
	
	protected MetricProviderContext context;
	
	protected List<IMetricProvider> uses;
	
	@Override
	public String getIdentifier() {
		return TopicMetadataTransMetricProvider.class.getCanonicalName();
	}

	@Override
	public String getShortIdentifier() {
		return "ForumsTopicMetadata";
	}

	@Override
	public String getFriendlyName() {
		return "Forum's topic Metadata in Forums";
	}

	@Override
	public String getSummaryInformation() {
		return "Forums's topic Metadata in Forums";
	}

	@Override
	public boolean appliesTo(Project project) {
		for (CommunicationChannel communicationChannel: project.getCommunicationChannels()) {
			if (communicationChannel instanceof EclipseForum) return true;
		}
		return false;
	}

	@Override
	public void setUses(List<IMetricProvider> uses) {
		this.uses = uses;
	}

	@Override
	public List<String> getIdentifiersOfUses() {
		return Arrays.asList(RequestReplyClassificationTransMetricProvider.class.getCanonicalName(),
				SentimentClassificationTransMetricProvider.class.getCanonicalName(),
				DetectingCodeTransMetricProvider.class.getCanonicalName());
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.context = context;
		this.platformCommunicationChannelManager = context.getPlatformCommunicationChannelManager();
	}

	@Override
	public ForumsTopicMetadataTransMetric adapt(DB db) {
		return new ForumsTopicMetadataTransMetric(db);
	}

	@Override
	public void measure(Project project, ProjectDelta projectDelta, ForumsTopicMetadataTransMetric db)
	{
		CommunicationChannelProjectDelta delta = projectDelta.getCommunicationChannelDelta();
		
		if (uses.size()!=getIdentifiersOfUses().size())
		{
			System.err.println("Metric: " + getIdentifier() + " failed to retrieve " + 
								"the transient metrics it needs!");
			System.exit(-1);
		}
		
		RequestReplyClassificationTransMetric requestReplyClassifier = ((RequestReplyClassificationTransMetricProvider)uses.get(0)).adapt(context.getProjectDB(project));
		
		Map<String, String> postReplyRequest = new HashMap<String, String>();
		
		for (ForumsPosts post: requestReplyClassifier.getForumPosts())
		{
			postReplyRequest.put(post.getTopicId() + post.getPostId(), post.getClassificationResult());
		}
		
		SentimentClassificationTransMetric sentimentClassifier = ((SentimentClassificationTransMetricProvider)uses.get(1)).adapt(context.getProjectDB(project));
		
		DetectingCodeTransMetric detectingCodeMetric = ((DetectingCodeTransMetricProvider)uses.get(2)).adapt(context.getProjectDB(project));
		
		for (CommunicationChannelDelta communicationChannelDelta : delta.getCommunicationChannelSystemDeltas())
		{
			
			CommunicationChannel communicationChannel = communicationChannelDelta.getCommunicationChannel();
			String communicationChannelName;
			if(communicationChannel instanceof EclipseForum)
			{
				EclipseForum forum = (EclipseForum) communicationChannel;
				communicationChannelName = forum.getForum_id();
			}
			else
				continue;
			
			Map<String, List<String>> newTopicPosts = new HashMap<String, List<String>>();
			
			for(CommunicationChannelForumPost post : communicationChannelDelta.getPosts())
			{
				List<String> newPosts;
				if (newTopicPosts.containsKey(post.getTopicId())) {
					newPosts = newTopicPosts.get(post.getTopicId());
				} else {
					newPosts = new ArrayList<String>();
					newTopicPosts.put(post.getForumId(), newPosts);
				}
				newPosts.add(post.getPostId());	
			}
			
			Classifier classifier = new Classifier();
			Map<String, ClassificationInstance> classificationInstanceIndex = new HashMap<String, ClassificationInstance>();
			
			for(CommunicationChannelForumPost post : communicationChannelDelta.getPosts())
			{
				Iterable<PostData> postIt = db.getPosts().
													find(PostData.FORUMID.eq(post.getForumId()),
														 PostData.TOPICID.eq(post.getTopicId()),
														 PostData.POSTID.eq(post.getPostId()));
				int numberOfStoredPosts = 0;
				for (Iterator<PostData> it = postIt.iterator(); it.hasNext();)
				{
					it.next();
					numberOfStoredPosts++;
				}
				List<String> commentList = newTopicPosts.get(post.getTopicId());
				int positionFromThreadBeginning = commentList.indexOf(post.getPostId());
				positionFromThreadBeginning += numberOfStoredPosts;	
				ClassificationInstance instance = prepareClassificationInstance(post, positionFromThreadBeginning, detectingCodeMetric);
				
				
				classifier.add(instance);
				classificationInstanceIndex.put(post.getForumId()+"_"+post.getPostId(), instance);
			}
			
			classifier.classify();
			
			storeComments(db, classifier, communicationChannelDelta, classificationInstanceIndex, postReplyRequest); 
			
			db.sync(); 
			
			//TODO Need to implement parts regarding the topics
			
			//Still need to be implemeted the topics
//			for(CommuincationChannelForumTopic topic : communicationChannelDelta.getTopics())
//			{
//				
//			}
			
			
		}
		
	}
	
	private String getNaturalLanguage(CommunicationChannelForumPost post, DetectingCodeTransMetric db)
	{
		ForumPostDetectingCode forumPostInDetectionCode = null;
		Iterable<ForumPostDetectingCode> forumPostIt = db.getForumPosts().
				find(ForumPostDetectingCode.FORUMID.eq(post.getForumId()),
						ForumPostDetectingCode.TOPICID.eq(post.getTopicId()),
						ForumPostDetectingCode.POSTID.eq(post.getPostId()));
		for (ForumPostDetectingCode fpdc:  forumPostIt) {
			forumPostInDetectionCode = fpdc;
		}
		if(forumPostInDetectionCode.getNaturalLanguage() != null)
			return forumPostInDetectionCode.getNaturalLanguage();
		else
			return "";
	}
	
	private ClassificationInstance prepareClassificationInstance(CommunicationChannelForumPost post, int positionFromThreadBeginning, DetectingCodeTransMetric db)
	{
		ClassificationInstance instance = new ClassificationInstance(); 
		instance.setTopicId(post.getForumId());
		instance.setPostId(post.getPostId());
		instance.setPositionFromThreadBeginning(positionFromThreadBeginning);
		instance.setSubject(post.getSubject());
		instance.setText(getNaturalLanguage(post, db));
		return instance;
	}
	
	private void storeComments(ForumsTopicMetadataTransMetric db, Classifier classifier, CommunicationChannelDelta communicationChannelDelta, 
			   Map<String, ClassificationInstance> classificationInstanceIndex, Map<String, String> postReplyRequest)
	{
		for (CommunicationChannelForumPost post : communicationChannelDelta.getPosts())
		{
			Iterable<PostData> postIt = 
				db.getPosts().find(PostData.FORUMID.eq(post.getForumId()), 
										PostData.TOPICID.eq(post.getTopicId()),
										PostData.POSTID.eq(post.getPostId()));
			PostData postData = null;
			for (PostData pd:  postIt)
				postData = pd;
			if (postData == null)
			{
				postData = new PostData();
				postData.setForumId(post.getForumId());
				postData.setTopicId(post.getTopicId());
				postData.setPostId(post.getPostId());
				postData.setCreationDate(post.getDate().toString());
				postData.setCreator(post.getUser());
				ClassificationInstance classificationInstance = classificationInstanceIndex.get(post.getTopicId()+"_"+post.getPostId());
				postData.setContentClass(classifier.getClassificationResult(classificationInstance));
				String requestReplyPrediction = postReplyRequest.get(post.getForumId()+ post.getTopicId() + post.getPostId());
				postData.setRequestReplyPrediction(requestReplyPrediction);
				db.getPosts().add(postData);
				db.sync();
			}
		}
	}

}
