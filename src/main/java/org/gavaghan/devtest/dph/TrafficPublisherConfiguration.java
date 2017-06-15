package org.gavaghan.devtest.dph;

import java.io.PrintWriter;

import com.itko.lisa.vse.stateful.protocol.DataProtocolConfiguration;

/**
 * Publish the request and response to an Active MQ topic.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class TrafficPublisherConfiguration extends DataProtocolConfiguration
{
	@Override
	public void writeSubXML(final PrintWriter out)
	{
	}
}
