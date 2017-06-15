package org.gavaghan.devtest.dph;

import com.itko.lisa.vse.stateful.protocol.DataProtocolConfiguration;

/**
 * Publish the request and response to an Active MQ topic.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class TrafficPublisherConfiguration extends DataProtocolConfiguration
{
	static private final String BROKER_URL = "brokerUrl";
	static private final String BROKER_USERNAME = "brokerUsername";
	static private final String BROKER_PASSWORD = "brokerPassword";
	static private final String TOPIC = "topic";

	/**
	 * Set default values.
	 */
	public TrafficPublisherConfiguration()
	{
		putUnparsed(BROKER_URL, "tcp://localhost:61616");
		putUnparsed(BROKER_USERNAME, "");
		putUnparsed(BROKER_PASSWORD, "");
		putUnparsed(TOPIC, "");
	}

	public String getBrokerURL()
	{
		return getUnparsed(BROKER_URL);
	}
	
	public void setBrokerURL(String value)
	{
		putUnparsed(BROKER_URL, value);
	}

	public String getBrokerUsername()
	{
		return getUnparsed(BROKER_USERNAME);
	}
	
	public void setBrokerUsername(String value)
	{
		putUnparsed(BROKER_USERNAME, value);
	}

	public String getBrokerPassword()
	{
		return getUnparsed(BROKER_PASSWORD);
	}
	
	public void setBrokerPassword(String value)
	{
		putUnparsed(BROKER_PASSWORD, value);
	}

	public String getTopic()
	{
		return getUnparsed(TOPIC);
	}
	
	public void setTopic(String value)
	{
		putUnparsed(TOPIC, value);
	}
}
