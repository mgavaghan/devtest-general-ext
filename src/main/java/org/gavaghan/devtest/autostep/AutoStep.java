package org.gavaghan.devtest.autostep;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestEvent;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;
import com.itko.util.XMLUtils;

/**
 * Base class for DevTest steps that are defined declaratively.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public abstract class AutoStep //extends TestNode implements CloneImplemented
{
   /** Our logger */
   static private final Logger LOG = LogManager.getLogger(AutoStep.class);

   /** Default property values. */
   static private final Map<Class<?>, Object> sDefaultInitialValues = new HashMap<Class<?>, Object>();

   /** Boxed type mapping. */
   static private final Map<Class<?>, Class<?>> sBoxedTypes = new HashMap<Class<?>, Class<?>>();

   /** Convenient constant for reflection. */
   static private final Class<?>[] NO_PARAMS = new Class<?>[0];

   static
   {
      // setup default property values
      sDefaultInitialValues.put(String.class, "");
      sDefaultInitialValues.put(Integer.class, new Integer(0));
      sDefaultInitialValues.put(Boolean.class, Boolean.FALSE);
      
      // setup boxed types
      sBoxedTypes.put(int.class, Integer.class);
      sBoxedTypes.put(boolean.class, Boolean.class);
   }

   /** Our concrete type. */
   private final Class<?> mSubClass;

   /** The value to be returned by getTypeName() */
   private String mTypeName;

   /** Map of property names to their value. */
   private final Map<String, Object> mPropValues = new HashMap<String, Object>();

   /** Map of property names to their type. */
   private final Map<String, Class<?>> mPropTypes = new HashMap<String, Class<?>>();

   /** Map of property names to their description. */
   private final Map<String, String> mPropDescr = new HashMap<String, String>();

   /** Last response if subtype overrides it. */
   private Object mLastResponse = null;

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
    * Instantiate the default instance of a type.
    * 
    * @param klass a supported class
    * @return new instance of type klass
    */
   private Object getDefault(Class<?> klass)
   {
      Object value = sDefaultInitialValues.get(klass);
      
      if (value == null)
      {
         throw new RuntimeException(getString("TypeNotSupported", klass.getName(), mSubClass.getName()));
      }

      return value;
   }

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

      // if there's no @TypeName, make sure getTypeName() is overridden
      if ((typeName == null) && method.getDeclaringClass().equals(AutoStep.class))
      {
         throw new RuntimeException(getString("NoTypeName", mSubClass.getName()));
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
    * Find all of the properties from the @Property annotations
    */
   private void reflectProperties()
   {
      Properties props = mSubClass.getAnnotation(Properties.class);

      if (props != null)
      {
         for (Property prop : props.value())
         {
            String name = prop.value();
            Class<?> type = prop.type();
            String descr = prop.description();
            
            // check if value should be boxed
            Class<?> box = sBoxedTypes.get(type);
            
            if (box != null)
            {
               LOG.warn(getString("Boxing", name, box.getSimpleName()));
               
               type = box;
            }

            // look for duplicate name in descriptions
            if (mPropDescr.containsKey(name))
            {
               throw new RuntimeException(getString("DupeProperty", name, mSubClass.getName()));
            }

            // add description
            if (descr.length() == 0) descr = name; // default
            if (prop.localized()) descr = AutoStepUtils.getString(mSubClass, descr);
            mPropDescr.put(name, descr);
            if (LOG.isDebugEnabled()) LOG.debug("Property added.  " + name + ": " + descr);

            // add default values
            mPropValues.put(name, getDefault(type));

            // add type
            mPropTypes.put(name, type);
         }
      }
      else
      {
         LOG.warn(getString("LogNoProperties", mSubClass.getName()));
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
         reflectProperties();
      }
      catch (RuntimeException | NoSuchMethodException exc)
      {
         String text = getString("FailedToInstantiate", getClass().getName());
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

   /**
    * Get a property value.
    * 
    * @param name property name
    * @return property value - never a null.
    */
   public Object getProperty(String name)
   {
      Object value = mPropValues.get(name);

      if (value == null) throw new RuntimeException(getString("NoSuchProperty", name, mSubClass.getName()));

      return value;
   }

   /**
    * Set a property value.
    * 
    * @param name  property name
    * @param value new value (may not be null)
    */
   public void setProperty(String name, Object value)
   {
      if (name == null) throw new NullPointerException("'name' may not be null.");
      if (value == null) throw new RuntimeException(getString("NoNullProperties", name, mSubClass.getName()));

      Class<?> type = mPropTypes.get(name);
      if (type == null) throw new RuntimeException(getString("NoSuchProperty", name, mSubClass.getName()));

      if (!type.isAssignableFrom(value.getClass()))
      {
         throw new RuntimeException(getString("WrongType", name, mSubClass.getName(), type.getSimpleName()));
      }

      mPropValues.put(name, value);
   }

   /**
    * Build step from file system.
    * 
    * @param testCase the test case being built
    * @param element  XML element from test case
    * @throws TestDefException on any initialization failure
    */
   //@Override
   public void initialize(TestCase testCase, Element element) throws TestDefException
   {
      LOG.debug("initialize()");

      try
      {
         // FIXME super.initialize(testCase, element);
         //setFilenameProperty(XMLUtils.findChildGetItsText(element, "filename"));
         //setTargetFolder(XMLUtils.findChildGetItsText(element, "targetFolder"));
      }
      catch (Exception exc)
      {
         LOG.error("Failed in initialize()", exc);
         throw exc;
      }
   }

   /**
    * Save step to the file system.
    * 
    * @param pw target writer.
    */
   //@Override
   public void writeSubXML(PrintWriter pw)
   {
      try
      {
         // FIXME super.writeSubXML(pw);
         for (String name : mPropValues.keySet())
         {
            XMLUtils.streamTagAndChild(pw, name, getProperty(name).toString());
         }
      }
      catch (Exception exc)
      {
         LOG.error("Failed in writeSubXML()", exc);
         throw exc;
      }
   }
}
