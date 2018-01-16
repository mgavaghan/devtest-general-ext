package org.gavaghan.devtest.templates.step.impl;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;
import org.gavaghan.json.JSONValue;

/**
 * Render step accessors.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class ImplAccessorsBuilder implements MemberBuilder
{
	/** The list of packages this builder depends on. */
	static private final Set<String> sPackages = Collections.unmodifiableSet(new HashSet<String>());

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
		JSONObject fields = ImplFieldsBuilder.getUnqualifiedFields(config);
		if (fields == null) return;
		
		String format = parent.readResource("step/impl/Accessors.txt");
		
		boolean first = true;

		for (String key : fields.keySet())
		{
			if (first)  first = false;
			else builder.append(parent.getEOL());

			JSONValue fieldType = fields.get(key);
			String value = fieldType.getValue().toString();		
			
			String content = MessageFormat.format(format, key, ImplFieldsBuilder.camelCase(key), value);
			
			builder.append(content);
		}
	}
}
