package org.eclipse.scava.repository.model.eclipse;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import org.eclipse.scava.platform.factoids.*;
import org.eclipse.scava.repository.model.*;
import org.eclipse.scava.repository.model.bts.bugzilla.*;
import org.eclipse.scava.repository.model.cc.forum.*;
import org.eclipse.scava.repository.model.cc.nntp.*;
import org.eclipse.scava.repository.model.cc.wiki.*;
import org.eclipse.scava.repository.model.eclipse.*;
import org.eclipse.scava.repository.model.github.*;
import org.eclipse.scava.repository.model.googlecode.*;
import org.eclipse.scava.repository.model.metrics.*;
import org.eclipse.scava.repository.model.redmine.*;
import org.eclipse.scava.repository.model.sourceforge.*;
import org.eclipse.scava.repository.model.vcs.cvs.*;
import org.eclipse.scava.repository.model.vcs.git.*;
import org.eclipse.scava.repository.model.vcs.svn.*;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
	include=JsonTypeInfo.As.PROPERTY,
	property = "_type")
@JsonSubTypes({
	@Type(value = MailingList.class, name="org.eclipse.scava.repository.model.eclipse.MailingList"), })
@JsonIgnoreProperties(ignoreUnknown = true)
public class MailingList extends CommunicationChannel {

	protected String name;
	protected String description;
	protected String type;
	protected String archiveUrl;
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getType() {
		return type;
	}
	public String getArchiveUrl() {
		return archiveUrl;
	}
	
}
