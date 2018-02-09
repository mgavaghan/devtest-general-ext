package org.gavaghan.devtest.templates.step.impl;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;

/**
 * Step type name.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class StepTypeNameBuilder implements MemberBuilder
{
	/** The list of packages this builder depends on. */
	static private final Set<String> sPackages = new HashSet<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gavaghan.devtest.templates.HasDependencies#getPackages()
	 */
	@Override
	public Set<String> getPackages(JSONObject config)
	{
		return sPackages;
	}

	@Override
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		String format = parent.readResource("step/impl/StepTypeName.txt");
		builder.append(MessageFormat.format(format, parent.getName()));
	}
}
