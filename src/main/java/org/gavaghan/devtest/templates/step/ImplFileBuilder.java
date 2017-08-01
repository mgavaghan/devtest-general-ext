package org.gavaghan.devtest.templates.step;

import org.gavaghan.devtest.templates.FileBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;

/**
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class ImplFileBuilder extends FileBuilder
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gavaghan.devtest.templates.FileBuilder#getName(org.gavaghan.devtest.
	 * templates.TemplateBuilder, org.gavaghan.json.JSONObject)
	 */
	@Override
	public String getName(TemplateBuilder parent, JSONObject config)
	{
		return parent.getName() + "Step.java";
	}
}
