package org.gavaghan.devtest.step;

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
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SFTPStep extends SSHStepBase
{
   /** Our logger */
   static private final Logger LOG = LogManager.getLogger(SFTPStep.class);

   /** SendRecv */
   private String mSendRecv;

   /** LocalFile */
   private String mLocalFile;

   /** RemoteFile */
   private String mRemoteFile;


   /**
    * Get SendRecv.
    *
    * @return SendRecv
    */
   public String getSendRecv()
   {
      return mSendRecv;
   }

   /**
    * Set SendRecv.
    *
    * @param value
    */
   public void setSendRecv(String value)
   {
      mSendRecv = value;
   }

   /**
    * Get LocalFile.
    *
    * @return LocalFile
    */
   public String getLocalFile()
   {
      return mLocalFile;
   }

   /**
    * Set LocalFile.
    *
    * @param value
    */
   public void setLocalFile(String value)
   {
      mLocalFile = value;
   }

   /**
    * Get RemoteFile.
    *
    * @return RemoteFile
    */
   public String getRemoteFile()
   {
      return mRemoteFile;
   }

   /**
    * Set RemoteFile.
    *
    * @param value
    */
   public void setRemoteFile(String value)
   {
      mRemoteFile = value;
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.test.NamedType#getTypeName()
    */
   @Override
   public String getTypeName() throws Exception
   {
      return "SFTP";
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.test.TestNode#initialize(com.itko.lisa.test.TestCase, org.w3c.dom.Element)
    */
   @Override
   public void initialize(TestCase testCase, Element elem) throws TestDefException
   {
   	super.initialize(testCase, elem);
   	
      setSendRecv(XMLUtils.findChildGetItsText(elem, "SendRecv"));
      setLocalFile(XMLUtils.findChildGetItsText(elem, "LocalFile"));
      setRemoteFile(XMLUtils.findChildGetItsText(elem, "RemoteFile"));
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.test.TestNode#writeSubXML(java.io.PrintWriter)
    */
   @Override
   public void writeSubXML(PrintWriter pw)
   {
   	super.writeSubXML(pw);
   	
      XMLUtils.streamTagAndChild(pw, "SendRecv", getSendRecv());
      XMLUtils.streamTagAndChild(pw, "LocalFile", getLocalFile());
      XMLUtils.streamTagAndChild(pw, "RemoteFile", getRemoteFile());
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
		ChannelSftp channel = null;

		// expand parameters
		String username = nullSafe(testExec.parseInState(getUsername()));
		String hostname = nullSafe(testExec.parseInState(getHostname()));
		int port = Integer.parseInt(nullSafe(testExec.parseInState(getPort())));
		int timeout = Integer.parseInt(nullSafe(testExec.parseInState(getTimeout()))) * 1000; // convert
																														// to
																														// millis
		String password = nullSafe(testExec.parseInState(getPassword()));
		String privateKey = nullSafe(testExec.parseInState(getPrivateKey()));
		String passphrase = nullSafe(testExec.parseInState(getPassphrase()));

		String sendRecv = nullSafe(testExec.parseInState(mSendRecv));
		String remoteFile = nullSafe(testExec.parseInState(mRemoteFile));
		String localFile = nullSafe(testExec.parseInState(mLocalFile));
		
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
			channel = (ChannelSftp) session.openChannel("sftp");
			
			// connect
			LOG.debug("About to connect");
			channel.connect();

			// do a put
			if ("send".equals(sendRecv.toLowerCase()))
			{
				channel.put(localFile, remoteFile);
			}
			// do a get
			else
			{
				channel.get(remoteFile, localFile);
			}
		}
		catch (SftpException exc)
		{
			throw new TestRunException("SFTP failure during execution", exc);
		}
		finally
		{
			if (channel != null) channel.disconnect();
			if (session != null) session.disconnect();
		}

		return null;
   }
}
