package org.eclipse.scava.repository.model.github;

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
	@Type(value = GitHubContent.class, name="org.eclipse.scava.repository.model.github.GitHubContent"), })
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubContent extends Object {

	protected String type;
	protected String envoding;
	protected int size;
	protected String name;
	protected String path;
	protected String sha;
	
	public String getType() {
		return type;
	}
	public String getEnvoding() {
		return envoding;
	}
	public int getSize() {
		return size;
	}
	public String getName() {
		return name;
	}
	public String getPath() {
		return path;
	}
	public String getSha() {
		return sha;
	}
	
}
