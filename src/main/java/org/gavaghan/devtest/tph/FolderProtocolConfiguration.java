package org.gavaghan.devtest.tph;

import com.itko.lisa.vse.stateful.protocol.TransportProtocolConfiguration;

/**
 * Encapsulates the outbound/inbound folders for the folder protocol.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class FolderProtocolConfiguration extends TransportProtocolConfiguration
{
	public FolderProtocolConfiguration()
	{
		resetOutboundFolder();
		resetInboundFolder();
	}
	
	public String getOutboundFolder()
	{
		return getUnparsed("outbound");
	}

	public void setOutboundFolder(String outbound)
	{
		putUnparsed("outbound", outbound);
	}

	public void resetOutboundFolder()
	{
		putUnparsed("outbound", "");
	}
	
	public String getInboundFolder()
	{
		return getUnparsed("inbound");
	}

	public void setInboundFolder(String inbound)
	{
		putUnparsed("inbound", inbound);
	}

	public void resetInboundFolder()
	{
		putUnparsed("inbound", "");
	}
}
