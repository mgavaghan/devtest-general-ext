package org.gavaghan.devtest.step;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import org.gavaghan.devtest.tph.FolderProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestEvent;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.vse.stateful.BaseRespondStep;
import com.itko.lisa.vse.stateful.model.TransientResponse;
import com.itko.util.Parameter;
import com.itko.util.XMLUtils;

/**
 * <p>
 * "Respond" step for the Folder Protocol. File(s) from the service image are
 * stored to the file system.
 * </p>
 * <p>
 * All files are stored as UTF-8.
 * </p>
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class FolderSaveStep extends BaseRespondStep
{
	/** Logger. */
	static private final Logger LOG = LoggerFactory.getLogger(FolderSaveStep.class);

	/** Response property. */
	private String mResponseProperty;

	/** Filename property. */
	private String mFilenameProperty;

	/** Folder to save the file in. */
	private String mTargetFolder;

	/**
	 * Create a new FolderSaveStep.
	 */
	public FolderSaveStep()
	{
		super(true);
	}

	/**
	 * Get the response list property.
	 * 
	 * @return
	 */
	@Override
	public String getResponseListProperty()
	{
		return mResponseProperty;
	}

	/**
	 * Set the response property.
	 * 
	 * @param name
	 */
	@Override
	public void setResponseListProperty(String name)
	{
		mResponseProperty = name;
	}

	/**
	 * Get the filename property.
	 * 
	 * @return
	 */
	public String getFilenameProperty()
	{
		return mFilenameProperty;
	}

	/**
	 * Set the filename property.
	 * 
	 * @param name
	 */
	public void setFilenameProperty(String name)
	{
		mFilenameProperty = name;
	}

	/**
	 * Get the target folder.
	 * 
	 * @return
	 */
	public String getTargetFolder()
	{
		return mTargetFolder;
	}

	/**
	 * Set the target folder.
	 * 
	 * @param name
	 */
	public void setTargetFolder(String name)
	{
		mTargetFolder = name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.itko.lisa.test.NamedType#getTypeName()
	 */
	@Override
	public String getTypeName() throws Exception
	{
		return "Folder Save";
	}

	/*
	 * (non-Javadoc)
	 * @see com.itko.lisa.test.VSNode#canDeployToVSE()
	 */
	@Override
	public boolean canDeployToVSE()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.itko.lisa.test.VSNode#isQuietTheDefault()
	 */
	@Override
	public boolean isQuietTheDefault()
	{
		return false;
	}

	/**
	 * Load from file system.
	 */
	@Override
	public void initialize(TestCase testCase, Element element) throws TestDefException
	{
		LOG.debug("initialize()");

		try
		{
			super.initialize(testCase, element);
			setFilenameProperty(XMLUtils.findChildGetItsText(element, "filename"));
			setTargetFolder(XMLUtils.findChildGetItsText(element, "targetFolder"));
		}
		catch (Exception exc)
		{
			LOG.error("Failed in initialize", exc);
			throw exc;
		}
	}

	/**
	 * Save to file system.
	 */
	@Override
	public void writeSubXML(PrintWriter pw)
	{
		LOG.debug("writeSubXML()");

		try
		{
			super.writeSubXML(pw);
			XMLUtils.streamTagAndChild(pw, "filename", getFilenameProperty());
			XMLUtils.streamTagAndChild(pw, "targetFolder", getTargetFolder());
		}
		catch (Exception exc)
		{
			LOG.error("Failed in writeSubXML", exc);
			throw exc;
		}
	}

	/**
	 * gavmi01: I have no idea who calls this method.
	 */
	@Override
	protected String getResponsePropertyKey()
	{
		LOG.debug("getResponsePropertyKey()");
		return "lisa.vse.ff.response.list";
	}

	@Override
	protected void respond(TestExec testExec) throws Exception
	{
		List<TransientResponse> responses = getResponseList(testExec);
		String folder = testExec.parseInState(mTargetFolder);

		// loop over all responses and schedule the saving of the response.
		for (Object rspObj : responses)
		{
			TransientResponse response = (TransientResponse) rspObj;
			DelayedResponse responseAction = new DelayedResponse(testExec, response, folder, mFilenameProperty);
			respondLater(testExec, response, responseAction);
		}

		// lastly, remove the request filename from the state
		testExec.removeState(FolderProtocol.FILENAME_PROPERTY);
	}
}

/**
 * Handle deferred invocation.
 */
class DelayedResponse implements Runnable
{
	/** Logger. */
	static private final Logger LOG = LoggerFactory.getLogger(DelayedResponse.class);

	/** Our testExec. */
	private final TestExec mTestExec;

	/** Response to send. */
	private final TransientResponse mResponse;

	/** Folder. */
	private final String mFolder;

	/** Filename property. */
	private final String mFilenameProperty;

	/**
	 * 
	 * @param testExec
	 * @param response
	 * @param folder
	 * @param filenameProperty
	 */
	DelayedResponse(TestExec testExec, TransientResponse response, String folder, String filenameProperty)
	{
		mTestExec = testExec;
		mResponse = response;
		mFolder = folder;
		mFilenameProperty = filenameProperty;
	}

	@Override
	public void run()
	{
		String body = mTestExec.parseInState(mResponse.getBodyAsString());
		String filename;

		// get the filename
		@SuppressWarnings("deprecation")
		Parameter p = mResponse.getMetaData().getParameter(mFilenameProperty);
		filename = (p == null) ? (UUID.randomUUID().toString() + ".txt") : p.getValue();

		// write output file
		try (FileOutputStream fos = new FileOutputStream(mFolder + "/" + filename); OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8"))
		{
			osw.write(body);
			osw.flush();
		}
		catch (IOException exc)
		{
			LOG.error("Failed to save content to '" + filename + "'", exc);
			mTestExec.raiseEvent(TestEvent.EVENT_ERROR, "Failed to save file", exc.getMessage());
		}
	}
}