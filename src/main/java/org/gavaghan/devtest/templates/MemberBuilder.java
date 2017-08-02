package org.gavaghan.devtest.templates;

import java.io.IOException;

import org.gavaghan.json.JSONObject;

/**
 * Interface to a class that can build class members.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public interface MemberBuilder extends HasDependencies
{
	/**
	 * Build the class member.
	 * 
	 * @param parent
	 * @param config
	 * @param builder
	 * @return
	 * @throws BuilderException
	 * @throws IOException
	 */
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException;
}
