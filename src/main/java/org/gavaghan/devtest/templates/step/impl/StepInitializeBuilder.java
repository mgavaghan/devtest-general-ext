package org.gavaghan.devtest.templates.step.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
	static private final Set<String> sPackages = new HashSet<String>();

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
	public Set<String> getPackages(JSONObject config)
	{
		return sPackages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gavaghan.devtest.templates.MemberBuilder#build(org.gavaghan.devtest.
	 * templates.TemplateBuilder, org.gavaghan.json.JSONObject,
	 * java.lang.StringBuilder)
	 */
	@Override
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		String format = parent.readResource("step/impl/StepInitialize.txt");
		
		StringBuilder setters = new StringBuilder();
		
		JSONObject fields = ImplFieldsBuilder.getUnqualifiedFields(config);
		if (fields != null)
		{
			for (String key : fields.keySet())
			{
				setters.append(MessageFormat.format("      set{1}(XMLUtils.findChildGetItsText(elem, \"{0}\"));{2}", key, ImplFieldsBuilder.camelCase(key), parent.getEOL()));
			}
		}
		
		builder.append(MessageFormat.format(format, setters.toString()));
	}
}
