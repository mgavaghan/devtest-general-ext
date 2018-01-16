package org.gavaghan.devtest.tph.ptcp;

/**
 * Interface to an instance that can record traffic.
 * 
 * @author Mike Gavaghan
 */
public interface IPersistentTCPTransactionRecorder
{
	/**
	 * Record a request/response pair.
	 * 
	 * @param session
	 * @param request
	 * @param response
	 */
	public void record(String session, byte[] request, byte[] response);
}
