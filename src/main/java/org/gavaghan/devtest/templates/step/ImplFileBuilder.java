package org.gavaghan.devtest.templates.step;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.FileBuilder;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;

import com.ibm.icu.text.MessageFormat;

/**
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class ImplFileBuilder extends FileBuilder
{
	/** The list of packages this builder depends on. */
	static private final List<String> sPackages = new ArrayList<String>();

	/** The list members in this typen. */
	static private final List<MemberBuilder> sMembers = new ArrayList<MemberBuilder>();

	static
	{
		// build package list
		sPackages.add("com.itko.util.CloneImplemented");
		sPackages.add("com.itko.lisa.test.TestNode");
		
		// build member list
		sMembers.add(new Log4JBuilder());
		sMembers.add(new StepTypeNameBuilder());
		sMembers.add(new StepInitializeBuilder());
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
	public List<String> getPackages()
	{
		return sPackages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gavaghan.devtest.templates.FileBuilder#getName(org.gavaghan.devtest.
	 * templates.TemplateBuilder, org.gavaghan.json.JSONObject)
	 */
	@Override
	public String getName(TemplateBuilder parent, JSONObject config) throws BuilderException, IOException
	{
		return parent.getName() + "Step.java";
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
		builder.append(MessageFormat.format("public class {0}Step extends TestNode implements CloneImplemented", parent.getName()));
		builder.append(parent.getEOL());
		builder.append("{").append(parent.getEOL());
	}
}
