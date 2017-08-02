package org.gavaghan.devtest.templates.step.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;

import com.ibm.icu.text.MessageFormat;

/**
 * Step type name.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class StepTypeNameBuilder implements MemberBuilder
{
	/** The list of packages this builder depends on. */
	static private final List<String> sPackages = new ArrayList<String>();

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
		String format = parent.readResource("step/impl/StepTypeName.txt");
		builder.append(MessageFormat.format(format, parent.getName()));
	}
}
