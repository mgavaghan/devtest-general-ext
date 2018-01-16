package org.gavaghan.devtest.tph;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage outbound/inbound folder transactions during the recording process.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class FolderRecorder implements Runnable
{
	/** Logger. */
	static private final Logger LOG = LoggerFactory.getLogger(FolderRecorder.class);

	/** Recording strategy. */
	private ITransactionRecorder mRecorder;

	/** Outbound folder manager. */
	private FolderServer mOutboundFolder;

	/** Inbound folder manager. */
	private FolderServer mInboundFolder;

	/** Shutdown flag. */
	private AtomicBoolean mShutdown = new AtomicBoolean();

	/** Session identifier. */
	private String mSession;

	/**
	 * Manage the conversation.
	 */
	private void manageConversation()
	{
		LOG.debug("manageConversation(");
		while (!mShutdown.get())
		{
			// get the outbound file
			LOG.debug("Getting the next outbound file");
			FileContent outbound = mOutboundFolder.getNextFile();
			if (outbound == null) break;
			if (mShutdown.get()) break;

			// get the inbound file
			FileContent inbound = null;
			if (mInboundFolder != null)
			{
				LOG.debug("Getting the next inbound file");
				inbound = mInboundFolder.getNextFile();
				if (inbound == null) break;
			}
			if (mShutdown.get()) break;

			LOG.debug("About to record completed transaction");

			// record the result.
			if (mRecorder != null) mRecorder.record(mSession, outbound, inbound);
		}
	}

	/**
	 * Create a new recorder.
	 * 
	 * @param recorder
	 * @param outbound
	 * @param inbound
	 */
	public FolderRecorder(ITransactionRecorder recorder, FolderServer outbound, FolderServer inbound)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("outbound = " + outbound);
			LOG.debug("inbound = " + inbound);
		}

		mRecorder = recorder;
		mOutboundFolder = outbound;
		mInboundFolder = inbound;
		mShutdown.set(false);
	}

	/**
	 * Request the recorder shutdown.
	 */
	public void shutdown()
	{
		mShutdown.set(true);
	}

	@Override
	public void run()
	{
		LOG.info("Begin recording");

		try
		{
			// loop until a shutdown has been requested
			while (!mShutdown.get())
			{
				mSession = UUID.randomUUID().toString();

				// manage the conversation
				try
				{
					manageConversation();
				}
				finally
				{
					mOutboundFolder.stop();
					if (mInboundFolder == null) mInboundFolder.stop();
				}
			}
		}
		catch (Exception exc)
		{
			LOG.error("Recorder failed", exc);
		}

		LOG.info("End recording");
	}
}
