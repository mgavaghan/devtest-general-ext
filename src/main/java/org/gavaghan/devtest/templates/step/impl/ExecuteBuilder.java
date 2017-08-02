package org.gavaghan.devtest.templates.step.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;

import com.ibm.icu.text.MessageFormat;

/**
 * Render execute method.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class ExecuteBuilder implements MemberBuilder
{
	/** The list of packages this builder depends on. */
	static private final List<String> sPackages = new ArrayList<String>();

	static
	{
		sPackages.add("com.itko.lisa.test.TestExec");
		sPackages.add("com.itko.lisa.test.TestRunException");
		sPackages.add("com.itko.lisa.test.TestEvent");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gavaghan.devtest.templates.HasDependencies#getPackages()
	 */
	@Override
	public List<String> getPackages(JSONObject config)
	{
		return sPackages;
	}

	@Override
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		builder.append(MessageFormat.format(parent.readResource("step/impl/Execute.txt"), ""));
	}
}
