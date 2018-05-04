package org.gavaghan.devtest.step;

import javax.swing.Icon;

import com.itko.lisa.editor.TestNodeInfo;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public abstract class SSHControllerBase extends TestNodeInfo
{
   abstract String getStepKey();

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
}
