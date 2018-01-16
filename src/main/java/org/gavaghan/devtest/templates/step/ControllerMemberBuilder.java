package org.gavaghan.devtest.templates.step;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;

import com.ibm.icu.text.MessageFormat;

/**
 * Render controller body.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class ControllerMemberBuilder implements MemberBuilder
{
	/** The list of packages this builder depends on. */
	static private final Set<String> sPackages = new HashSet<String>();

	static
	{
		// build package list
		sPackages.add("java.io.PrintWriter");
		sPackages.add("javax.swing.Icon");
		sPackages.add("com.itko.lisa.core.ModuleLegacy");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.HasDependencies#getPackages(org.gavaghan.json.JSONObject)
	 */
	@Override
	public Set<String> getPackages(JSONObject config) throws BuilderException
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
		String format = parent.readResource("step/Controller.txt");
		
		builder.append(MessageFormat.format(format, parent.getName()));
	}
}
