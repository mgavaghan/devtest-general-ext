package org.gavaghan.devtest.templates;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Exception thrown during the building process.
 *  
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class BuilderException extends Exception
{
	/**
	 * Create a new BuilderException.
	 * 
	 * @param message
	 * @param cause
	 */
	public BuilderException(String message, Exception cause)
	{
		super(message,cause);
	}
	
	/**
	 * Create a new BuilderException.
	 * 
	 * @param message
	 */
	public BuilderException(String message)
	{
		super(message);
	}
}
