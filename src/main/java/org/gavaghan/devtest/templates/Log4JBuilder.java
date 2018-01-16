package org.gavaghan.devtest.templates;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.gavaghan.json.JSONObject;

import com.ibm.icu.text.MessageFormat;

/**
 * The Log4j member.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class Log4JBuilder implements MemberBuilder
{
	/** The list of packages this builder depends on. */
	static private final Set<String> sPackages = new HashSet<String>();

	static
	{
		// build package list
		sPackages.add("org.apache.log4j.LogManager");
		sPackages.add("org.apache.log4j.Logger");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.HasDependencies#getPackages(org.gavaghan.json.JSONObject)
	 */
	@Override
	public Set<String> getPackages(JSONObject config)
	{
		return sPackages;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.MemberBuilder#build(org.gavaghan.devtest.templates.TemplateBuilder, org.gavaghan.json.JSONObject, java.lang.StringBuilder)
	 */
	@Override
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		builder.append("   /** Our logger */").append(parent.getEOL());
		builder.append(MessageFormat.format("   static private final Logger LOG = LogManager.getLogger({0}Step.class);", parent.getName()));
		builder.append(parent.getEOL());
	}
}
