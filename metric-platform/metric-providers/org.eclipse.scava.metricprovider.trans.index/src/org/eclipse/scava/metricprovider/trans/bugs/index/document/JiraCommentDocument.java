package org.eclipse.scava.metricprovider.trans.bugs.index.document;

public class JiraCommentDocument extends CommentDocument{

	private String url;
    private String update_Author;
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @return the updateAuthor
	 */
	public String getUpdateAuthor() {
		return update_Author;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @param updateAuthor the updateAuthor to set
	 */
	public void setUpdateAuthor(String updateAuthor) {
		this.update_Author = updateAuthor;
	}
	
	
}
