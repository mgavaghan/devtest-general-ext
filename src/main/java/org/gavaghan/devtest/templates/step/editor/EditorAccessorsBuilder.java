package org.gavaghan.devtest.templates.step.editor;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.devtest.templates.step.impl.ImplFieldsBuilder;
import org.gavaghan.json.JSONObject;

/**
 * Render step accessors.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class EditorAccessorsBuilder implements MemberBuilder
{
	/** The list of packages this builder depends on. */
	static private final List<String> sPackages = new ArrayList<String>();
	
	static
	{
		// build package list
		sPackages.add("javax.swing.JTextField");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.HasDependencies#getPackages(org.gavaghan.json.JSONObject)
	 */
	@Override
	public List<String> getPackages(JSONObject config) throws BuilderException
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
		JSONObject fields = ImplFieldsBuilder.getUnqualifiedFields(config);
		if (fields == null) return;
		
		String format = parent.readResource("step/editor/Accessors.txt");
		
		boolean first = true;

		for (String key : fields.keySet())
		{
			if (first)  first = false;
			else builder.append(parent.getEOL());

			builder.append(MessageFormat.format(format, key, ImplFieldsBuilder.camelCase(key)));
		}
	}
}
