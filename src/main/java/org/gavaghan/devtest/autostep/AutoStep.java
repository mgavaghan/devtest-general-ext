package org.gavaghan.devtest.autostep;

import java.lang.reflect.Method;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestEvent;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;

/**
 * Base class for DevTest steps that are defined declaratively.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public abstract class AutoStep //extends TestNode implements CloneImplemented
{
   /** Our logger */
   static private final Logger LOG = LogManager.getLogger(AutoStep.class);
   
   /** Shorthand for the AutoStep class object. */
   static private final Class<?> THIS;

   /** Convenient constant for reflection. */
   static private final Class<?>[] NO_PARAMS = new Class<?>[0];
   
   static
   {
      THIS = AutoStep.class;
   }

   /** Our concrete type. */
   private final Class<?> mSubClass;

   /** The value to be returned by getTypeName() */
   private String mTypeName;

   /** Last response if subtype overrides it. */
   private Object mLastResponse = null;

   /**
    * Resolve the type name by looking for @TypeName or an override of
    * getTypeName().
    * 
    * @throws NoSuchMethodException
    */
   private void reflectTypeName() throws NoSuchMethodException
   {
      Method method = mSubClass.getMethod("getTypeName", NO_PARAMS);
      TypeName typeName = mSubClass.getAnnotation(TypeName.class);

      // if there's not @TypeName, make sure getTypeName() is overridden
      if ((typeName == null) && method.getDeclaringClass().equals(AutoStep.class))
      {
         throw new RuntimeException(AutoStepUtils.getString(THIS, "NoTypeName", mSubClass.getSimpleName()));
      }

      // if @TypeName wants to be localized
      if ((typeName != null) && typeName.localized())
      {
         mTypeName = AutoStepUtils.getString(mSubClass, typeName.value());
      }
      // else, take the literal value
      else
      {
         mTypeName = (typeName != null) ? typeName.value() : null;
      }
   }

   /**
    * Start reflecting on the concrete type 
    */
   protected AutoStep()
   {
      try
      {
         // get concrete type
         mSubClass = getClass();
         if (LOG.isDebugEnabled()) LOG.debug("Constructing AutoStep of type: " + mSubClass.getName());

         reflectTypeName();

      }
      catch (RuntimeException | NoSuchMethodException exc)
      {
         String text = AutoStepUtils.getString(THIS, "FailedToInstantiate", getClass().getName());
         LOG.fatal(text, exc);
         throw new RuntimeException(text, exc);
      }
   }

   /**
    * Override the default last response. If this was previously set in
    * doNodeLogic() but an exception is thrown, you must call this again during
    * onException();
    * 
    * @param resp new last response
    */
   protected final void setLastResponse(Object resp)
   {
      mLastResponse = resp;
   }

   /**
    * <p>
    * Give the subtype an opportunity to deal with exceptions. To override the
    * default last response value, call setLastRespone().
    * <p>
    * 
    * <p>
    * By default, this method does nothing.
    * </p>
    * 
    * @param exc the unhandled exception.
    */
   protected void onException(Exception exc)
   {
   }

   /**
    * Do the logic of this step. To override the default last response value, call
    * setLastRespone().
    * 
    * @param testExec test state
    * @return default last response for the node
    * @throws Exception on any unhandled exception
    */
   protected abstract Object doNodeLogic(TestExec testExec) throws Exception;

   /**
    * Wraps call to doNodeLogic() to handle last response, events, and exceptions
    * 
    * @param testExec test state
    * @throws TestRunException on any unhandled test failure
    */
   //@Override
   protected final void execute(TestExec testExec) throws TestRunException
   {
      try
      {
         // execute the business logic
         Object lastResponse = doNodeLogic(testExec);

         // see if the subtype overrode the last response
         if (mLastResponse != null) lastResponse = mLastResponse;

         testExec.setLastResponse(lastResponse);
      }
      catch (Exception exc)
      {
         // clear whatever had been set before.
         mLastResponse = null;

         // allow subtypes to deal with exceptions
         onException(exc);

         // last response defaults to exception message
         Object lastResponse = exc.getMessage();

         // see if the subtype overrode the last response during onException();
         if (mLastResponse != null) lastResponse = mLastResponse;
         testExec.setLastResponse(lastResponse);

         // raise events and log
         testExec.raiseEvent(TestEvent.EVENT_ABORT, getClass().getName() + " transaction failed.", exc.getMessage() + "\n" + exc.getStackTrace(), exc);
         testExec.setNextNode("abort");
         LOG.error(getClass().getName() + " transaction failed.", exc);
      }
      finally
      {
         mLastResponse = null;
      }
   }

   /**
    * Returns the value of the @TypeName annotation on the class.
    * 
    * @return the type name.
    */
   //@Override
   public String getTypeName()
   {
      return mTypeName;
   }

   //@Override
   public void initialize(TestCase testCase, Element arg1) throws TestDefException
   {
      // TODO Auto-generated method stub

   }
}
