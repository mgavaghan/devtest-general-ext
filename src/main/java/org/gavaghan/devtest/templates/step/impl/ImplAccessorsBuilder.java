package org.gavaghan.devtest.templates.step.impl;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;
import org.gavaghan.json.JSONString;

/**
 * Render step accessors.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class ImplAccessorsBuilder implements MemberBuilder
{
	/** The list of packages this builder depends on. */
	static private final List<String> sPackages = Collections.unmodifiableList(new ArrayList<String>());

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
		JSONObject fields = ImplFieldsBuilder.getUnqualifiedFields(config);
		if (fields == null) return;
		
		String format = parent.readResource("step/impl/Accessors.txt");
		
		boolean first = true;

		for (String key : fields.keySet())
		{
			if (first)  first = false;
			else builder.append(parent.getEOL());

			String value = (String) ((JSONString) fields.get(key)).getValue();
			builder.append(MessageFormat.format(format, key, ImplFieldsBuilder.camelCase(key), value));
		}
	}
}
