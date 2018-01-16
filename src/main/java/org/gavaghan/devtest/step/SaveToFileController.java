package org.gavaghan.devtest.step;

import java.io.PrintWriter;

import javax.swing.Icon;

import com.itko.lisa.core.ModuleLegacy;
import com.itko.lisa.editor.TestNodeInfo;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SaveToFileController extends TestNodeInfo
{
   /** Context key. */
   static final String STEP_KEY = "lisa.SaveToFile.key";

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.TestNodeInfo#initNewOne()
    */
   @Override
   public void initNewOne()
   {
      SaveToFileStep node = new SaveToFileStep();
      putAttribute(STEP_KEY, node);
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.TestNodeInfo#getLargeIcon()
    */
   @Override
   public Icon getLargeIcon()
   {
      return ModuleLegacy.resources.getIcon("icon.tctree.ftpnode.lg", new Object[0]);
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.TestNodeInfo#getSmallIcon()
    */
   @Override
   public Icon getSmallIcon()
   {
      return ModuleLegacy.resources.getIcon("icon.tctree.ftpnode", new Object[0]);
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.TestNodeInfo#writeSubXML(java.io.PrintWriter)
    */
   @Override
   public void writeSubXML(PrintWriter pw)
   {
      SaveToFileStep node = (SaveToFileStep) getAttribute(STEP_KEY);
      node.writeSubXML(pw);
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.ControllerBase#migrate(java.lang.Object)
    */
   @Override
   public void migrate(Object obj)
   {
      SaveToFileStep node = (SaveToFileStep) obj;
      putAttribute(STEP_KEY, node);
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.ControllerBase#getEditorName()
    */
   @Override
   public String getEditorName()
   {
      return "Save To File";
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.gui.ContextHelpSupport#getHelpString()
    */
   @Override
   public String getHelpString()
   {
      return "Save To File";
   }
}
