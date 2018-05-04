package org.gavaghan.devtest.step;

import java.io.PrintWriter;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SFTPController extends SSHControllerBase
{
   /** Context key. */
   static final String STEP_KEY = "lisa.SFTP.key";
 
   @Override
   String getStepKey()
   {
   	return STEP_KEY;
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.TestNodeInfo#initNewOne()
    */
   @Override
   public void initNewOne()
   {
      SFTPStep node = new SFTPStep();
      putAttribute(STEP_KEY, node);
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.TestNodeInfo#writeSubXML(java.io.PrintWriter)
    */
   @Override
   public void writeSubXML(PrintWriter pw)
   {
      SFTPStep node = (SFTPStep) getAttribute(STEP_KEY);
      node.writeSubXML(pw);
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.ControllerBase#migrate(java.lang.Object)
    */
   @Override
   public void migrate(Object obj)
   {
      SFTPStep node = (SFTPStep) obj;
      putAttribute(STEP_KEY, node);
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.ControllerBase#getEditorName()
    */
   @Override
   public String getEditorName()
   {
      return "SFTP";
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.gui.ContextHelpSupport#getHelpString()
    */
   @Override
   public String getHelpString()
   {
      return "SFTP";
   }
}
