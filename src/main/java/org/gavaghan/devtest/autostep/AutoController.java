package org.gavaghan.devtest.autostep;

import java.io.PrintWriter;

import javax.swing.Icon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itko.lisa.editor.TestNodeInfo;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public abstract class AutoController<T extends AutoStep> extends TestNodeInfo
{
   /** Logger. */
   static private final Logger LOG = LoggerFactory.getLogger(AutoController.class);

   /** Context key. */
   static final String STEP_KEY = "lisa." + "" + ".key";

   private final TypeParameterFactory<T> mStepFactory;
   
   /** Our concrete type. */
   private final Class<?> mSubClass;
   
   /** Our step key */
   private final String mStepKey;

   /** The value to be returned by getEditorName() */
   private String mEditorName;

   /** The value to be returned by getHelpString() */
   private String mHelpString;

   /**
    * Convenience method for getting AutoStep resources.
    * 
    * @param key
    * @param args
    * @return
    */
   private String getString(String key, Object... args)
   {
      return AutoStepUtils.getString(AutoStep.class, key, args);
   }

   /**
    * Start reflecting on the concrete type
    */
   protected AutoController(Class<T> stepType)
   {
      LOG.debug("Constructing AutoController");
      
      try
      {
         // build a type factory so we can get a prototype instance of the step
         mStepFactory = new TypeParameterFactory<T>(stepType);
         AutoStep prototypeStep = mStepFactory.get();
         
         mStepKey = prototypeStep.getStepKey();
         
         // get concrete type
         mSubClass = getClass();
         if (LOG.isDebugEnabled()) LOG.debug("Constructing AutoStep of type: " + mSubClass.getName());
         
         LOG.debug("About to reflect simpkle getters");
         mEditorName = AutoStepUtils.reflectSimpleGetter(mSubClass, "getEditorName", TypeName.class);
         mHelpString = AutoStepUtils.reflectSimpleGetter(mSubClass, "getEditorName", TypeName.class);
      }
      catch (RuntimeException exc)
      {
         String text = getString("FailedToInstantiate", getClass().getName());
         LOG.error(text, exc);
         throw new RuntimeException(text, exc);
      }
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.TestNodeInfo#initNewOne()
    */
   @Override
   public void initNewOne()
   {
      T node = mStepFactory.get();
      putAttribute(mStepKey, node);
   }

   // FIXME make a default sometime
   @Override
   public abstract Icon getLargeIcon();

   // FIXME make a default sometime
   @Override
   public abstract Icon getSmallIcon();

   @Override
   public String getEditorName()
   {
      return mEditorName;
   }

   @Override
   public String getHelpString()
   {
      return mHelpString;
   }

   @Override
   public void writeSubXML(PrintWriter pw)
   {
      @SuppressWarnings("unchecked")
      T node = (T) getAttribute(mStepKey);
      node.writeSubXML(pw);
   }

   @Override
   public void migrate(Object obj)
   {
      @SuppressWarnings("unchecked")
      T node = (T) obj;
      putAttribute(mStepKey, node);
   }
}
