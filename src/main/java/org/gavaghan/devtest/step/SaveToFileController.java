package org.gavaghan.devtest.step;

import javax.swing.Icon;

import org.gavaghan.devtest.autostep.AutoController;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SaveToFileController extends AutoController<SaveToFileStep>
{
   /** Context key. */
   static final String STEP_KEY = "lisa.SaveToFile.key";
   
   /**
    * Constructor passes the type paramter class;
    */
   public SaveToFileController()
   {
      super(SaveToFileStep.class);
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.TestNodeInfo#getLargeIcon()
    */
   @Override
   public Icon getLargeIcon()
   {
      return SaveToFileIcons.getLargeIcon();
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.TestNodeInfo#getSmallIcon()
    */
   @Override
   public Icon getSmallIcon()
   {
      return SaveToFileIcons.getSmallIcon();
   }
}
