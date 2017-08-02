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
 * Step initializer.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class StepInitializeBuilder implements MemberBuilder
{
	/** The list of packages this builder depends on. */
	static private final List<String> sPackages = new ArrayList<String>();

	static
	{
		// build package list
		sPackages.add("com.itko.lisa.test.TestCase");
		sPackages.add("com.itko.lisa.test.TestDefException");
		sPackages.add("com.itko.util.XMLUtils");
		sPackages.add("org.w3c.dom.Element");
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

	@Override
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		String format = parent.readResource("step/StepInitialize.txt");
		builder.append(MessageFormat.format(format, parent.getName()));
	}
}
