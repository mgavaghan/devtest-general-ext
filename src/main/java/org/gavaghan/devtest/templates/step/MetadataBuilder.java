package org.gavaghan.devtest.templates.step;

import java.io.IOException;
import java.util.List;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.FileBuilder;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;

import com.ibm.icu.text.MessageFormat;

/**
 * Create the *.lisaextensions file.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class MetadataBuilder extends FileBuilder
{
	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.HasDependencies#getPackages()
	 */
	@Override
	public List<String> getPackages(JSONObject config)
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.FileBuilder#getMemberBuilders()
	 */
	@Override
	protected List<MemberBuilder> getMemberBuilders()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.FileBuilder#getName(org.gavaghan.devtest.templates.TemplateBuilder, org.gavaghan.json.JSONObject)
	 */
	@Override
	public String getName(TemplateBuilder parent, JSONObject config) throws BuilderException, IOException
	{
		return parent.getName() + ".lisaextensions";
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.FileBuilder#build(org.gavaghan.devtest.templates.TemplateBuilder, org.gavaghan.json.JSONObject)
	 */
	@Override
	public String build(TemplateBuilder parent, JSONObject config) throws BuilderException, IOException
	{
		StringBuilder builder = new StringBuilder();
		
		String format = parent.readResource("step/Metadata.txt");
		builder.append(MessageFormat.format(format, parent.getPackageName(), parent.getName()));
		
		return builder.toString();
	}
}
