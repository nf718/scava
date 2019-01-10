package org.eclipse.scava.metricprovider.historic.index;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.metricprovider.historic.index.model.IndexHistoricMetric;
import org.eclipse.scava.metricprovider.trans.bugs.emotions.EmotionsTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.detectingcode.DetectingCodeTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.DetectingCodeTransMetric;
import org.eclipse.scava.metricprovider.trans.plaintextprocessing.PlainTextProcessingTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.plaintextprocessing.model.BugTrackerCommentPlainTextProcessing;
import org.eclipse.scava.metricprovider.trans.plaintextprocessing.model.PlainTextProcessingTransMetric;
import org.eclipse.scava.metricprovider.trans.requestreplyclassification.RequestReplyClassificationTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.sentimentclassification.SentimentClassificationTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.severityclassification.SeverityClassificationTransMetricProvider;
import org.eclipse.scava.metricprovider.trans.topics.TopicsTransMetricProvider;
import org.eclipse.scava.platform.AbstractHistoricalMetricProvider;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;
import org.eclipse.scava.platform.delta.bugtrackingsystem.PlatformBugTrackingSystemManager;
import org.eclipse.scava.platform.delta.communicationchannel.PlatformCommunicationChannelManager;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.repository.model.cc.eclipseforums.EclipseForum;
import org.eclipse.scava.repository.model.cc.nntp.NntpNewsGroup;
import org.eclipse.scava.repository.model.sourceforge.Discussion;

import com.googlecode.pongo.runtime.Pongo;

public class IndexHistoricMetricProvider extends AbstractHistoricalMetricProvider {

	
	
	public final static String IDENTIFIER = "org.eclipse.scava.metricprovider.historic.index";
	protected PlatformBugTrackingSystemManager platformBugTrackingSystemManager;
	protected PlatformCommunicationChannelManager communicationChannelManager;
	protected MetricProviderContext context;
	
	/**
	 * List of MPs that are used by this MP. These are MPs who have specified that 
	 * they 'provide' data for this MP.
	 */
	protected List<IMetricProvider> uses;
	
	@Override
	public Pongo measure(Project project) throws NullPointerException{
		System.err.println("Started " + getIdentifier());
		IndexHistoricMetric indexHistoricMetric = new IndexHistoricMetric();
		
			
		return indexHistoricMetric;
	}

	@Override
	public String getIdentifier() {
		
		return IDENTIFIER;
	}

	@Override
	public String getShortIdentifier() {
		
		return "indexing";
	}

	@Override
	public String getFriendlyName() {
		
		return "Architectural requirement assoicated with populating the Elasticsearch Index";
	}

	@Override
	public String getSummaryInformation() {
		
		return "This metric is an architectural requirement associated with the  preparation of documents for indexing. This does not 'compute' knowledge for the knowledge base";
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
		
		this.uses = uses;
		
	}

	@Override
	public List<String> getIdentifiersOfUses() {
		
		List<String> list = new ArrayList<String>();
		
		list.add(PlainTextProcessingTransMetricProvider.class.getCanonicalName());//plain text
		list.add(SeverityClassificationTransMetricProvider.class.getCanonicalName());//severity
		list.add(SentimentClassificationTransMetricProvider.class.getCanonicalName());//sentiment
		list.add(DetectingCodeTransMetricProvider.class.getCanonicalName());//code detect
		list.add(RequestReplyClassificationTransMetricProvider.class.getCanonicalName());// Request Reply
		list.add(EmotionsTransMetricProvider.class.getCanonicalName());//emotion Bugs;
		list.add(org.eclipse.scava.metricprovider.trans.newsgroups.emotions.EmotionsTransMetricProvider.class.getCanonicalName());//emotion (CC)
		
		
		list.add(TopicsTransMetricProvider.class.getCanonicalName());//topics
		
		//ADD MORE HERE
		
	return list;
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.platformBugTrackingSystemManager = context.getPlatformBugTrackingSystemManager();
		this.communicationChannelManager = context.getPlatformCommunicationChannelManager();
		this.context = context;
	}

}
