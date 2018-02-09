package org.gavaghan.devtest.templates.step;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.FileBuilder;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;

/**
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class ControllerFileBuilder extends FileBuilder
{
	/** The list of packages this builder depends on. */
	static private final Set<String> sPackages = new HashSet<String>();

	/** The list members in this type. */
	static private final List<MemberBuilder> sMembers = new ArrayList<MemberBuilder>();

	static
	{
		// build package list
		sPackages.add("com.itko.lisa.editor.TestNodeInfo");

		// build member list
		sMembers.add(new ControllerMemberBuilder());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.FileBuilder#getMemberBuilders()
	 */
	@Override
	protected List<MemberBuilder> getMemberBuilders()
	{
		return sMembers;
	}

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
	public String getName(TemplateBuilder parent, JSONObject config) throws BuilderException, IOException
	{
		return parent.getName() + "Controller.java";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gavaghan.devtest.templates.FileBuilder#writeOpenType(org.gavaghan.
	 * devtest.templates.TemplateBuilder, org.gavaghan.json.JSONObject,
	 * java.lang.StringBuilder)
	 */
	@Override
	public void writeOpenType(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		builder.append(MessageFormat.format("public class {0}Controller extends TestNodeInfo", parent.getName()));
		builder.append(parent.getEOL());
		builder.append("{").append(parent.getEOL());
	}
}
