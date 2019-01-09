package org.eclipse.scava.metricprovider.trans.plaintextprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.scava.metricprovider.trans.plaintextprocessing.model.BugTrackerCommentPlainTextProcessing;
import org.eclipse.scava.metricprovider.trans.plaintextprocessing.model.NewsgroupArticlePlainTextProcessing;
import org.eclipse.scava.metricprovider.trans.plaintextprocessing.model.PlainTextProcessingTransMetric;
import org.eclipse.scava.nlp.htmlparser.HtmlParser;
import org.eclipse.scava.nlp.preprocessor.markdown.github.MarkdownParserGitHub;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.ITransientMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;
import org.eclipse.scava.platform.delta.ProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemComment;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.PlatformBugTrackingSystemManager;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelArticle;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelDelta;
import org.eclipse.scava.platform.delta.communicationchannel.CommunicationChannelProjectDelta;
import org.eclipse.scava.platform.delta.communicationchannel.PlatformCommunicationChannelManager;
import org.eclipse.scava.repository.model.BugTrackingSystem;
import org.eclipse.scava.repository.model.CommunicationChannel;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.repository.model.cc.nntp.NntpNewsGroup;
import org.eclipse.scava.repository.model.sourceforge.Discussion;

import com.mongodb.DB;

public class PlainTextProcessingTransMetricProvider implements ITransientMetricProvider<PlainTextProcessingTransMetric> {

	protected PlatformBugTrackingSystemManager platformBugTrackingSystemManager;
	protected PlatformCommunicationChannelManager communicationChannelManager;
	
	private Pattern newline = Pattern.compile("\\v+");
	
	@Override
	public String getIdentifier() {
		return PlainTextProcessingTransMetricProvider.class.getCanonicalName();
	}

	@Override
	public String getShortIdentifier() {
		return "plaintextprocessing";
	}

	@Override
	public String getFriendlyName() {
		return "Plain Text Processing";
	}

	@Override
	public String getSummaryInformation() {
		return "This metric preprocess each bug comment or newsgroup article into a split " +
				"plain text format.";
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
		// DO NOTHING -- we don't use anything
	}

	@Override
	public List<String> getIdentifiersOfUses() {
		return Collections.emptyList();
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.platformBugTrackingSystemManager = context.getPlatformBugTrackingSystemManager();
		this.communicationChannelManager = context.getPlatformCommunicationChannelManager();
	}

	@Override
	public PlainTextProcessingTransMetric adapt(DB db) {
		return new PlainTextProcessingTransMetric(db);
	}

	@Override
	public void measure(Project project, ProjectDelta projectDelta, PlainTextProcessingTransMetric db) {

		clearDB(db);
		System.err.println("Started " + getIdentifier());
		
		BugTrackingSystemProjectDelta btspDelta = projectDelta.getBugTrackingSystemDelta();
		
		for (BugTrackingSystemDelta bugTrackingSystemDelta : btspDelta.getBugTrackingSystemDeltas()) {
			
			BugTrackingSystem bugTracker = bugTrackingSystemDelta.getBugTrackingSystem();
			
			for (BugTrackingSystemComment comment: bugTrackingSystemDelta.getComments()) {
				
				BugTrackerCommentPlainTextProcessing commentsData = findBugTrackerComment(db, bugTracker, comment);
				
				if (commentsData == null) {
					commentsData = new BugTrackerCommentPlainTextProcessing();
					commentsData.setBugTrackerId(bugTracker.getOSSMeterId());
					commentsData.setBugId(comment.getBugId());
					commentsData.setCommentId(comment.getCommentId());
					db.getBugTrackerComments().add(commentsData);
				}
				List<String> plainText = new ArrayList<String>();
				switch(bugTracker.getBugTrackerType())
				{
					case "github": plainText=processGitHub(comment.getText()); break;
					case "bugzilla": plainText=processBugzilla(comment.getText()); break;
					//case "jira":
					//case "redmine":
					//case "mantis":
					default: plainText = processPlainText(comment.getText()); break;
				}
				commentsData.setPlainText(plainText);
			}
			db.sync();
		}
		
		CommunicationChannelProjectDelta ccpDelta = projectDelta.getCommunicationChannelDelta();
		for ( CommunicationChannelDelta communicationChannelDelta: ccpDelta.getCommunicationChannelSystemDeltas()) {
			CommunicationChannel communicationChannel = communicationChannelDelta.getCommunicationChannel();
			String communicationChannelName;
			if (!(communicationChannel instanceof NntpNewsGroup))
				communicationChannelName = communicationChannel.getUrl();
			else {
				NntpNewsGroup newsgroup = (NntpNewsGroup) communicationChannel;
				communicationChannelName = newsgroup.getNewsGroupName();
			}
			for (CommunicationChannelArticle article: communicationChannelDelta.getArticles()) {
				NewsgroupArticlePlainTextProcessing newsgroupArticlesData = 
						findNewsgroupArticle(db, communicationChannelName, article);
				if (newsgroupArticlesData == null) {
					newsgroupArticlesData = new NewsgroupArticlePlainTextProcessing();
					newsgroupArticlesData.setNewsGroupName(communicationChannelName);
					newsgroupArticlesData.setArticleNumber(article.getArticleNumber());
					db.getNewsgroupArticles().add(newsgroupArticlesData);
				}
				List<String> plainText = processPlainText(article.getText());
				newsgroupArticlesData.setPlainText(plainText);
			}
			db.sync();
		}
		
	}
	
	private List<String> processGitHub(String text)
	{
		/*We need to duplicate the number of new lines as
		newlines by default are softlines and disappear
		in the Markdown parser.
		*/
		text=newline.matcher(text).replaceAll("\n\n");
		text = MarkdownParserGitHub.parse(text);
		//We need to delete the extra newlines again before parsing the text
		text=newline.matcher(text).replaceAll("");
		return HtmlParser.parse(text);
	}
	
	@Deprecated
	//Temporal solution while we get a new version of Bugzilla?
	private List<String> processBugzilla(String text)
	{
		List<String> textLines = Arrays.asList(text.split("\\h\\h+"));
		return textLines;
	}
	
	private List<String> processPlainText(String text)
	{
		List<String> textLines = Arrays.asList(text.split("\\v+"));
		return textLines;
	}
	
	private void clearDB(PlainTextProcessingTransMetric db) {
		db.getBugTrackerComments().getDbCollection().drop();
		db.getNewsgroupArticles().getDbCollection().drop();
		db.sync();
	}

	private BugTrackerCommentPlainTextProcessing findBugTrackerComment(PlainTextProcessingTransMetric db, 
								BugTrackingSystem bugTracker, BugTrackingSystemComment comment) {
		BugTrackerCommentPlainTextProcessing bugTrackerCommentsData = null;
		Iterable<BugTrackerCommentPlainTextProcessing> bugTrackerCommentsDataIt = 
				db.getBugTrackerComments().
						find(BugTrackerCommentPlainTextProcessing.BUGTRACKERID.eq(bugTracker.getOSSMeterId()), 
								BugTrackerCommentPlainTextProcessing.BUGID.eq(comment.getBugId()),
								BugTrackerCommentPlainTextProcessing.COMMENTID.eq(comment.getCommentId()));
		for (BugTrackerCommentPlainTextProcessing bcd:  bugTrackerCommentsDataIt) {
			bugTrackerCommentsData = bcd;
		}
		return bugTrackerCommentsData;
	}
	

	private NewsgroupArticlePlainTextProcessing findNewsgroupArticle(PlainTextProcessingTransMetric db, 
							String communicationChannelName, CommunicationChannelArticle article) {
		NewsgroupArticlePlainTextProcessing newsgroupArticlesData = null;
		Iterable<NewsgroupArticlePlainTextProcessing> newsgroupArticlesDataIt = 
				db.getNewsgroupArticles().
						find(NewsgroupArticlePlainTextProcessing.NEWSGROUPNAME.eq(communicationChannelName), 
								NewsgroupArticlePlainTextProcessing.ARTICLENUMBER.eq(article.getArticleNumber()));
		for (NewsgroupArticlePlainTextProcessing nad:  newsgroupArticlesDataIt) {
			newsgroupArticlesData = nad;
		}
		return newsgroupArticlesData;
	}
}
