package org.gavaghan.devtest.templates.step.impl;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;
import org.gavaghan.json.JSONString;
import org.gavaghan.json.JSONValue;

/**
 * Render step fields.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class ImplFieldsBuilder implements MemberBuilder
{
	/**
	 * Get fields, if any, on the type. This method assures the correctness of
	 * the fields entry.
	 * 
	 * @param config
	 * @return
	 */
	static public JSONObject getFields(JSONObject config) throws BuilderException
	{
		// look for a fields key
		JSONValue fieldsValue = config.get("fields");
		if (fieldsValue == null) return null;
		if (!(fieldsValue instanceof JSONObject)) throw new BuilderException("'fields' expected to be a JSONObject");

		// check correctness of fields
		JSONObject fields = (JSONObject) fieldsValue;

		for (String key : fields.keySet())
		{
			JSONValue value = fields.get(key);
			if (!(value instanceof JSONString)) throw new BuilderException("'" + key + " in 'fields' expected to be a JSONString");
		}

		return fields;
	}

	/**
	 * Get fields, if any, on the type. Use unqualified type names. This method
	 * assures the correctness of the fields entry.
	 * 
	 * @param config
	 * @return
	 */
	static public JSONObject getUnqualifiedFields(JSONObject config) throws BuilderException
	{
		JSONObject fields = getFields(config);
		if (fields == null) return null;

		JSONObject unqualified = new JSONObject();

		for (String key : fields.keySet())
		{
			JSONString jValue = (JSONString) fields.get(key);
			String value = (String) jValue.getValue();

			int dot = value.lastIndexOf('.');
			if (dot > 0) value = value.substring(dot + 1);

			unqualified.put(key, new JSONString(value));
		}

		return unqualified;
	}

	/**
	 * Capitalize the first letter in a string.
	 * 
	 * @param text
	 * @return
	 */
	static public String camelCase(String text)
	{
		return Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gavaghan.devtest.templates.HasDependencies#getPackages()
	 */
	@Override
	public List<String> getPackages(JSONObject config) throws BuilderException
	{
		List<String> packs = new ArrayList<String>();

		// look for fields
		JSONObject fields = getFields(config);
		if (fields == null) return packs;

		// find fully qualified fields
		for (String key : fields.keySet())
		{
			JSONString jValue = (JSONString) fields.get(key);
			String value = (String) jValue.getValue();

			if (value.indexOf('.') > 0) packs.add(value);
		}

		return packs;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.MemberBuilder#build(org.gavaghan.devtest.templates.TemplateBuilder, org.gavaghan.json.JSONObject, java.lang.StringBuilder)
	 */
	@Override
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		JSONObject fields = getUnqualifiedFields(config);
		if (fields == null) return;
		
		boolean first = true;

		for (String key : fields.keySet())
		{
			if (first)  first = false;
			else builder.append(parent.getEOL());
			
			String value = (String) ((JSONString) fields.get(key)).getValue();
			builder.append(MessageFormat.format("   /** {0} */{1}", key, parent.getEOL()));
			builder.append(MessageFormat.format("   private {0} m{1};{2}", value, camelCase(key), parent.getEOL()));
		}
	}
}
