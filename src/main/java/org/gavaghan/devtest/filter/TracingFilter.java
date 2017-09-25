package org.gavaghan.devtest.filter;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.gavaghan.json.JSONNull;
import org.gavaghan.json.JSONObject;
import org.gavaghan.json.JSONString;
import org.gavaghan.json.JSONValue;
import org.w3c.dom.Element;

import com.itko.activemq.ActiveMQConnectionFactory;
import com.itko.jms.Connection;
import com.itko.jms.JMSException;
import com.itko.jms.MessageProducer;
import com.itko.jms.Session;
import com.itko.jms.TextMessage;
import com.itko.jms.Topic;
import com.itko.lisa.test.FilterBaseImpl;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;
import com.itko.util.Parameter;
import com.itko.util.ParameterList;
import com.itko.util.XMLUtils;

/**
 * Publish tracing to an Active MQ topic.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class TracingFilter extends FilterBaseImpl
{
	/** Logger. */
	static private final Logger LOG = LogManager.getLogger(TracingFilter.class);

	static private final String BROKER_URL = "brokerUrl";
	static private final String BROKER_USERNAME = "brokerUsername";
	static private final String BROKER_PASSWORD = "brokerPassword";
	static private final String TOPIC = "topic";

	private String mBrokerURL;
	private String mBrokerUsername;
	private String mBrokerPassword;
	private String mTopic;

	/**
	 * Return a JSONNull or JSONString as appropriate.
	 * 
	 * @param value
	 * @return
	 */
	private JSONValue getStringOrNull(String value)
	{
		if (value == null) return new JSONNull();

		return new JSONString(value);
	}

	/**
	 * Add testExec properties to the JSON object.
	 * 
	 * @param json
	 * @param testExec
	 */
	private void addProperties(JSONObject json, TestExec testExec)
	{
		JSONObject properties = new JSONObject();
		Map<String, Object> state = testExec.getAllState();

		for (String key : state.keySet())
		{
			String value = testExec.getStateString(key, null);
			properties.put(key, getStringOrNull(value));
		}
		json.put("properties", properties);
	}

	/**
	 * Publish the JSON to the configured topic.
	 * 
	 * @param json
	 */
	private void publishJSON(JSONObject json, TestExec testExec)
	{
		LOG.debug("ENTER - publishJSON()");

		String brokerUrl = testExec.parseInState(mBrokerURL);
		String brokerUsername = testExec.parseInState(mBrokerUsername);
		String brokerPassword = testExec.parseInState(mBrokerPassword);
		String topicName = testExec.parseInState(mTopic);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Broker URL: " + brokerUrl);
			LOG.debug("Broker Username: " + brokerUsername);
			LOG.debug("Topic: " + topicName);
		}

		LOG.debug("Creating connection factory");
		ActiveMQConnectionFactory connFactory = new ActiveMQConnectionFactory(brokerUsername, brokerPassword, brokerUrl);
		Connection connection = null;
		Session session = null;
		MessageProducer messageProducer = null;

		try
		{
			LOG.debug("Creating connection");
			connection = connFactory.createConnection();
			LOG.debug("Creating session");
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			LOG.debug("Creating topic");
			Topic topic = session.createTopic(topicName);
			LOG.debug("Creating message producer");
			messageProducer = session.createProducer(topic);
			LOG.debug("Creating message");
			TextMessage message = session.createTextMessage();

			message.setText(json.toPrettyString());

			LOG.debug("Publishing message");
			messageProducer.send(message);
		}
		catch (JMSException exc)
		{
			LOG.warn("Failed to publish request to a topic", exc);
		}
		finally
		{
			try
			{
				if (messageProducer != null) messageProducer.close();
				if (session != null) session.close();
				if (connection != null) connection.close();
			}
			catch (JMSException ignored)
			{
				// no-op
			}
		}
	}

	/**
	 * Create a new TracingFilter.
	 */
	public TracingFilter()
	{
		markFilterAsGlobal(true);
		setThreadSafe(true);
	}

	@Override
	public String getTypeName()
	{
		return "Step Tracer";
	}

	@Override
	public ParameterList getParameters()
	{
		ParameterList p = new ParameterList();
		p.addParameter(new Parameter("Broker URL", BROKER_URL, mBrokerURL, TestExec.PROPERTY_PARAM_TYPE));
		p.addParameter(new Parameter("Username", BROKER_USERNAME, mBrokerUsername, TestExec.PROPERTY_PARAM_TYPE));
		p.addParameter(new Parameter("Password", BROKER_PASSWORD, mBrokerPassword, TestExec.PROPERTY_PARAM_TYPE));
		p.addParameter(new Parameter("Topic", TOPIC, mTopic, TestExec.PROPERTY_PARAM_TYPE));
		return p;
	}

	@Override
	public void initialize(Element elem) throws TestDefException
	{
		mBrokerURL = XMLUtils.findChildGetItsText(elem, BROKER_URL);
		mBrokerUsername = XMLUtils.findChildGetItsText(elem, BROKER_USERNAME);
		mBrokerPassword = XMLUtils.findChildGetItsText(elem, BROKER_PASSWORD);
		mTopic = XMLUtils.findChildGetItsText(elem, TOPIC);

		if (mBrokerURL == null) mBrokerURL = "tcp://localhost:61616";
		if (mBrokerUsername == null) mBrokerUsername = "";
		if (mBrokerPassword == null) mBrokerPassword = "";
		if (mTopic == null) mTopic = "";
	}

	@Override
	public boolean subPreFilter(TestExec testExec) throws TestRunException
	{
		LOG.debug("ENTER - subPreFilter()");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		JSONObject json = new JSONObject();
		json.put("type", new JSONString("trace-pre"));
		json.put("version", new JSONString("1.0"));
		
		// add testExec
		JSONObject testExecJSON = new JSONObject();
		addProperties(testExecJSON, testExec);
		json.put("testExec", testExecJSON);
		
		publishJSON(json, testExec);

		return false;
	}

	@Override
	public boolean subPostFilter(TestExec testExec) throws TestRunException
	{
		LOG.debug("ENTER - subPostFilter()");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		JSONObject json = new JSONObject();
		json.put("type", new JSONString("trace-post"));
		json.put("version", new JSONString("1.0"));
		
		// add testExec
		JSONObject testExecJSON = new JSONObject();
		addProperties(testExecJSON, testExec);
		json.put("testExec", testExecJSON);
		
		publishJSON(json, testExec);

		return false;
	}

	@Override
	public boolean isScopeLocal()
	{
		return false;
	}

	@Override
	public boolean isScopeGlobal()
	{
		return true;
	}
}
