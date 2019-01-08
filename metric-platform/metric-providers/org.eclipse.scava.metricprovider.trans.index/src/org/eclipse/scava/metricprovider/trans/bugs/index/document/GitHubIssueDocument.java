package org.eclipse.scava.metricprovider.trans.bugs.index.document;


import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.Date;
import java.util.List;

import org.eclipse.scava.platform.bugtrackingsystem.github.GitHubComment;
import org.eclipse.scava.platform.delta.bugtrackingsystem.BugTrackingSystemComment;

//used for experiments
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
public class GitHubIssueDocument extends Document{

	String bug_id;
	String summary;
	String assignee;
	String html_url;
	String url;
	List<GitHubCommentDocument> comments;
	
	int number_of_comments;
	
	Date closed_at;

	/**
	 * @return the bug_id
	 */
	public String getBug_id() {
		return bug_id;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @return the assignee
	 */
	public String getAssignee() {
		return assignee;
	}

	/**
	 * @return the html_url
	 */
	public String getHtml_url() {
		return html_url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @return the number_of_comments
	 */
	public int getNumber_of_comments() {
		return number_of_comments;
	}

	/**
	 * @return the closed_at
	 */
	public Date getClosed_at() {
		return closed_at;
	}

	/**
	 * @param bug_id the bug_id to set
	 */
	public void setBug_id(String bug_id) {
		this.bug_id = bug_id;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @param assignee the assignee to set
	 */
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	/**
	 * @param html_url the html_url to set
	 */
	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param number_of_comments the number_of_comments to set
	 */
	public void setNumber_of_comments(int number_of_comments) {
		this.number_of_comments = number_of_comments;
	}

	/**
	 * @param closed_at the closed_at to set
	 */
	public void setClosed_at(Date closed_at) {
		this.closed_at = closed_at;
	}

	public List<GitHubCommentDocument> getComments() {
		return comments;
	}

	public void setComment(List<GitHubCommentDocument> comments) {
		this.comments = comments;
	}

	// experimental code
//	public static void main(String[] args) throws IOException, InterruptedException, NoSuchFieldException, SecurityException, ClassNotFoundException {
//		
//		GitHubIssueDocument issue = new GitHubIssueDocument();
//		
//		issue.setSummary("hello");
//		issue.setBody("testing");
//		
//		ObjectMapper mapper = new ObjectMapper();
//		
//		String jsonInString = mapper.writeValueAsString(issue);
//		
//		System.out.println(jsonInString);
//		
//		
//		
//		Class c = Class.forName("org.eclipse.scava.metricprovider.trans.index.document.GitHubIssueDocument");
//        System.out.println("Class Name :"+c.getName()+"\n");
//        Field allFieldArray[] = c.getDeclaredFields();
//         
//        int size = allFieldArray.length;
//        for(Field f : allFieldArray)
//         {
//            System.out.println(f.getName()); 
//         }  
//        
//	}	
}
