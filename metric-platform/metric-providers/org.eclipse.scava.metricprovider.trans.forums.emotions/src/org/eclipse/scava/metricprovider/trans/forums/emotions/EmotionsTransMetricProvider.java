package org.eclipse.scava.metricprovider.trans.forums.emotions;

import java.util.Arrays;
import java.util.List;

import org.eclipse.scava.metricprovider.trans.detectingcode.DetectingCodeTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.DetectingCodeTransMetric;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.ForumPostDetectingCode;
import org.eclipse.scava.metricprovider.trans.forums.emotions.model.EmotionDimension;
import org.eclipse.scava.metricprovider.trans.forums.emotions.model.ForumEmotionsData;
import org.eclipse.scava.metricprovider.trans.forums.emotions.model.ForumsEmotionsTransMetric;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.ITransientMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;
import org.eclipse.scava.platform.communicationchannel.eclipseforums.EclipseForumsPost;
import org.eclipse.scava.platform.communicationchannel.eclipseforums.EclipseForumsTopic;
import org.eclipse.scava.platform.delta.ProjectDelta;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelForumPost;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelDelta;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelProjectDelta;
import org.eclipse.scava.platform.delta.communicationchannel.PlatformCommunicationChannelManager;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum;
import org.eclipse.scava.sentimentclassifier.opennlptartarus.libsvm.ClassificationInstance;
import org.eclipse.scava.sentimentclassifier.opennlptartarus.libsvm.EmotionalDimensions;

import com.mongodb.DB;

public class EmotionsTransMetricProvider implements ITransientMetricProvider<ForumsEmotionsTransMetric>
{

	protected PlatformCommunicationChannelManager platformCommunicationChannelManager;
	
	protected MetricProviderContext context;
	
	protected List<IMetricProvider> uses;
	
	@Override
	public String getIdentifier() {
		return EmotionsTransMetricProvider.class.getCanonicalName();
	}

	@Override
	public String getShortIdentifier() {
		return "Forumsemotions";
	}

	@Override
	public String getFriendlyName() {
		return "Emotional Dimensions in Forum Posts";
	}

	@Override
	public String getSummaryInformation() {
		return "Emotional Dimensions in Forum Posts";
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
		return Arrays.asList(DetectingCodeTransMetricProvider.class.getCanonicalName());
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.context = context;
		this.platformCommunicationChannelManager = context.getPlatformCommunicationChannelManager();
	}

	@Override
	public ForumsEmotionsTransMetric adapt(DB db) {
		return new ForumsEmotionsTransMetric(db);
	}

	@Override
	public void measure(Project project, ProjectDelta projectDelta, ForumsEmotionsTransMetric db)
	{
		
		CommunicationChannelProjectDelta delta = projectDelta.getCommunicationChannelDelta();
		DetectingCodeTransMetric detectingCodeMetric = ((DetectingCodeTransMetricProvider)uses.get(0)).adapt(context.getProjectDB(project));
		
		for (CommunicationChannelDelta communicationChannelSystemDelta : delta.getCommunicationChannelSystemDeltas())
		{
			
			CommunicationChannel communicationChannel = communicationChannelSystemDelta.getCommunicationChannel();
			String communicationChannelName;
			if(communicationChannel instanceof EclipseForum)
			{
				EclipseForum forum = (EclipseForum) communicationChannel;
				communicationChannelName = forum.getForum_id();
				
			}
			else
				continue;
			
			Iterable<ForumEmotionsData> forumDataIt = db.getForums().find(ForumEmotionsData.FORUMID.eq(communicationChannelName));
			ForumEmotionsData forumEmotionsData = null;
			
			for (ForumEmotionsData fed : forumDataIt)
			{
				forumEmotionsData = fed;
			}
			
			if(forumEmotionsData == null)
			{
				forumEmotionsData = new ForumEmotionsData();
				forumEmotionsData.setForumId(communicationChannelName);
				forumEmotionsData.setNumberOfPosts(0);
				forumEmotionsData.setCumulativeNumberOfPosts(0);
				db.getForums().add(forumEmotionsData);
			}
			
			
			forumEmotionsData.setNumberOfPosts(communicationChannelSystemDelta.getPosts().size());
			forumEmotionsData.setCumulativeNumberOfPosts(forumEmotionsData.getCumulativeNumberOfPosts() +
					communicationChannelSystemDelta.getPosts().size());
			
			db.sync();
			
			Iterable<EmotionDimension> emotionIt = db.getDimensions().find(EmotionDimension.FORUMID.eq(communicationChannelName));
			
			for (EmotionDimension emotion : emotionIt)
			{
				emotion.setNumberOfPosts(0);
			}
			
			for (CommunicationChannelForumPost post : communicationChannelSystemDelta.getPosts())
			{
				ClassificationInstance instance = new ClassificationInstance();
				//Adrián: Modify classification instance composed ID!!!!!
				instance.setForumId(post.getForumId());
				instance.setTopicId(post.getTopicId());
				instance.setPostId(post.getPostId());
				instance.setText(getNaturalLanguage(detectingCodeMetric, post));
				
				String [] emotionalDimensions = EmotionalDimensions.getDimensions(instance).split(",");
				
				for(String dimension : emotionalDimensions)
				{
					dimension = dimension.trim();
					if(dimension.length()>0)
					{
						emotionIt = db.getDimensions().
								find(EmotionDimension.FORUMID.eq(communicationChannelName),
									 EmotionDimension.EMOTIONLABEL.eq(dimension));
						EmotionDimension emotion = null;
						
						for(EmotionDimension em : emotionIt) 
						{
							emotion = em;
						}
						if(emotion == null)
						{
							emotion =new EmotionDimension();
							emotion.setTopicId(communicationChannelName);
							emotion.setEmotionLabel(dimension);
							emotion.setNumberOfPosts(0);
							emotion.setCumulativeNumberOfPosts(0);
							db.getDimensions().add(emotion);
						}
						emotion.setNumberOfPosts(emotion.getNumberOfPosts()+1);
						emotion.setCumulativeNumberOfPosts(emotion.getCumulativeNumberOfPosts() +1);
						db.sync();
					}
				}
			}
			
			db.sync();
			
			emotionIt = db.getDimensions().
					find(EmotionDimension.FORUMID.eq(communicationChannelName));
			
			for (EmotionDimension emotion : db.getDimensions())
			{
				if(forumEmotionsData.getNumberOfPosts() > 0)
				{
					emotion.setPercentage(((float)100*emotion.getNumberOfPosts()) / forumEmotionsData.getNumberOfPosts());
				}
				else
					emotion.setPercentage((float) 0 );
				if(forumEmotionsData.getCumulativeNumberOfPosts()>0)
				{
					emotion.setCumulativePercentage(
							((float)100*emotion.getCumulativeNumberOfPosts()) / forumEmotionsData.getCumulativeNumberOfPosts());
				}
				else
					emotion.setCumulativePercentage((float) 0);
			}
			
			db.sync();
		}
		
	}
	
	private String getNaturalLanguage(DetectingCodeTransMetric db, CommunicationChannelForumPost post) {
		ForumPostDetectingCode forumPostInDetectionCode = null;
		Iterable<ForumPostDetectingCode> forumPostIt = db.getForumPosts().
				find(ForumPostDetectingCode.FORUMID.eq(post.getForumId()),
						ForumPostDetectingCode.TOPICID.eq(post.getTopicId()),
						ForumPostDetectingCode.POSTID.eq(post.getPostId()));
		for (ForumPostDetectingCode nadc:  forumPostIt) {
			forumPostInDetectionCode = nadc;
		}
		if(forumPostInDetectionCode.getNaturalLanguage() == null)
			return "";
		else
			return forumPostInDetectionCode.getNaturalLanguage();
	}

}
