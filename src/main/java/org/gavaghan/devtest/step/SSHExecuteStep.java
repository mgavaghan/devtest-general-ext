package org.gavaghan.devtest.step;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;
import com.itko.util.XMLUtils;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Remotely execute a single command via SSH.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SSHExecuteStep extends SSHStepBase
{
	/** Our logger */
	static private final Logger LOG = LogManager.getLogger(SSHExecuteStep.class);

	/** Command */
	private String mCommand;

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
		super.initialize(testCase, elem);

		setCommand(XMLUtils.findChildGetItsText(elem, "Command"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itko.lisa.test.TestNode#writeSubXML(java.io.PrintWriter)
	 */
	@Override
	public void writeSubXML(PrintWriter pw)
	{
		super.writeSubXML(pw);

		XMLUtils.streamTagAndChild(pw, "Command", getCommand());
	}

	/**
	 * Do the business logic of this step.
	 *
	 * @param testExec
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Object doNodeLogic(TestExec testExec) throws Exception
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
		String username = nullSafe(testExec.parseInState(getUsername()));
		String hostname = nullSafe(testExec.parseInState(getHostname()));
		int port = Integer.parseInt(nullSafe(testExec.parseInState(getPort())));
		int timeout = Integer.parseInt(nullSafe(testExec.parseInState(getTimeout()))) * 1000; // convert
																														// to
																														// millis
		String command = nullSafe(testExec.parseInState(mCommand));
		String password = nullSafe(testExec.parseInState(getPassword()));
		String privateKey = nullSafe(testExec.parseInState(getPrivateKey()));
		String passphrase = nullSafe(testExec.parseInState(getPassphrase()));

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

			@SuppressWarnings("resource")
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
