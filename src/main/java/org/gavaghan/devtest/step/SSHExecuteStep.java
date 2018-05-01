package org.gavaghan.devtest.step;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.DHG14;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Remotely execute a single command via SSH.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SSHExecuteStep extends TestNode implements CloneImplemented
{
	/** Our logger */
	static private final Logger LOG = LogManager.getLogger(SSHExecuteStep.class);

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

	/** Command */
	private String mCommand;

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
		SSHExecuteStep.class.notifyAll();
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
			SSHExecuteStep.class.wait();
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
	 * Get Command.
	 *
	 * @return Command
	 */
	public String getCommand()
	{
		return mCommand;
	}

	/**
	 * Set Command.
	 *
	 * @param value
	 */
	public void setCommand(String value)
	{
		mCommand = value;
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
	 * @see com.itko.lisa.test.NamedType#getTypeName()
	 */
	@Override
	public String getTypeName() throws Exception
	{
		return "SSH Execute";
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
		setCommand(XMLUtils.findChildGetItsText(elem, "Command"));
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
		XMLUtils.streamTagAndChild(pw, "Command", getCommand());
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
	 * Turn nulls into empty strings.
	 * 
	 * @param value
	 * @return
	 */
	private String nullSafe(String value)
	{
		if (value == null) return "";
		return value;
	}

	/**
	 * Load a file.
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	private byte[] loadFile(String filename) throws IOException
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
	 * Get configuration to build the session.
	 * 
	 * @param password
	 * @param privateKey
	 * @return
	 * @throws TestDefException
	 */
	private Properties getConfiguration(String password, String privateKey) throws TestDefException
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
	 * Do the business logic of this step.
	 *
	 * @param testExec
	 * @return
	 * @throws Exception
	 */
	private Object doNodeLogic(TestExec testExec) throws Exception
	{
		if (LOG.isDebugEnabled()) LOG.debug(getClass().getName() + " transaction beginning.");

		Session session = null;
		ChannelExec channel = null;
		String response;
		String stderr;

		// remove old state
		testExec.removeState("ssh.execute.exit.status");
		testExec.removeState("ssh.execute.stderr");

		// expand parameters
		String username = nullSafe(testExec.parseInState(mUsername));
		String hostname = nullSafe(testExec.parseInState(mHostname));
		int port = Integer.parseInt(nullSafe(testExec.parseInState(mPort)));
		int timeout = Integer.parseInt(nullSafe(testExec.parseInState(mTimeout))) * 1000; // convert
																														// to
																														// millis
		String command = nullSafe(testExec.parseInState(mCommand));
		String password = nullSafe(testExec.parseInState(mPassword));
		String privateKey = nullSafe(testExec.parseInState(mPrivateKey));
		String passphrase = nullSafe(testExec.parseInState(mPassphrase));

		// ensure the preload has completed
		waitForPreload();

		// create the JSch
		JSch jsch = new JSch();

		// add identities
		if (privateKey.length() > 0)
		{
			byte[] pkBytes = loadFile(privateKey);
			jsch.addIdentity(username, pkBytes, new byte[0], passphrase.getBytes());
		}

		// create the configuration
		Properties config = getConfiguration(password, privateKey);

		try
		{
			// create the session object
			session = jsch.getSession(username, hostname, port);
			session.setConfig(config);
			session.setTimeout(timeout);
			if (password.length() > 0) session.setPassword(password);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("cipher.c2s = " + session.getConfig("cipher.c2s"));
				LOG.debug("cipher.s2c = " + session.getConfig("cipher.s2c"));
				LOG.debug("CheckCiphers = " + session.getConfig("CheckCiphers"));
				LOG.debug("CheckKexes = " + session.getConfig("CheckKexes"));
			}

			// open the session
			LOG.info("About to open session");
			session.connect();

			// open the channel
			LOG.debug("About to open channel");
			channel = (ChannelExec) session.openChannel("exec");

			// set the command
			if (LOG.isInfoEnabled()) LOG.info("About to set command to: " + mCommand);
			channel.setCommand(command);

			ByteArrayOutputStream stderrStr = new ByteArrayOutputStream();

			channel.setInputStream(null);
			channel.setErrStream(stderrStr);

			InputStream in = channel.getInputStream();

			// connect
			LOG.debug("About to connect");
			channel.connect();

			// read the response
			byte[] buffer = new byte[4096];
			StringBuilder builder = new StringBuilder();

			while (true)
			{
				// keep reading while we have data
				while (in.available() > 0)
				{
					int i = in.read(buffer, 0, 1024);
					if (i < 0) break;
					builder.append(new String(buffer, 0, i));
				}

				// check for complete
				if (channel.isClosed())
				{
					if (in.available() > 0) continue;
					testExec.setStateValue("ssh.execute.exit.status", new Integer(channel.getExitStatus()));
					break;
				}

				try
				{
					Thread.sleep(1000);
				}
				catch (Exception ee)
				{
				}
			}

			response = builder.toString();
			stderr = new String(stderrStr.toByteArray());
		}
		catch (JSchException exc)
		{
			throw new TestRunException("SSH failure during execution", exc);
		}
		finally
		{
			if (channel != null) channel.disconnect();
			if (session != null) session.disconnect();
		}

		testExec.setStateValue("ssh.execute.stderr", stderr);

		return response;
	}
}
