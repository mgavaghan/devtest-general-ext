package org.gavaghan.devtest.tph;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.gavaghan.devtest.step.FolderSaveStep;
import org.gavaghan.devtest.step.FolderWatcherStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.itko.lisa.gui.WizardStep;
import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.vse.stateful.ConversationalStep;
import com.itko.lisa.vse.stateful.model.Request;
import com.itko.lisa.vse.stateful.model.Response;
import com.itko.lisa.vse.stateful.model.Transaction;
import com.itko.lisa.vse.stateful.protocol.TransportProtocol;
import com.itko.lisa.vse.stateful.recorder.RecordingWizard;
import com.itko.lisa.vse.stateful.recorder.WizardPhase;
import com.itko.util.XMLUtils;

/**
 * Protocol handler for folder based protocol handling.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class FolderProtocol extends TransportProtocol implements ITransactionRecorder
{
	/** Logger. */
	static private final Logger LOG = LoggerFactory.getLogger(FolderProtocol.class);

	/** UTF-8 character set. */
	static private final Charset UTF8 = Charset.forName("UTF-8");

	/** Polling interval for recording. */
	static private final int RECORDING_POLL_INTERVAL = 1000;
	
	/** Property used to hold the filename. */
	static public final String FILENAME_PROPERTY = "folder.protocol.filename";

	/** The recorder. */
	private FolderRecorder mRecorder;

	/** Hang on to the testExec. */
	private TestExec mTestExec;

	/**
	 * Create a file object from a path.
	 * 
	 * @param testExec
	 * @param path
	 * @return
	 */
	private File createFile(TestExec testExec, String path)
	{
		String parsed = testExec.parseInState(path);
		return new File(parsed);
	}

	@Override
	protected void beginRecordProcess(TestExec testExec) throws Exception
	{
		if (LOG.isDebugEnabled()) LOG.debug("Starting recording on '" + getConfig().getOutboundFolder() + "' to '"
				+ getConfig().getInboundFolder() + "'");

		mTestExec = testExec;

		FolderServer outbound = FolderServer.create(createFile(testExec, getConfig().getOutboundFolder()),
				RECORDING_POLL_INTERVAL);
		FolderServer inbound = null;

		if (getConfig().getInboundFolder().trim().length() != 0) inbound = FolderServer
				.create(createFile(testExec, getConfig().getInboundFolder()), RECORDING_POLL_INTERVAL);

		mRecorder = new FolderRecorder(this, outbound, inbound);

		Thread thread = new Thread(mRecorder);
		thread.setName("FolderRecorder");
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	protected void endRecordProcess()
	{
		mRecorder.shutdown();
		FolderServer.remove(createFile(mTestExec, getConfig().getOutboundFolder()));
		if (getConfig().getInboundFolder().trim().length() != 0)
			FolderServer.remove(createFile(mTestExec, getConfig().getInboundFolder()));
		mTestExec = null;
	}

	/**
	 * Create default FileFolderProtocol.
	 */
	public FolderProtocol()
	{
		super(new FolderProtocolConfiguration());
	}

	/**
	 * Cast the configuration.
	 */
	@Override
	public FolderProtocolConfiguration getConfig()
	{
		return (FolderProtocolConfiguration) super.getConfig();
	}

	/**
	 * Create FileFolderProtocol from a configuration.
	 * 
	 * @param config
	 */
	public FolderProtocol(FolderProtocolConfiguration config)
	{
		super(config);
	}

	@Override
	public WizardStep[] getWizardSteps(RecordingWizard recordingWizard, WizardPhase phase)
	{
		if (phase == WizardPhase.CONFIGURATION)
			return new WizardStep[] { new FolderPairPanel(recordingWizard, this) };

		return null;
	}

	@Override
	public TestNode createListenStep(TestCase testCase)
	{
		LOG.debug("Creating a listen step");

		FolderWatcherStep listen = new FolderWatcherStep();

		listen.setName("Watch Folder: " + getConfig().getOutboundFolder());
		listen.setWatchFolder(getConfig().getOutboundFolder());
		listen.setPollInterval("10000");

		return listen;
	}

	@Override
	public TestNode createRespondStep(TestCase testCase)
	{
		LOG.debug("Creating a respond step");

		FolderSaveStep respond = new FolderSaveStep();

		respond.setName("Save Response");
		respond.setResponseListProperty("lisa.vse.response");
		respond.setFilenameProperty(FILENAME_PROPERTY);
		respond.setTargetFolder(getConfig().getInboundFolder());

		return respond;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void record(String session, FileContent requestContent, FileContent responseContent)
	{
		// create transactions
		Transaction txn = new Transaction();
		txn.setAllowDupSpecifics(true);

		// grab the payloads
		String requestPayload = new String(requestContent.getContent(), UTF8);
		String responsePayload = null;
		if (responseContent != null) responsePayload = new String(responseContent.getContent(), UTF8);

		// create request
		Request request = txn.getRequest();
		request.setOperation("AS2");
		request.setBody(requestPayload);

		request.getMetaData().put(ConversationalStep.SESSION_KEY, session);
		request.getMetaData().put(FILENAME_PROPERTY, requestContent.getFilename().getName());

		// create response
		if (responsePayload != null)
		{
			Response response = new Response();
			response.setBody(responsePayload);
			response.getMetaData().put(FILENAME_PROPERTY, responseContent.getFilename().getName());

			txn.addResponse(response);
		}

		// add transaction
		addTransaction(txn);
	}

	/**
	 * Load from file system.
	 */
	@Override
	public void initialize(Element element)
	{
		super.initialize(element);

		LOG.debug("initialize()");

		String outbound = XMLUtils.findChildGetItsText(element, "outbound");
		String inbound = XMLUtils.findChildGetItsText(element, "inbound");

		getConfig().setOutboundFolder(outbound);
		getConfig().setInboundFolder(inbound);
	}

	/**
	 * Save to file system.
	 */
	@Override
	public void writeSubXML(PrintWriter out)
	{
		super.writeSubXML(out);

		XMLUtils.streamTagAndChild(out, "outbound", getConfig().getOutboundFolder());
		XMLUtils.streamTagAndChild(out, "inbound", getConfig().getInboundFolder());
	}
}
