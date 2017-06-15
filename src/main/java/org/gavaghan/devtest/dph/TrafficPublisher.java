package org.gavaghan.devtest.dph;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.gavaghan.json.JSONNull;
import org.gavaghan.json.JSONNumber;
import org.gavaghan.json.JSONObject;
import org.gavaghan.json.JSONString;
import org.gavaghan.json.JSONValue;

import com.itko.activemq.ActiveMQConnectionFactory;
import com.itko.commons.logging.Log;
import com.itko.commons.logging.LogFactory;
import com.itko.jms.Connection;
import com.itko.jms.JMSException;
import com.itko.jms.Message;
import com.itko.jms.MessageConsumer;
import com.itko.jms.MessageProducer;
import com.itko.jms.Session;
import com.itko.jms.TextMessage;
import com.itko.jms.Topic;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.vse.stateful.model.Request;
import com.itko.lisa.vse.stateful.model.TransientResponse;
import com.itko.lisa.vse.stateful.protocol.DataProtocol;
import com.itko.util.Parameter;

/**
 * Publish the request and response to an Active MQ topic.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class TrafficPublisher extends DataProtocol
{
	/** Logger. */
	static private final Log LOG = LogFactory.getLog(TrafficPublisher.class);

	/** Transaction ID value. */
	static private final AtomicLong sTxnID = new AtomicLong();

	/** Transaction ID key. */
	static private final String TXN_ID_KEY = "TrafficPublisher.TXN_ID";

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
	 * Build a JSON object out of the request.
	 * 
	 * @param request
	 * @param txnId
	 * @return
	 */
	private JSONObject buildJSONRequest(Request request, long txnId)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		JSONObject json = new JSONObject();
		json.put("version", new JSONString("1.0"));
		json.put("txnId", new JSONNumber(txnId));
		json.put("type", new JSONString("request"));
		json.put("time", new JSONString(dateFormat.format(new Date())));
		json.put("operation", getStringOrNull(request.getOperation()));
		json.put("body", getStringOrNull(request.getBodyAsString()));

		// add arguments
		JSONObject args = new JSONObject();
		for (Parameter param : request.getArguments())
		{
			args.put(param.getName(), getStringOrNull(param.getValue()));
		}
		json.put("arguments", args);

		// add metadata
		JSONObject metadata = new JSONObject();
		for (Parameter param : request.getMetaData())
		{
			metadata.put(param.getName(), getStringOrNull(param.getValue()));
		}
		json.put("metadata", metadata);

		return json;
	}

	/**
	 * Build a JSON object out of the response.
	 * 
	 * @param response
	 * @param txnId
	 * @return
	 */
	private JSONObject buildJSONResponse(TransientResponse response, Long txnId)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		JSONObject json = new JSONObject();
		json.put("version", new JSONString("1.0"));
		if (txnId != null) json.put("txnId", new JSONNumber(txnId.longValue()));
		json.put("type", new JSONString("response"));
		json.put("time", new JSONString(dateFormat.format(new Date())));
		json.put("id", new JSONNumber(response.getId()));
		json.put("body", getStringOrNull(response.getBodyAsString()));

		// add metadata
		JSONObject metadata = new JSONObject();
		for (Parameter param : response.getMetaData())
		{
			metadata.put(param.getName(), getStringOrNull(param.getValue()));
		}
		json.put("metadata", metadata);

		return json;
	}

	/**
	 * Publish the JSON to the configured topic.
	 * 
	 * @param json
	 */
	private void publishJSON(JSONObject json)
	{
		String brokerUrl = "tcp://localhost:61616";
		String brokerUsername = "";
		String brokerPassword = "";
		String topicName = "MyTopic";

		ActiveMQConnectionFactory connFactory = new ActiveMQConnectionFactory(brokerUsername, brokerPassword, brokerUrl);
		Connection connection = null;
		Session session = null;
		MessageProducer messageProducer = null;

		try
		{
			connection = connFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(topicName);
			messageProducer = session.createProducer(topic);
			TextMessage message = session.createTextMessage();

			message.setText(json.toPrettyString());

			messageProducer.send(message);
		}
		catch (JMSException exc)
		{
			LOG.warn("Failed to publich request to a topic", exc);
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
	 * Publish an incoming request.
	 */
	@Override
	public void updateRequest(TestExec testExec, Request request)
	{
		long txnId = sTxnID.getAndIncrement();
		testExec.setStateValue(TXN_ID_KEY, new Long(txnId));

		JSONObject json = buildJSONRequest(request, txnId);

		publishJSON(json);
	}

	/**
	 * Publish a response on playback.
	 */
	@Override
	public void updateResponse(TestExec testExec, TransientResponse response)
	{
		Long txnId = (Long) testExec.getStateValue(TXN_ID_KEY);

		JSONObject json = buildJSONResponse(response, txnId);

		publishJSON(json);
	}

	static public void main(String[] args) throws Exception
	{
		String brokerUrl = "tcp://localhost:61616";
		String brokerUsername = "";
		String brokerPassword = "";
		String topicName = "MyTopic";

		// create a Connection Factory
		ActiveMQConnectionFactory connFactory = new ActiveMQConnectionFactory(brokerUsername, brokerPassword, brokerUrl);

		// create a Connection
		Connection connection = connFactory.createConnection();
		connection.start();

		// create a Session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// create the Topic to which messages will be sent
		Topic topic = session.createTopic(topicName);

		// create a MessageProducer for sending messages
		MessageConsumer messageConsumer = session.createConsumer(topic);

		while (true)
		{
			Message received = messageConsumer.receive();
			if (received instanceof TextMessage)
			{
				TextMessage text = (TextMessage) received;
				System.out.println(text.getText());
			}
		}

		// connection.stop();
		// messageConsumer.close();
		// session.close();
		// connection.close();
		// System.out.println("Done");
	}
}
