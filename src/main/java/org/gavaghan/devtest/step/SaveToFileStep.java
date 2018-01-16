package org.gavaghan.devtest.step;

import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestEvent;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.util.CloneImplemented;
import com.itko.util.XMLUtils;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SaveToFileStep extends TestNode implements CloneImplemented
{
   /** Our logger */
   static private final Logger LOG = LogManager.getLogger(SaveToFileStep.class);

   /** Filename */
   private String mFilename;

   /** Binary */
   private boolean mBinary;

   /** Encoding */
   private String mEncoding;

   /** Content */
   private String mContent;

   /**
    * Get Filename.
    *
    * @return Filename
    */
   public String getFilename()
   {
      return mFilename;
   }

   /**
    * Set Filename.
    *
    * @param value
    */
   public void setFilename(String value)
   {
      mFilename = value;
   }

   /**
    * Get Binary.
    *
    * @return Binary
    */
   public boolean getBinary()
   {
      return mBinary;
   }

   /**
    * Set Binary.
    *
    * @param value
    */
   public void setBinary(boolean value)
   {
      mBinary = value;
   }

   /**
    * Get Encoding.
    *
    * @return Encoding
    */
   public String getEncoding()
   {
      return mEncoding;
   }

   /**
    * Set Encoding.
    *
    * @param value
    */
   public void setEncoding(String value)
   {
      mEncoding = value;
   }

   /**
    * Get Content.
    *
    * @return Content
    */
   public String getContent()
   {
      return mContent;
   }

   /**
    * Set Content.
    *
    * @param value
    */
   public void setContent(String value)
   {
      mContent = value;
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.test.NamedType#getTypeName()
    */
   @Override
   public String getTypeName() throws Exception
   {
      // TODO Provide a step type name
      return "SaveToFile";
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.test.TestNode#initialize(com.itko.lisa.test.TestCase, org.w3c.dom.Element)
    */
   @Override
   public void initialize(TestCase testCase, Element elem) throws TestDefException
   {
      setFilename(XMLUtils.findChildGetItsText(elem, "Filename"));
      //setBinary(XMLUtils.findChildGetItsText(elem, "Binary"));
      setEncoding(XMLUtils.findChildGetItsText(elem, "Encoding"));
      setContent(XMLUtils.findChildGetItsText(elem, "Content"));
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.test.TestNode#writeSubXML(java.io.PrintWriter)
    */
   @Override
   public void writeSubXML(PrintWriter pw)
   {
      XMLUtils.streamTagAndChild(pw, "Filename", getFilename());
      //XMLUtils.streamTagAndChild(pw, "Binary", getBinary());
      XMLUtils.streamTagAndChild(pw, "Encoding", getEncoding());
      XMLUtils.streamTagAndChild(pw, "Content", getContent());
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.test.TestNode#execute(com.itko.lisa.test.TestExec)
    */
   @Override
   public void execute(TestExec testExec) throws TestRunException
   {
      try
      {
         testExec.setLastResponse(doNodeLogic(testExec));

         if (LOG.isDebugEnabled())  LOG.debug(getClass().getName() + " transaction completed.");
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
    * Do the business logic of this step.
    *
    * @param testExec
    * @return
    * @throws Exception
    */
   private Object doNodeLogic(TestExec testExec) throws Exception
   {
      if (LOG.isDebugEnabled())  LOG.debug(getClass().getName() + " transaction beginning.");
      
      // TODO Your rockin' business logic goes here
      return null;
   }
}
