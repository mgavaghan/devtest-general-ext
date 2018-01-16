package org.gavaghan.devtest.templates;

import java.util.Set;

import org.gavaghan.json.JSONObject;

/**
 * Interface to a class that has package dependencies.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public interface HasDependencies
{
	/**
	 * Get the package dependencies.
	 * 
	 * @param config
	 * @return
	 * @throws BuilderException
	 */
	public Set<String> getPackages(JSONObject config) throws BuilderException;
}
