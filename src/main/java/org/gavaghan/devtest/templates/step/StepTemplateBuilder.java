package org.gavaghan.devtest.templates.step;

import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;

/**
 * Template builder for custom steps.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class StepTemplateBuilder extends TemplateBuilder
{
	/* (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.TemplateBuilder#canBuild(org.gavaghan.json.JSONObject)
	 */
	@Override
	public boolean canBuild(JSONObject config)
	{
		String type = getString(config, "type");
		
		if (type == null)  return false;
		
		return "step".equals(type.toLowerCase());
	}

	/* (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.TemplateBuilder#build(org.gavaghan.json.JSONObject)
	 */
	@Override
	protected void build(JSONObject config) throws BuildException, IOException
	{
	}
}
