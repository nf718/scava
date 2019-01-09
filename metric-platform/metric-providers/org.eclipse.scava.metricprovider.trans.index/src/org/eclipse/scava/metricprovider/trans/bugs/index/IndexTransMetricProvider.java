package org.eclipse.scava.metricprovider.trans.bugs.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.scava.metricprovider.trans.bugs.index.document.BitbucketCommentDocument;
import org.eclipse.scava.metricprovider.trans.bugs.index.document.BitbucketIssueDocument;
import org.eclipse.scava.metricprovider.trans.bugs.index.document.GitHubCommentDocument;
import org.eclipse.scava.metricprovider.trans.bugs.index.document.GitHubIssueDocument;
import org.eclipse.scava.metricprovider.trans.bugs.index.document.GitHubPullRequestDocument;
import org.eclipse.scava.metricprovider.trans.bugs.index.document.JiraCommentDocument;
import org.eclipse.scava.metricprovider.trans.bugs.index.document.JiraIssueDocument;
import org.eclipse.scava.metricprovider.trans.bugs.index.document.MantisIssueDocument;
import org.eclipse.scava.metricprovider.trans.bugs.index.document.RedmineCommentDocument;
import org.eclipse.scava.metricprovider.trans.bugs.index.document.RedmineIssueDocument;
import org.eclipse.scava.metricprovider.trans.bugs.index.model.IndexTransMetric;
import org.eclipse.scava.metricprovider.trans.detectingcode.model.DetectingCodeTransMetric;
import org.eclipse.scava.platform.IMetricProvider;
import org.eclipse.scava.platform.ITransientMetricProvider;
import org.eclipse.scava.platform.MetricProviderContext;
import org.eclipse.scava.platform.bugtrackingsystem.bitbucket.api.BitbucketIssue;
import org.eclipse.scava.platform.bugtrackingsystem.bitbucket.api.BitbucketIssueComment;
import org.eclipse.scava.platform.bugtrackingsystem.github.GitHubBugTrackingSystemDelta;
import org.eclipse.scava.platform.bugtrackingsystem.github.GitHubComment;
import org.eclipse.scava.platform.bugtrackingsystem.github.GitHubIssue;
import org.eclipse.scava.platform.bugtrackingsystem.github.GitHubPullRequest;
import org.eclipse.scava.platform.bugtrackingsystem.jira.api.JiraComment;
import org.eclipse.scava.platform.bugtrackingsystem.jira.api.JiraIssue;
import org.eclipse.scava.platform.bugtrackingsystem.mantis.MantisIssue;
import org.eclipse.scava.platform.bugtrackingsystem.redmine.RedmineBugTrackingSystemDelta;
import org.eclipse.scava.platform.bugtrackingsystem.redmine.api.RedmineComment;
import org.eclipse.scava.platform.bugtrackingsystem.redmine.api.RedmineIssue;
import org.eclipse.scava.platform.delta.ProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemBug;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemComment;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemProjectDelta;
import org.eclipse.scava.platform.delta.bugtrackingsystem.PlatformBugTrackingSystemManager;
import org.eclipse.scava.repository.model.Project;
import org.eclipse.scava.index.indexer.Indexer;

import com.mongodb.DB;

/**
 * This class is responsible for preparing data contained within the bug tracking system deltas in a format that is accepted by the Indexing Tool 
 * 
 * TODO - Enable NL Fields once models/classifiers are complete
 * TODO - Add Mappings
 * 
 * @author Dan Campbell
 */
public class IndexTransMetricProvider implements ITransientMetricProvider<IndexTransMetric> {

	protected PlatformBugTrackingSystemManager platformBugTrackingSystemManager;
	protected List<IMetricProvider> uses;
	protected MetricProviderContext context;
	
	protected String knowledgeType = "nlp";

	@Override
	public String getIdentifier() {
		return IndexTransMetricProvider.class.getCanonicalName();
	}

	@Override
	public String getShortIdentifier() {
		return "BugIndexerPrep";
	}

	@Override
	public String getFriendlyName() {
		return "BugTrackerIndexPreperation";
	}

	@Override
	public String getSummaryInformation() {

		return "This metric is responsible for preparing bug tracking system documents for the Indexing";
	}

	@Override
	public boolean appliesTo(Project project) {
	
		return !project.getBugTrackingSystems().isEmpty();
	}

	@Override
	public void setUses(List<IMetricProvider> uses) {

		this.uses = uses;
	}

	@Override
	//TODO - add metric dependencies 
	public List<String> getIdentifiersOfUses() {
		List<String> requiresList = new ArrayList<String>();
		// Here I add all of the TransMetricProvider classes the indexer relies on
		//requiresList.add(PlainTextProcessingTransMetricProvider.class.getCanonicalName());
		//requiresList.add(DetectingCodeTransMetricProvider.class.getCanonicalName());
		//requiresList.add(SeverityClassificationTransMetricProvider.class.getCanonicalName());
		// list.add( .class.getCanonicalName());
		return requiresList;
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.platformBugTrackingSystemManager = context.getPlatformBugTrackingSystemManager();
		this.context = context;
	}

	@Override
	public IndexTransMetric adapt(DB db) {
		return new IndexTransMetric(db);
	}

	@Override
	public void measure(Project project, ProjectDelta projectDelta, IndexTransMetric db) {

		BugTrackingSystemProjectDelta bugTrackingSystemProjectDelta = projectDelta.getBugTrackingSystemDelta();

		for (BugTrackingSystemDelta bugTrackingSystemDelta : bugTrackingSystemProjectDelta.getBugTrackingSystemDeltas()) {

			String bugTrackerType = bugTrackingSystemDelta.getBugTrackingSystem().getBugTrackerType();

			switch (bugTrackerType) {

			case "github":
				prepareGitHub(project, bugTrackingSystemDelta, projectDelta); //done
				break;
			case "mantis":
				prepareMatnis(project, bugTrackingSystemDelta, projectDelta); //done
				break;
			case "bitbucket":
				prepareBitbucket(project, bugTrackingSystemDelta, projectDelta); //broken
				break;
			case "redmine":
				prepareRedmine(project, bugTrackingSystemDelta, projectDelta); //done
				break;
			case "gitlab":
				prepareGitLab(project, bugTrackingSystemDelta, projectDelta); //pending york
				break;
			case "bugzilla":
				prepareBugzilla(project, bugTrackingSystemDelta, projectDelta); //pending york
				break;
			case "jira":
				prepareJira(project, bugTrackingSystemDelta, projectDelta); //need source to check
				break;
			}
		}
		
	
		clearDB(db);
	}
	
	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------

		// METHODS RELATING TO THE BUG TRACKING SOURCES

	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------
	
	/**
	 * prepares GitHub bugtracking system delta objects for indexing
	 * 
	 * @param project
	 * @param bugTrackingSystemDelta
	 * @param projectDelta
	 */
	private void prepareGitHub(Project project, BugTrackingSystemDelta bugTrackingSystemDelta, ProjectDelta projectDelta) {
		
		GitHubBugTrackingSystemDelta githubDelta = (GitHubBugTrackingSystemDelta) bugTrackingSystemDelta;
		String bugTrackerType = githubDelta.getBugTrackingSystem().getBugTrackerType();

		for (BugTrackingSystemBug bug : githubDelta.getNewBugs()) {
			
			GitHubIssue issue = (GitHubIssue) bug;
			String documentType = "bug";
			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
			String uid = generateUniqueDocumentId(projectDelta, issue.getBugId(), bugTrackerType);
			String mapping = loadMapping(indexName);
			GitHubIssueDocument githubIssueDocument = gitHubIssueObjMap(issue, project.getName(), uid);
			
			Indexer.performIndexing(indexName, mapping, documentType, uid, githubIssueDocument);
		}
		
		for (BugTrackingSystemBug bug : githubDelta.getUpdatedBugs()) {
		
			GitHubIssue issue = (GitHubIssue) bug;
			String documentType = "bug";
			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
			String uid = generateUniqueDocumentId(projectDelta, issue.getBugId(), bugTrackerType);
			String mapping = loadMapping(indexName);
			GitHubIssueDocument githubIssueDocument = gitHubIssueObjMap(issue, project.getName(), uid);
			
			Indexer.performIndexing(indexName, mapping, documentType, uid, githubIssueDocument);
		}
		
		for (GitHubPullRequest pullRequest : githubDelta.getPullRequests()) {
			
			String documentType = "pullrequest";
			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
			String uid = generateUniqueDocumentId(projectDelta, pullRequest.getId(), bugTrackerType);
			String mapping = loadMapping(indexName);
			GitHubPullRequestDocument githubPullRequestDocument = gitHubPullRequestObjMap(pullRequest, project.getName(), uid);
			Indexer.performIndexing(indexName, mapping, documentType, uid, githubPullRequestDocument);
		}
	}

	/**
	 * prepares Manits bugtracking system delta objects for indexing
	 * 
	 * @param project
	 * @param bugTrackingSystemDelta
	 * @param projectDelta
	 */
	private void prepareMatnis(Project project, BugTrackingSystemDelta bugTrackingSystemDelta, ProjectDelta projectDelta) {
		
		String bugTrackerType = bugTrackingSystemDelta.getBugTrackingSystem().getBugTrackerType();
		
		for (BugTrackingSystemBug bug : bugTrackingSystemDelta.getNewBugs()) {
			
			MantisIssue issue = (MantisIssue)bug;
			String documentType = "bug";
			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
			String uid = generateUniqueDocumentId(projectDelta, issue.getBugId(), bugTrackerType);
			String mapping = loadMapping(indexName);
			MantisIssueDocument mantisIssueDocument = mantisIssueObjMap(issue, project.getName(), uid);
			Indexer.performIndexing(indexName, mapping, documentType, uid, mantisIssueDocument);
		}
		
		for (BugTrackingSystemBug bug : bugTrackingSystemDelta.getUpdatedBugs()) {
			
			MantisIssue issue = (MantisIssue)bug;
			String documentType = "bug";
			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
			String uid = generateUniqueDocumentId(projectDelta, issue.getBugId(), bugTrackerType);
			String mapping = loadMapping(indexName);
			MantisIssueDocument mantisIssueDocument = mantisIssueObjMap(issue, project.getName(), uid);
			Indexer.performIndexing(indexName, mapping, documentType, uid, mantisIssueDocument);
		}

	}
	
	/**
	 * prepares Bitbucket bug tracking system delta objects for indexing
	 * 
	 * @param project
	 * @param bugTrackingSystemDelta
	 * @param projectDelta
	 */
	private void prepareBitbucket(Project project, BugTrackingSystemDelta bugTrackingSystemDelta, ProjectDelta projectDelta) {
		
		//TODO INCLUDE COMMENTS IN ISSUES
		String bugTrackerType = bugTrackingSystemDelta.getBugTrackingSystem().getBugTrackerType();
		
		for (BugTrackingSystemBug bug : bugTrackingSystemDelta.getNewBugs()) {
			
			BitbucketIssue issue = (BitbucketIssue)bug;
			String documentType = "bug";
			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
			String uid = generateUniqueDocumentId(projectDelta, issue.getBugId(), bugTrackerType);
			String mapping = loadMapping(indexName);
			BitbucketIssueDocument bitbucketIssueDocument = bitbucketIssueObjMap(issue, project.getName(), uid);
			
			Indexer.performIndexing(indexName, mapping, documentType, uid, bitbucketIssueDocument);
		}	
		
		for (BugTrackingSystemBug bug : bugTrackingSystemDelta.getUpdatedBugs()) {
			
			BitbucketIssue issue = (BitbucketIssue)bug;
			String documentType = "bug";
			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
			String uid = generateUniqueDocumentId(projectDelta, issue.getBugId(), bugTrackerType);
			BitbucketIssueDocument bitbucketIssueDocument = bitbucketIssueObjMap(issue, project.getName(), uid);
			String mapping = loadMapping(indexName);
			Indexer.performIndexing(indexName, mapping, documentType, uid, bitbucketIssueDocument);
		}	
		
		
	}
	

	/**
	 * prepares Jira bug tracking system delta objects for indexing
	 * 
	 * @param project
	 * @param bugTrackingSystemDelta
	 * @param projectDelta
	 */
	private void prepareJira(Project project, BugTrackingSystemDelta bugTrackingSystemDelta, ProjectDelta projectDelta) {
		
		String bugTrackerType = bugTrackingSystemDelta.getBugTrackingSystem().getBugTrackerType();
		
		for (BugTrackingSystemBug bug : bugTrackingSystemDelta.getNewBugs()) {
			
			JiraIssue issue = (JiraIssue)bug;
			String documentType = "bug";
			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
			String uid = generateUniqueDocumentId(projectDelta, issue.getBugId(), bugTrackerType);
			String mapping = loadMapping(indexName);
			JiraIssueDocument bitbucketIssueDocument = jiraIssueObjMap(issue, project.getName(), uid);
			
			Indexer.performIndexing(indexName, mapping, documentType, uid, bitbucketIssueDocument);
		
		}
		
		for (BugTrackingSystemBug bug : bugTrackingSystemDelta.getNewBugs()) {
			String bugId = bug.getBugId();
			
			
			JiraIssue issue = (JiraIssue)bug;
			String documentType = "bug";
			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
			String uid = generateUniqueDocumentId(projectDelta, issue.getBugId(), bugTrackerType);
			String mapping = loadMapping(indexName);
			JiraIssueDocument bitbucketIssueDocument = jiraIssueObjMap(issue, project.getName(), uid);
		
			Indexer.performIndexing(indexName, mapping,  documentType, uid, bitbucketIssueDocument);
		}
		
		for (BugTrackingSystemComment comment : bugTrackingSystemDelta.getComments()) {
			
			JiraComment jiraComment = (JiraComment)comment;
			String documentType = "comment";
			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
			String uid = generateUniqueDocumentId(projectDelta, jiraComment.getBugId(), bugTrackerType);
			String mapping = loadMapping(indexName);
			JiraCommentDocument jiraCommentDocument = jiraCommentObjMap(jiraComment, project.getName(), uid);
			
			Indexer.performIndexing(indexName, mapping,  documentType, uid, jiraCommentDocument);
		
		}
	}
	
	/**
	 * prepares Bugzilla bug tracking system delta objects for indexing
	 * 
	 * @param project
	 * @param bugTrackingSystemDelta
	 * @param projectDelta
	 */
	private void prepareBugzilla(Project project, BugTrackingSystemDelta bugTrackingSystemDelta, ProjectDelta projectDelta) {
		String bugTrackerType = bugTrackingSystemDelta.getBugTrackingSystem().getBugTrackerType();
		
		for (BugTrackingSystemBug bug : bugTrackingSystemDelta.getNewBugs()) {
			
		}
		
		for (BugTrackingSystemBug bug : bugTrackingSystemDelta.getUpdatedBugs()) {
			
		}
		
		for (BugTrackingSystemComment comment : bugTrackingSystemDelta.getComments()) {
			
		}
		
	}
	
	/**
	 * prepares GitLab bug tracking system delta objects for indexing
	 * 
	 * @param project
	 * @param bugTrackingSystemDelta
	 * @param projectDelta
	 */
	private void prepareGitLab(Project project, BugTrackingSystemDelta bugTrackingSystemDelta, ProjectDelta projectDelta) {
		//TODO Pending York
		
		String bugTrackerType = bugTrackingSystemDelta.getBugTrackingSystem().getBugTrackerType();
		
		for (BugTrackingSystemBug bug : bugTrackingSystemDelta.getNewBugs()) {
			

//			GitLabIssue issue = (GitLabIssue ) bug;
//			String documentType = "bug";
//			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
//			String uid = generateUniqueDocumentId(projectDelta, issue.getBugId(), bugTrackerType);
//			String mapping = loadMapping(indexName);
//			GitLabIssueDocument githubIssueDocument = GitLabIssueDocumentObjMap(issue, project.getName(), uid);
			
		}
		
		for (BugTrackingSystemBug bug : bugTrackingSystemDelta.getUpdatedBugs()) {
			
//			GitLabIssue issue = (GitLabIssue ) bug;
//			String documentType = "bug";
//			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
//			String uid = generateUniqueDocumentId(projectDelta, issue.getBugId(), bugTrackerType);
//			String mapping = loadMapping(indexName);
//			GitLabIssueDocument githubIssueDocument = GitLabIssueDocumentObjMap(issue, project.getName(), uid);
			
		}
		
		for (BugTrackingSystemComment comment : bugTrackingSystemDelta.getComments()) {
			
		}
	}
	
	/**
	 * prepares Redmine bug tracking system delta objects for indexing
	 * 
	 * @param project
	 * @param bugTrackingSystemDelta
	 * @param projectDelta
	 */
	private void prepareRedmine(Project project, BugTrackingSystemDelta bugTrackingSystemDelta, ProjectDelta projectDelta) {

		RedmineBugTrackingSystemDelta redmineDelta = (RedmineBugTrackingSystemDelta) bugTrackingSystemDelta;
		String bugTrackerType = redmineDelta.getBugTrackingSystem().getBugTrackerType();

		for (BugTrackingSystemBug bug : redmineDelta.getNewBugs()) {
			
			RedmineIssue issue = (RedmineIssue) bug;
			String documentType = "bug";
			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
			String uid = generateUniqueDocumentId(projectDelta, issue.getBugId(), bugTrackerType);
			String mapping = loadMapping(indexName);
			RedmineIssueDocument githubIssueDocument = redmineIssueObjMap(issue, project.getName(), uid);
			
			Indexer.performIndexing(indexName, mapping,  documentType, uid, githubIssueDocument);
		}
		
		for (BugTrackingSystemBug bug : redmineDelta.getUpdatedBugs()) {
		
			RedmineIssue issue = (RedmineIssue) bug;
			String documentType = "bug";
			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
			String uid = generateUniqueDocumentId(projectDelta, issue.getBugId(), bugTrackerType);
			String mapping = loadMapping(indexName);
			RedmineIssueDocument githubIssueDocument = redmineIssueObjMap(issue, project.getName(), uid);
			
			Indexer.performIndexing(indexName, mapping,  documentType, uid, githubIssueDocument);
		}
		
		for (BugTrackingSystemComment comment : redmineDelta.getComments()) {
			
			RedmineComment RedmineComment = (RedmineComment)comment;
			String documentType = "comment";
			String indexName = Indexer.generateIndexName(bugTrackerType, documentType, knowledgeType);
			String uid = generateUniqueDocumentId(projectDelta, comment.getBugId(), bugTrackerType);
			String mapping = loadMapping(indexName);
			RedmineCommentDocument commentDocument = redmineCommentObjMap(RedmineComment, project.getName(), uid);
			
			Indexer.performIndexing(indexName, mapping,  documentType, uid, commentDocument);
		}
		

	}
	
	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------

		// HELPER METHODS

	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------
	
	/**
	 * This method returns a unique Identifier based upon the SourceType, Project,
	 * Document Type and source ID;
	 * 
	 * @return String uid - a uniquely identifiable string.
	 */
	private String generateUniqueDocumentId(ProjectDelta projectDelta, String id, String bugTrackerType) {

		String projectName = projectDelta.getProject().getName();
		String uid = bugTrackerType + " " + projectName + " " + id;
		return uid;
	}
	


	/**
	 * Loads the mapping for a particular index from the 'mappings'directory
	 * 
	 * @param mapping
	 * @return String
	 */
	private String loadMapping(String indexName) {
		
		String indexmapping = "";
		File file = null;
		try {
			file = new File(locateMappings(indexName));
		} catch (IllegalArgumentException | IOException e1) {
			e1.printStackTrace();
		} 
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line; 
			while ((line = br.readLine()) != null) {
				indexmapping = indexmapping + line;
			  } 
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return indexmapping;
		
	}
	
	/**
	 * Locates the mapping file within the 'mappings' directory and returns a file path
	 * 
	 * @return String 
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	private String locateMappings(String indexName) throws IllegalArgumentException, IOException {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		if (path.endsWith("bin/"))
			path = path.substring(0, path.lastIndexOf("bin/"));
		File file = new File(path + "mappings/"+ indexName + ".json");
		checkPropertiesFilePath(file.toPath());

		return file.getPath();
	}
	/**
	 * Checks if a file exists
	 * 
	 * @param path
	 */
	private void checkPropertiesFilePath(Path path) {
		
		if (!Files.exists(path)) {
			System.err.println("The file " + path + " has not been found");
		}
	}
	
	/**
	 * Maps the necessary information from a 'GithubIssue' object to a 'GitHubIssueDocument'
	 * 
	 * @param issue
	 * @param projectName
	 * @param uid
	 * @return GitHubIssueDocument
	 */
	private GitHubIssueDocument gitHubIssueObjMap(GitHubIssue issue, String projectName, String uid) {
		
		GitHubIssueDocument gitIssueDoc = new GitHubIssueDocument();
		gitIssueDoc.setAssignee(issue.getAssignee());
		gitIssueDoc.setBody(issue.getBody());
		gitIssueDoc.setBug_id(issue.getBugId());
		gitIssueDoc.setClosed_at(issue.getClosedTime());
		gitIssueDoc.setCreated_at(issue.getCreationTime());
		gitIssueDoc.setCreator(issue.getCreator());
		gitIssueDoc.setHtml_url(issue.getHtmlUrl());
		gitIssueDoc.setNumber_of_comments(issue.getNumComments());
		gitIssueDoc.setProject(projectName);
		gitIssueDoc.setSummary(issue.getSummary());
		gitIssueDoc.setUid(uid);
		gitIssueDoc.setUpdated_at(issue.getUpdatedTime());
		gitIssueDoc.setUrl(issue.getUrl());
		
		
		if (!issue.getGithubComments().isEmpty()) {
		List<GitHubCommentDocument> comments = new ArrayList<>();
		for(GitHubComment comment : issue.getGithubComments()) {
			
			comments.add(gitHubCommentObjMap(comment));
			
		}
		
		gitIssueDoc.setComment(comments);
		}
		
		// NLP METRIC INFORMATION
		// gitIssueDoc.setContains_code();
		// gitIssueDoc.setPlain_text(plain_text);
		// gitIssueDoc.setEmotion(emotion);
		// gitIssueDoc.setRequest(request);
		// gitIssueDoc.setSentiment(sentiment);
		// gitIssueDoc.setSeverity(severity);

		return gitIssueDoc;
	}
	
	/**
	 * Maps the necessary information from a 'GithubPullRequest' object to a 'GitHubIssuePullRequest'
	 * 
	 * @param issue
	 * @param projectName
	 * @param uid
	 * @return GitHubPullRequestDocument
	 */
	private GitHubPullRequestDocument gitHubPullRequestObjMap(GitHubPullRequest pullRequest, String projectName, String uid) throws NullPointerException{
	
		GitHubPullRequestDocument gitHubPullRequestDocument = new GitHubPullRequestDocument();
		gitHubPullRequestDocument.setTitle(pullRequest.getTitle());
		gitHubPullRequestDocument.setBody(pullRequest.getBody());
		gitHubPullRequestDocument.setClosed_at(pullRequest.getClosedAt());
		gitHubPullRequestDocument.setCreated_at(pullRequest.getCreatedAt());
		gitHubPullRequestDocument.setCreator(pullRequest.getUser());
		gitHubPullRequestDocument.setAdditions(pullRequest.getAdditions());
		gitHubPullRequestDocument.setDiff_url(pullRequest.getDiffUrl());
		gitHubPullRequestDocument.setHtml_url(pullRequest.getHtmlUrl());
		gitHubPullRequestDocument.setIssue_url(pullRequest.getIssueUrl());
		gitHubPullRequestDocument.setMergable(pullRequest.isMergeable());
		gitHubPullRequestDocument.setIs_merged(pullRequest.isMerged());
		gitHubPullRequestDocument.setMerged_at(pullRequest.getMergedAt());
		gitHubPullRequestDocument.setMerged_by(pullRequest.getMergedBy());
		
		//gitHubPullRequestDocument.setMilestone(pullRequest.getMilestone());
			
		
		
		gitHubPullRequestDocument.setNumber(pullRequest.getNumber());
		gitHubPullRequestDocument.setNumber_of_comments(pullRequest.getComments());
		gitHubPullRequestDocument.setNumber_of_commits(pullRequest.getCommits());
		gitHubPullRequestDocument.setNumber_of_deletions(pullRequest.getDeletions());
		gitHubPullRequestDocument.setPatch_url(pullRequest.getPatchUrl());
		gitHubPullRequestDocument.setProject(projectName);
		gitHubPullRequestDocument.setPull_request_id(pullRequest.getId());
		gitHubPullRequestDocument.setState(pullRequest.getState());
		gitHubPullRequestDocument.setUid(uid);
		gitHubPullRequestDocument.setUpdated_at(pullRequest.getUpdatedAt());
		gitHubPullRequestDocument.setUrl(pullRequest.getUrl());
		
		//NLP components
//		gitHubPullRequestDocument.setContains_code(contains_code);
//		gitHubPullRequestDocument.setEmotion(emotion);
//		gitHubPullRequestDocument.setPlain_text(plain_text);
//		gitHubPullRequestDocument.setRequest(request);
//		gitHubPullRequestDocument.setSentiment(sentiment);
//		gitHubPullRequestDocument.setSeverity(severity);
		
		return gitHubPullRequestDocument; 
	}
	
	
	private GitHubCommentDocument gitHubCommentObjMap(GitHubComment comment) {
		
		GitHubCommentDocument gitHubCommentDocument = new GitHubCommentDocument();
		
		gitHubCommentDocument.setBody(comment.getText());
		gitHubCommentDocument.setComment_id(comment.getCommentId());
		gitHubCommentDocument.setCreator(comment.getCreator());
		gitHubCommentDocument.setCreated_at(comment.getCreationTime());
		gitHubCommentDocument.setUpdated_at(comment.getUpdatedAt());
		gitHubCommentDocument.setUrl(comment.getUrl());
		
		//NLP components
		//gitHubCommentDocument.setContains_code(contains_code);
		//gitHubCommentDocument.setEmotion(emotion);
		//gitHubCommentDocument.setPlain_text(plain_text);
		//gitHubCommentDocument.setRequest(request);
		//gitHubCommentDocument.setSentiment(sentiment);
		//gitHubCommentDocument.setSeverity(severity);
		
		
		
		return gitHubCommentDocument;
	}
	
	/**
	 * Maps the necessary information from a 'MantisIssue' object to a 'MantisIssueDocument'
	 * 
	 * @param issue
	 * @param projectName
	 * @param uid
	 * @return MantisIssueDocument
	 */
	private MantisIssueDocument mantisIssueObjMap(MantisIssue issue, String projectName, String uid){
		
		MantisIssueDocument document = new MantisIssueDocument();
		document.setAssignee(issue.getHandler());
		document.setBody(issue.getSummary());
		document.setBug_id(issue.getBugId());		
		document.setComments(issue.getNotes());
		document.setCreated_at(issue.getCreationTime());
		document.setCreator(issue.getCreator());
		document.setDescription(issue.getDescription());
		document.setOperating_system(issue.getOperatingSystem());
		document.setOs_build(issue.getOs_build());
		document.setPlatform(issue.getPlatform());
		document.setProject(projectName);
		document.setPrority(issue.getPriority());
		document.setReproducibility(issue.getReproducibility());
		document.setResoultion(issue.getResolution());
		document.setStatus(issue.getStatus());
		document.setTags(issue.getTags());
		document.setUid(uid);
		document.setUpdated_at(issue.getUpdated_at());
		document.setView_state(issue.getView_state());
		
		//NLP METRIC INFORMATION
//		document.setContains_code(contains_code);
//		document.setEmotion(emotion);
//		document.setPlain_text(plain_text);
//		document.setRequest(request);
//		document.setSentiment(sentiment);
//		document.setSeverity(severity);
		
		return document;
	}

	/**
	 * Maps the necessary information from a 'RedmineIssue' object to a 'RemineIssueDocument'
	 * 
	 * @param issue
	 * @param projectName
	 * @param uid
	 * @return RedmineIssueDocument
	 */
	private RedmineIssueDocument redmineIssueObjMap(RedmineIssue issue, String projectName, String uid) {
		
		RedmineIssueDocument redmineIssueDocument = new RedmineIssueDocument();
		
		redmineIssueDocument.setIssue_id(issue.getBugId());
		redmineIssueDocument.setBody(issue.getDescription());
		redmineIssueDocument.setCategory(issue.getCategory());
		redmineIssueDocument.setCategory_Id(issue.getCreatorId());
		redmineIssueDocument.setCreated_at(issue.getCreationTime());
		redmineIssueDocument.setCreator(issue.getCreator());
		redmineIssueDocument.setPriority_Id(issue.getPriorityId());
		redmineIssueDocument.setProject(projectName);
		redmineIssueDocument.setProject_Id(issue.getProjectId());
		redmineIssueDocument.setStatus_Id(issue.getStatusId());
		redmineIssueDocument.setSubject(issue.getSubject());
		redmineIssueDocument.setTracker(issue.getTracker());
		redmineIssueDocument.setTracker_Id(issue.getTrackerId());
		redmineIssueDocument.setUid(uid);
		redmineIssueDocument.setUpdated_at(issue.getUpdateDate());
		
		//NLP METRIC INFORMATION
		// redmineIssueDoc.setContains_code();
		// redmineIssueDoc.setPlain_text(plain_text);
		// redmineIssueDoc.setEmotion(emotion);
		// redmineIssueDoc.setRequest(request);
		// redmineIssueDoc.setSentiment(sentiment);
		// redmineIssueDoc.setSeverity(severity);
		
		return redmineIssueDocument;
	}
	
	/**
	 * Maps the necessary information from a 'RedmineCommnet' object to a 'RemineCommentDocument'
	 * 
	 * @param issue
	 * @param projectName
	 * @param uid
	 * @return RedmineCommentDocument
	 */
	private RedmineCommentDocument redmineCommentObjMap(RedmineComment comment, String projectName, String uid) {
		
		RedmineCommentDocument redmineCommentDocument = new RedmineCommentDocument();
		
		redmineCommentDocument.setBody(comment.getText());
		redmineCommentDocument.setCreated_at(comment.getCreationTime());
		redmineCommentDocument.setCreator(comment.getCreator());
		redmineCommentDocument.setCreatorId(comment.getCreatorId());
		redmineCommentDocument.setDetails(comment.getDetails());
		redmineCommentDocument.setProject(projectName);
		redmineCommentDocument.setUid(uid);
	
		//NLP METRIC INFORMATION
		// redmineCommentDocument.setContains_code();
		// redmineCommentDocument.setPlain_text(plain_text);
		// redmineIssueDoc.setEmotion(emotion);
		// redmineIssueDoc.setRequest(request);
		// redmineIssueDoc.setSentiment(sentiment);
		// redmineIssueDoc.setSeverity(severity);
		
		return redmineCommentDocument;
	}

	/**
	 * Maps the necessary information from a 'BitbucketIssue' object to a 'BitbucketIssueDocument'
	 * 
	 * @param issue
	 * @param projectName
	 * @param uid
	 * @return BitbucketIssueDocument
	 */
	private BitbucketIssueDocument bitbucketIssueObjMap(BitbucketIssue issue, String projectName, String uid) {
		
		BitbucketIssueDocument bitbucketIssueDocument = new BitbucketIssueDocument();
		//TODO Map once reader is fixed.
		
		
		
		
		
		return bitbucketIssueDocument;
	}
	
	
	
	/**
	 * Maps the necessary information from a 'JiraIssue' object to a 'JiraIssueDocument'
	 * 
	 * @param issue
	 * @param projectName
	 * @param uid
	 * @return JiraIssueDocument
	 */
	private JiraIssueDocument jiraIssueObjMap(JiraIssue issue, String projectName, String uid) {
		
		JiraIssueDocument jiraIssueDocument = new JiraIssueDocument();
		jiraIssueDocument.setAffectedVersions(issue.getAffectedVersions());
		jiraIssueDocument.setAssignee(issue.getAssignee());
		jiraIssueDocument.setBody(issue.getDescription());
		jiraIssueDocument.setCreated_at(issue.getCreationTime());
		jiraIssueDocument.setCreator(issue.getCreator());
		jiraIssueDocument.setDueDate(issue.getDueDate());
		jiraIssueDocument.setExpandables(issue.getExpandables());
		jiraIssueDocument.setFixVersions(issue.getFixVersions());
		jiraIssueDocument.setIssueType(issue.getIssueType());
		jiraIssueDocument.setKey(issue.getKey());
		jiraIssueDocument.setLabels(issue.getLabels());
		jiraIssueDocument.setNumWatchers(issue.getNumWatchers());
		jiraIssueDocument.setProject(projectName);
		jiraIssueDocument.setResolutionDate(issue.getResolutionDate());
		jiraIssueDocument.setSummary(issue.getSummary());
		jiraIssueDocument.setUid(uid);
		jiraIssueDocument.setUrl(issue.getUrl());
		
		//NL Components
		//jiraIssueDocument.setContains_code(contains_code);
		//jiraIssueDocument.setEmotion(emotion);
		//jiraIssueDocument.setPlain_text(plain_text);
		//jiraIssueDocument.setRequest(request);
		//jiraIssueDocument.setSentiment(sentiment);
		//jiraIssueDocument.setSeverity(severity);
		return  jiraIssueDocument;
	}
	
	/**
	 * Maps the necessary information from a 'JiraComment' object to a 'JiraCommentDocument'
	 * 
	 * @param issue
	 * @param projectName
	 * @param uid
	 * @return JiraCommentDocument
	 */
	private JiraCommentDocument jiraCommentObjMap(JiraComment comment, String projectName, String uid) {
		
		JiraCommentDocument jiraCommentDocument = new JiraCommentDocument();
		jiraCommentDocument.setBody(comment.getText());
		jiraCommentDocument.setBug_Id(comment.getBugId());
		jiraCommentDocument.setComment_Id(comment.getCommentId());
		jiraCommentDocument.setCreated_at(comment.getCreationTime());
		jiraCommentDocument.setCreator(comment.getCreator());
		jiraCommentDocument.setProject(projectName);
		jiraCommentDocument.setUid(uid);
		jiraCommentDocument.setUpdateAuthor(comment.getUpdateAuthor());
		jiraCommentDocument.setUrl(comment.getUrl());
		
		//NL Components
		//jiraCommentDocument.setContains_code(contains_code);
		//jiraCommentDocument.setEmotion(emotion);
		//jiraCommentDocument.setPlain_text(plain_text);
		//jiraCommentDocument.setRequest(request);
		//jiraCommentDocument.setSentiment(sentiment);
		
		
		return  jiraCommentDocument;
	}
	
	
	private void clearDB(IndexTransMetric db) {
		
		//TODO DROP DB TABLES!
		db.sync();
	}
}