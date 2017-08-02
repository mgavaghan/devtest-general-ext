package org.gavaghan.devtest.templates.step;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
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
	static private final List<String> sPackages = new ArrayList<String>();

	static
	{
		// build package list
		sPackages.add("org.apache.log4j.LogManager");
		sPackages.add("org.apache.log4j.Logger");
	}

	@Override
	public List<String> getPackages()
	{
		return sPackages;
	}

	@Override
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		builder.append("   /** Our logger */").append(parent.getEOL());
		builder.append(MessageFormat.format("   static private final Logger LOG = LogManager.getLogger({0}Step.class);", parent.getName()));
		builder.append(parent.getEOL());
	}
}
