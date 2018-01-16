package org.gavaghan.devtest.tph;

/**
 * Interface to an instance that can record Folder Protocol traffic.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public interface ITransactionRecorder
{
	/**
	 * Record a request/response pair.
	 * 
	 * @param session
	 * @param request
	 * @param response
	 */
	public void record(String session, FileContent request, FileContent response);
}
