package org.gavaghan.devtest.step;

import java.io.PrintWriter;

import javax.swing.Icon;

import com.itko.lisa.editor.TestNodeInfo;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SSHExecuteController extends TestNodeInfo
{
   /** Context key. */
   static final String STEP_KEY = "lisa.SSHExecute.key";

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.TestNodeInfo#initNewOne()
    */
   @Override
   public void initNewOne()
   {
      SSHExecuteStep node = new SSHExecuteStep();
      putAttribute(STEP_KEY, node);
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.TestNodeInfo#getLargeIcon()
    */
   @Override
   public Icon getLargeIcon()
   {
      return SSHIcons.getLargeIcon();
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.TestNodeInfo#getSmallIcon()
    */
   @Override
   public Icon getSmallIcon()
   {
      return SSHIcons.getSmallIcon();
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.TestNodeInfo#writeSubXML(java.io.PrintWriter)
    */
   @Override
   public void writeSubXML(PrintWriter pw)
   {
      SSHExecuteStep node = (SSHExecuteStep) getAttribute(STEP_KEY);
      node.writeSubXML(pw);
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.ControllerBase#migrate(java.lang.Object)
    */
   @Override
   public void migrate(Object obj)
   {
      SSHExecuteStep node = (SSHExecuteStep) obj;
      putAttribute(STEP_KEY, node);
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.ControllerBase#getEditorName()
    */
   @Override
   public String getEditorName()
   {
      return "SSH Execute";
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.gui.ContextHelpSupport#getHelpString()
    */
   @Override
   public String getHelpString()
   {
      return "SSH Execute";
   }
}
