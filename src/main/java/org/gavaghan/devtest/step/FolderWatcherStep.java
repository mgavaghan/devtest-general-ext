package org.gavaghan.devtest.step;

import java.io.File;
import java.io.PrintWriter;

import org.gavaghan.devtest.tph.FileContent;
import org.gavaghan.devtest.tph.FolderProtocol;
import org.gavaghan.devtest.tph.FolderServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestEvent;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;
import com.itko.lisa.vse.stateful.BaseListenStep;
import com.itko.lisa.vse.stateful.model.Request;
import com.itko.util.Parameter;
import com.itko.util.XMLUtils;

/**
 * <p>"Listener" step for the Folder Protocol. This step monitors a folder for
 * the appearance of a file. The file is load, deleted, and then passed along as
 * a request.</p>
 * 
 * <p>All files are presumed to be UTF-8.</p>
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class FolderWatcherStep extends BaseListenStep
{
	/** Logger. */
	static private final Logger LOG = LoggerFactory.getLogger(FolderWatcherStep.class);

	/** The folder to watch. */
	private String mWatchFolder;

	/** The poll interval */
	private String mPollInterval;

	/**
	 * Create new FileFolderWatcherStep.
	 */
	public FolderWatcherStep()
	{
		LOG.debug("FileFolderWatcherStep()");
	}

	/**
	 * Set the watch folder.
	 * 
	 * @param folder
	 */
	public void setWatchFolder(String folder)
	{
		mWatchFolder = folder;
	}

	/**
	 * Get the watch folder.
	 * 
	 * @return
	 */
	public String getWatchFolder()
	{
		return mWatchFolder;
	}

	/**
	 * Set the poll interval
	 * 
	 * @param folder
	 */
	public void setPollInterval(String millis)
	{
		mPollInterval = millis;
	}

	/**
	 * Get the watch folder.
	 * 
	 * @return
	 */
	public String getPollInterval()
	{
		return mPollInterval;
	}

	@Override
	public String getTypeName() throws Exception
	{
		return "Folder Watcher";
	}

	@Override
	public boolean canDeployToVSE()
	{
		return true;
	}

	@Override
	public boolean isQuietTheDefault()
	{
		return false;
	}

	/**
	 * Load from file system.
	 */
	@Override
	public void initialize(TestCase testCase, Element element)
	{
		super.initialize(testCase, element);
		setWatchFolder(XMLUtils.findChildGetItsText(element, "watchFolder"));
		setPollInterval(XMLUtils.findChildGetItsText(element, "pollInterval"));
		setVSResourceName("Folder: " + getWatchFolder(), null);
	}

	/**
	 * Save to file system.
	 */
	@Override
	public void writeSubXML(PrintWriter pw)
	{
		super.writeSubXML(pw);
		XMLUtils.streamTagAndChild(pw, "watchFolder", getWatchFolder());
		XMLUtils.streamTagAndChild(pw, "pollInterval", getPollInterval());
	}

	/**
	 * Make sure we stop watching the folder after the test has ended.
	 */
	@Override
	public void destroy(TestExec testExec)
	{
		String watchFolder = testExec.parseInState(mWatchFolder);
		if (watchFolder != null) FolderServer.remove(new File(watchFolder));

		super.destroy(testExec);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void execute(TestExec testExec) throws TestRunException
	{
		// parse the parameters
		String watchFolder = testExec.parseInState(mWatchFolder);
		String pollIntervalStr = testExec.parseInState(mPollInterval);
		int pollInterval;

		try
		{
			pollInterval = Integer.parseInt(pollIntervalStr);
		}
		catch (NumberFormatException exc)
		{
			LOG.error("Failed to parse poll interval as an integer: " + pollIntervalStr + ".  Defaulting to 10 seconds.");
			testExec.raiseEvent(TestEvent.EVENT_WARNING, "Failed to parse poll interval as an integer.  Defaulting to 10 seconds.", pollIntervalStr);
			pollInterval = 10000;
		}

		// get the server
		FolderServer ffs = FolderServer.getOrCreate(new File(watchFolder), pollInterval);

		// get the next file
		FileContent content = ffs.getNextFile();

		if (content == null)
		{
			LOG.info("It appears the folder watcher has stopped: " + watchFolder);
			testExec.removeState(FolderProtocol.FILENAME_PROPERTY);
			testExec.setLastResponse(null);
		}
		else
		{
			Request request = new Request();

			request.setBody(content.getContent(), "UTF-8");
			request.getMetaData().addParameter(new Parameter(FolderProtocol.FILENAME_PROPERTY, content.getFilename().getName()));
			request.setOperation("AS2");

			testExec.setStateValue(FolderProtocol.FILENAME_PROPERTY, content.getFilename());

			setRequest(testExec, request);
		}
	}
}
