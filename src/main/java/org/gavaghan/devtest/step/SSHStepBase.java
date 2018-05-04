package org.gavaghan.devtest.step;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.crypto.Cipher;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestEvent;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.util.CloneImplemented;
import com.itko.util.XMLUtils;
import com.jcraft.jsch.DHG14;
import com.jcraft.jsch.JSch;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public abstract class SSHStepBase extends TestNode implements CloneImplemented
{
	/** Our logger */
	static private final Logger LOG = LogManager.getLogger(SSHStepBase.class);

	/** Flag indicating if classes have been preloaded. */
	static private boolean mPreloaded = false;

	/** Flag indicating any preload issues. */
	static private Exception mPreloadFailure;

	/** Username */
	private String mUsername;

	/** Hostname */
	private String mHostname;

	/** Port */
	private String mPort = "22";

	/** Timeout */
	private String mTimeout = "30";

	/** Password */
	private String mPassword;

	/** PrivateKey */
	private String mPrivateKey;

	/** Passphrase */
	private String mPassphrase;

	/**
	 * Notify listeners the preload is complete
	 * 
	 * @param exc
	 */
	static synchronized void setPreloadComplete(Exception exc)
	{
		mPreloaded = true;
		mPreloadFailure = exc;
		SSHStepBase.class.notifyAll();
	}

	/**
	 * Wait or preload to complete.
	 * 
	 * @throws Exception
	 */
	static synchronized void waitForPreload() throws Exception
	{
		while (!mPreloaded)
		{
			SSHStepBase.class.wait();
		}

		if (mPreloadFailure != null) throw new TestRunException("Failed to preload JSch types", mPreloadFailure);
	}
	
	static
	{
		// We're going to force the initialization of some JSch objects.
		// Otherwise, and initial execution takes a long time to complete. Then
		// again, there's evidence this is actually a network issue. RESEARCH!
		Runnable init = new Runnable()
		{
			@Override
			public void run()
			{
				JSch jsch = new JSch();

				try
				{
					Cipher.getInstance("AES/CTR/NoPadding");

					DHG14 kex = new DHG14();
					kex.init(jsch.getSession("localhost"), null, null, null, null);

					setPreloadComplete(null);
				}
				catch (Exception exc)
				{
					setPreloadComplete(exc);
				}
			}
		};

		Thread t = new Thread(init);
		t.setDaemon(true);
		t.setPriority(Thread.NORM_PRIORITY - 2);
		t.setName("SSHExecute-preloader");
		t.start();
	}

	/**
	 * Turn nulls into empty strings.
	 * 
	 * @param value
	 * @return
	 */
	protected String nullSafe(String value)
	{
		if (value == null) return "";
		return value;
	}

	/**
	 * Get configuration to build the session.
	 * 
	 * @param password
	 * @param privateKey
	 * @return
	 * @throws TestDefException
	 */
	protected Properties getConfiguration(String password, String privateKey) throws TestDefException
	{
		Properties config;
		config = new Properties();

		// don't check the host key
		config.put("StrictHostKeyChecking", "no");

		// determine preferred authentications
		if (password.length() > 0)
		{
			if (privateKey.length() > 0)
			{
				config.put("PreferredAuthentications", "publickey,password");
			}
			else
			{
				config.put("PreferredAuthentications", "password");
			}
		}
		else if (privateKey.length() > 0)
		{
			config.put("PreferredAuthentications", "publickey");
		}
		else
		{
			throw new TestDefException(getName(), "No authentication method provided", null);
		}

		return config;
	}

	/**
	 * Load a file.
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	protected byte[] loadFile(String filename) throws IOException
	{
		byte[] content;
		byte[] buffer = new byte[4096];

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); FileInputStream fis = new FileInputStream(filename))
		{
			for (;;)
			{
				int got = fis.read(buffer);
				if (got < 0) break;

				baos.write(buffer, 0, got);
			}

			content = baos.toByteArray();
		}

		return content;
	}

	/**
	 * Get Username.
	 *
	 * @return Username
	 */
	public String getUsername()
	{
		return mUsername;
	}

	/**
	 * Set Username.
	 *
	 * @param value
	 */
	public void setUsername(String value)
	{
		mUsername = value;
	}

	/**
	 * Get Hostname.
	 *
	 * @return Hostname
	 */
	public String getHostname()
	{
		return mHostname;
	}

	/**
	 * Set Hostname.
	 *
	 * @param value
	 */
	public void setHostname(String value)
	{
		mHostname = value;
	}

	/**
	 * Get Port.
	 *
	 * @return Port
	 */
	public String getPort()
	{
		return mPort;
	}

	/**
	 * Set Port.
	 *
	 * @param value
	 */
	public void setPort(String value)
	{
		mPort = value;
	}

	/**
	 * Get Timeout.
	 *
	 * @return Timeout
	 */
	public String getTimeout()
	{
		return mTimeout;
	}

	/**
	 * Set Timeout.
	 *
	 * @param value
	 */
	public void setTimeout(String value)
	{
		mTimeout = value;
	}

	/**
	 * Get Password.
	 *
	 * @return Password
	 */
	public String getPassword()
	{
		return mPassword;
	}

	/**
	 * Set Password.
	 *
	 * @param value
	 */
	public void setPassword(String value)
	{
		mPassword = value;
	}

	/**
	 * Get PrivateKey.
	 *
	 * @return PrivateKey
	 */
	public String getPrivateKey()
	{
		return mPrivateKey;
	}

	/**
	 * Set PrivateKey.
	 *
	 * @param value
	 */
	public void setPrivateKey(String value)
	{
		mPrivateKey = value;
	}

	/**
	 * Get Passphrase.
	 *
	 * @return Passphrase
	 */
	public String getPassphrase()
	{
		return mPassphrase;
	}

	/**
	 * Set Passphrase.
	 *
	 * @param value
	 */
	public void setPassphrase(String value)
	{
		mPassphrase = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itko.lisa.test.TestNode#initialize(com.itko.lisa.test.TestCase,
	 * org.w3c.dom.Element)
	 */
	@Override
	public void initialize(TestCase testCase, Element elem) throws TestDefException
	{
		setUsername(XMLUtils.findChildGetItsText(elem, "Username"));
		setHostname(XMLUtils.findChildGetItsText(elem, "Hostname"));
		setPort(XMLUtils.findChildGetItsText(elem, "Port"));
		setTimeout(XMLUtils.findChildGetItsText(elem, "Timeout"));
		setPassword(XMLUtils.findChildGetItsText(elem, "Password"));
		setPrivateKey(XMLUtils.findChildGetItsText(elem, "PrivateKey"));
		setPassphrase(XMLUtils.findChildGetItsText(elem, "Passphrase"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itko.lisa.test.TestNode#writeSubXML(java.io.PrintWriter)
	 */
	@Override
	public void writeSubXML(PrintWriter pw)
	{
		XMLUtils.streamTagAndChild(pw, "Username", getUsername());
		XMLUtils.streamTagAndChild(pw, "Hostname", getHostname());
		XMLUtils.streamTagAndChild(pw, "Port", getPort());
		XMLUtils.streamTagAndChild(pw, "Timeout", getTimeout());
		XMLUtils.streamTagAndChild(pw, "Password", getPassword());
		XMLUtils.streamTagAndChild(pw, "PrivateKey", getPrivateKey());
		XMLUtils.streamTagAndChild(pw, "Passphrase", getPassphrase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itko.lisa.test.TestNode#execute(com.itko.lisa.test.TestExec)
	 */
	@Override
	public void execute(TestExec testExec) throws TestRunException
	{
		try
		{
			testExec.setLastResponse(doNodeLogic(testExec));

			if (LOG.isDebugEnabled()) LOG.debug(getClass().getName() + " transaction completed.");
		}
		catch (TestRunException exc)
		{
			throw exc;
		}
		catch (Exception exc)
		{
			testExec.setLastResponse(exc.getMessage());
			testExec.raiseEvent(TestEvent.EVENT_ABORT, getClass().getName() + " transaction failed.", exc.getMessage() + "\n" + exc.getStackTrace(), exc);
			testExec.setNextNode("abort");
			LOG.error(getClass().getName() + " transaction failed.", exc);
		}
	}
	
	/**
	 * Step implementation.
	 * 
	 * @param testExec
	 * @return
	 * @throws Exception
	 */
	protected abstract Object doNodeLogic(TestExec testExec) throws Exception;
}
