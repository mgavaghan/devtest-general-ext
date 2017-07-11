package org.gavaghan.devtest.dph;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.gavaghan.json.JSONNull;
import org.gavaghan.json.JSONNumber;
import org.gavaghan.json.JSONObject;
import org.gavaghan.json.JSONString;
import org.gavaghan.json.JSONValue;
import org.w3c.dom.Element;

import com.itko.activemq.ActiveMQConnectionFactory;
import com.itko.jms.Connection;
import com.itko.jms.JMSException;
import com.itko.jms.Message;
import com.itko.jms.MessageConsumer;
import com.itko.jms.MessageProducer;
import com.itko.jms.Session;
import com.itko.jms.TextMessage;
import com.itko.jms.Topic;
import com.itko.lisa.editor.Controller;
import com.itko.lisa.gui.WizardStep;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.vse.stateful.model.Request;
import com.itko.lisa.vse.stateful.model.Transaction;
import com.itko.lisa.vse.stateful.model.TransientResponse;
import com.itko.lisa.vse.stateful.protocol.DataProtocol;
import com.itko.lisa.vse.stateful.recorder.RecordingWizard;
import com.itko.lisa.vse.stateful.recorder.WizardPhase;
import com.itko.util.Parameter;

/**
 * Publish the request and response to an Active MQ topic.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class TrafficPublisher extends DataProtocol
{
	/** Logger. */
	static private final Logger LOG = LogManager.getLogger(TrafficPublisher.class);

	/** Transaction ID value. */
	static private final AtomicLong sTxnID = new AtomicLong();

	/** Transaction ID key. */
	static private final String TXN_ID_KEY = "TrafficPublisher.TXN_ID";

	/** Our editor. */
	private TrafficPublisherEditor mEditor;

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
	@SuppressWarnings("unused")
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
	 * Build a JSON object out of the request.
	 * 
	 * @param testExec
	 * @param request
	 * @param txnId
	 * @return
	 */
	private JSONObject buildJSONRequest(TestExec testExec, Request request, long txnId)
	{
		LOG.debug("ENTER - buildJSONRequest()");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		JSONObject json = new JSONObject();
		json.put("type", new JSONString("request"));
		json.put("version", new JSONString("1.0"));
		json.put("txnId", new JSONNumber(txnId));
		json.put("time", new JSONString(dateFormat.format(new Date())));
		json.put("project", getStringOrNull(testExec.getStateString("LISA_PROJ_NAME", null)));
		json.put("testCase", getStringOrNull(testExec.getStateString("testCase", null)));
		json.put("vseHost", getStringOrNull(testExec.getStateString("VSE_HOST", null)));
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

		// add properties
		// addProperties(json, testExec);

		return json;
	}

	/**
	 * Build a JSON object out of the response.
	 * 
	 * @param testExec
	 * @param response
	 * @param txnId
	 * @return
	 */
	private JSONObject buildJSONResponse(TestExec testExec, TransientResponse response, Long txnId)
	{
		LOG.debug("ENTER - buildJSONResponse()");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		JSONObject json = new JSONObject();
		json.put("type", new JSONString("response"));
		json.put("version", new JSONString("1.0"));
		if (txnId != null) json.put("txnId", new JSONNumber(txnId.longValue()));
		json.put("time", new JSONString(dateFormat.format(new Date())));
		json.put("project", getStringOrNull(testExec.getStateString("LISA_PROJ_NAME", null)));
		json.put("testCase", getStringOrNull(testExec.getStateString("testCase", null)));
		json.put("vseHost", getStringOrNull(testExec.getStateString("VSE_HOST", null)));
		Transaction txn = (Transaction) testExec.getStateObject("lisa.vse.matched.transaction");
		if (txn != null)
		{
			json.put("transactionId", new JSONNumber(txn.getId()));
			if (txn.getConversationName() != null) json.put("conversationName", new JSONNumber(txn.getId()));
		}

		json.put("body", getStringOrNull(testExec.parseInState(response.getBodyAsString())));

		// add metadata
		JSONObject metadata = new JSONObject();
		for (Parameter param : response.getMetaData())
		{
			metadata.put(param.getName(), getStringOrNull(testExec.parseInState(param.getValue())));
		}
		json.put("metadata", metadata);

		// add properties
		// addProperties(json, testExec);

		return json;
	}

	/**
	 * Publish the JSON to the configured topic.
	 * 
	 * @param json
	 */
	private void publishJSON(JSONObject json, TestExec testExec)
	{
		LOG.debug("ENTER - publishJSON()");

		String brokerUrl = testExec.parseInState(getConfig().getBrokerURL());
		String brokerUsername = testExec.parseInState(getConfig().getBrokerUsername());
		String brokerPassword = testExec.parseInState(getConfig().getBrokerPassword());
		String topicName = testExec.parseInState(getConfig().getTopic());

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
	 * @return
	 */
	TrafficPublisherEditor getEditor()
	{
		return mEditor;
	}

	/**
	 * @param editor
	 */
	void setEditor(final TrafficPublisherEditor editor)
	{
		mEditor = editor;
	}

	/**
	 * Create a new TrafficPublisher.
	 */
	public TrafficPublisher()
	{
		setConfig(new TrafficPublisherConfiguration());
	}

	/**
	 * Create a new TrafficPublisher.
	 * 
	 * @param config
	 */
	public TrafficPublisher(final TrafficPublisherConfiguration config)
	{
		setConfig(config);
	}

	/**
	 * Publish an incoming request.
	 */
	@Override
	public void updateRequest(TestExec testExec, Request request)
	{
		long txnId = sTxnID.getAndIncrement();
		testExec.setStateValue(TXN_ID_KEY, new Long(txnId));

		JSONObject json = buildJSONRequest(testExec, request, txnId);

		publishJSON(json, testExec);
	}

	/**
	 * Publish a response on playback.
	 */
	@Override
	public void updateResponse(TestExec testExec, TransientResponse response)
	{
		Long txnId = (Long) testExec.getStateValue(TXN_ID_KEY);

		JSONObject json = buildJSONResponse(testExec, response, txnId);

		publishJSON(json, testExec);
	}

	/**
	 * @return the config
	 */
	@Override
	public TrafficPublisherConfiguration getConfig()
	{
		return (TrafficPublisherConfiguration) super.getConfig();
	}

	/**
	 * This method is used to initialize this transport protocol from the
	 * specified XML element. Subclasses which override this method <b>MUST</b>
	 * invoke {@code super.initialize(element);} for things to be restored
	 * properly.
	 * 
	 * @param element
	 *           the element to initialize from.
	 */
	@Override
	public void initialize(final Element element)
	{
		super.initialize(element);
		getConfig().initialize(element);
	}

	/**
	 * @param controller
	 *           - the controller for the filter that this belongs to
	 * @return
	 */
	@Override
	public synchronized TrafficPublisherEditor getCustomEditor(final Controller controller)
	{
		if (getEditor() == null)
		{
			setEditor(new TrafficPublisherEditor());
		}
		return getEditor();
	}

	/**
	 * 
	 */
	@Override
	public void display()
	{
		LOG.debug("ENTER - display()");

		if (getEditor() != null)
		{
			getEditor().display(getConfig());
		}
	}

	/**
	 * 
	 */
	@Override
	public void save()
	{
		LOG.debug("ENTER - display()");

		if (getEditor() != null)
		{
			getEditor().save(getConfig());
		}
	}

	/**
	 * 
	 */
	@Override
	public String isEditorValid()
	{
		if (getEditor() != null)
		{
			return getEditor().isEditorValid(getConfig());
		}

		return null;
	}

	@Override
	public void writeSubXML(final PrintWriter out)
	{
		super.writeSubXML(out);
		getConfig().writeSubXML(out);
	}

	/**
	 * This method returns the list of recording wizard steps to use for this
	 * protocol.
	 * 
	 * @param wizard
	 *           the wizard the steps will be added to.
	 * @param phase
	 *           the phase of the recording wizard the steps should apply to.
	 * 
	 * @return an array of wizard step panels that will become part of the
	 *         recording wizard.
	 */
	@Override
	public WizardStep[] getWizardSteps(final RecordingWizard wizard, final WizardPhase phase)
	{
		WizardStep[] steps = null;

		switch (phase)
		{
		case FINALIZE:
			steps = new WizardStep[] { new TrafficPublisherWizardPanel(this, wizard.isRequestSide(this)) };
			break;
		default:
			break;
		}

		return steps;
	}

	static public void main(String[] args) throws Exception
	{
		String brokerUrl = "tcp://localhost:61616";
		String brokerUsername = "";
		String brokerPassword = "";
		String topicName = "SomeTopic";

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
