package org.gavaghan.devtest.templates.step;

import java.util.ArrayList;
import java.util.List;

import org.gavaghan.devtest.templates.FileBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;

/**
 * Template builder for custom steps.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class StepTemplateBuilder extends TemplateBuilder
{
	/** The FileBuilders that implement this TemplateBuilder. */
	static private final List<FileBuilder> sFileBuilders = new ArrayList<FileBuilder>();
	
	static
	{
		// build up the file builder list
		sFileBuilders.add(new ImplFileBuilder());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gavaghan.devtest.templates.TemplateBuilder#getFileBuilders()
	 */
	@Override
	protected List<FileBuilder> getFileBuilders()
	{
		return sFileBuilders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gavaghan.devtest.templates.TemplateBuilder#canBuild(org.gavaghan.json.
	 * JSONObject)
	 */
	@Override
	public boolean canBuild(JSONObject config)
	{
		String type = getString(config, "type");

		if (type == null) return false;

		return "step".equals(type.toLowerCase());
	}
}
