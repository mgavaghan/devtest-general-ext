package org.gavaghan.devtest.templates;

import org.gavaghan.json.JSONObject;

/**
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public abstract class FileBuilder
{
	public abstract String getName(TemplateBuilder parent, JSONObject config);
	
	public void writePackage(TemplateBuilder parent, JSONObject config, StringBuilder builder)
	{
		builder.append("package ").append(parent.getPackageName()).append(";").append(parent.getEOL());
	}
	
	public void writeImports(TemplateBuilder parent, JSONObject config, StringBuilder builder)
	{
	}
	
	public void writeTypeHeader(TemplateBuilder parent, JSONObject config, StringBuilder builder)
	{
	}

	public String build(TemplateBuilder parent, JSONObject config)  throws BuilderException
	{
		StringBuilder builder = new StringBuilder();

		writePackage(parent, config, builder);
		writeImports(parent, config, builder);
		writeTypeHeader(parent, config, builder);

		return builder.toString();
	}
}
