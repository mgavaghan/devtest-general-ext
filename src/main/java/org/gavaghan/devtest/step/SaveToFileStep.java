package org.gavaghan.devtest.step;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.gavaghan.devtest.autostep.AutoStep;
import org.gavaghan.devtest.autostep.Property;
import org.gavaghan.devtest.autostep.TypeName;

import com.itko.lisa.test.TestExec;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@TypeName("Save To File")
@Property(value = "filename", description = "Filename")
@Property(value = "encoding", description = "Encoding")  // FIXME this should default to UTF-8
@Property(value = "content", description = "Content")
public class SaveToFileStep extends AutoStep
{
   /** Our logger */
   static private final Logger LOG = LogManager.getLogger(SaveToFileStep.class);

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

      String filename = getParsedProperty(testExec, "filename");
      String encoding = getParsedProperty(testExec, "encoding");
      String content = getParsedProperty(testExec, "content");

      try (FileOutputStream fos = new FileOutputStream(filename); OutputStreamWriter osw = new OutputStreamWriter(fos, encoding))
      {
         osw.write(content);
         osw.flush();
         fos.flush();
      }

      return content;
   }
}
