package org.gavaghan.devtest.templates;

import java.util.List;

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
	 * @return
	 */
	public List<String> getPackages();
}
