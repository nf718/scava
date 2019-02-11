package org.eclipse.scava.metricprovider.trans.sentimentclassification.model;

import com.mongodb.*;
import java.util.*;
import com.googlecode.pongo.runtime.*;
import com.googlecode.pongo.runtime.querying.*;


public class ForumPostDetectingSentimentClassification extends Pongo {
	
	
	
	public ForumPostDetectingSentimentClassification() { 
		super();
		FORUMID.setOwningType("org.eclipse.scava.metricprovider.trans.sentimentclassification.model.ForumPostDetectingSentimentClassification");
		TOPICID.setOwningType("org.eclipse.scava.metricprovider.trans.sentimentclassification.model.ForumPostDetectingSentimentClassification");
		POSTID.setOwningType("org.eclipse.scava.metricprovider.trans.sentimentclassification.model.ForumPostDetectingSentimentClassification");
		POLARITY.setOwningType("org.eclipse.scava.metricprovider.trans.sentimentclassification.model.ForumPostDetectingSentimentClassification");
	}
	
	public static StringQueryProducer FORUMID = new StringQueryProducer("forumId"); 
	public static StringQueryProducer TOPICID = new StringQueryProducer("topicId"); 
	public static StringQueryProducer POSTID = new StringQueryProducer("postId"); 
	public static StringQueryProducer POLARITY = new StringQueryProducer("polarity"); 
	
	
	public String getForumId() {
		return parseString(dbObject.get("forumId")+"", "");
	}
	
	public ForumPostDetectingSentimentClassification setForumId(String forumId) {
		dbObject.put("forumId", forumId);
		notifyChanged();
		return this;
	}
	public String getTopicId() {
		return parseString(dbObject.get("topicId")+"", "");
	}
	
	public ForumPostDetectingSentimentClassification setTopicId(String topicId) {
		dbObject.put("topicId", topicId);
		notifyChanged();
		return this;
	}
	public String getPostId() {
		return parseString(dbObject.get("postId")+"", "");
	}
	
	public ForumPostDetectingSentimentClassification setPostId(String postId) {
		dbObject.put("postId", postId);
		notifyChanged();
		return this;
	}
	public String getPolarity() {
		return parseString(dbObject.get("polarity")+"", "");
	}
	
	public ForumPostDetectingSentimentClassification setPolarity(String polarity) {
		dbObject.put("polarity", polarity);
		notifyChanged();
		return this;
	}
	
	
	
	
}