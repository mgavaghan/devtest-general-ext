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
 * writeSubXML()
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class WriteSubXMLBuilder implements MemberBuilder
{
	/** The list of packages this builder depends on. */
	static private final Set<String> sPackages = new HashSet<String>();

	static
	{
		// build package list
		sPackages.add("java.io.PrintWriter");
		sPackages.add("com.itko.util.XMLUtils");
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
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		String format = parent.readResource("step/impl/WriteSubXML.txt");
		StringBuilder setters = new StringBuilder();

		JSONObject fields = ImplFieldsBuilder.getUnqualifiedFields(config);
		if (fields != null)
		{
			for (String key : fields.keySet())
			{
				String fieldType = fields.get(key).getValue().toString();

				if ("Boolean".equals(fieldType))
				{
					setters.append(MessageFormat.format("      XMLUtils.streamTagAndChild(pw, \"{0}\", get{1}().toString());{2}", key, ImplFieldsBuilder.camelCase(key), parent.getEOL()));
				}
				else
				{
					setters.append(MessageFormat.format("      XMLUtils.streamTagAndChild(pw, \"{0}\", get{1}());{2}", key, ImplFieldsBuilder.camelCase(key), parent.getEOL()));
				}
			}
		}

		builder.append(MessageFormat.format(format, setters.toString()));
	}
}
