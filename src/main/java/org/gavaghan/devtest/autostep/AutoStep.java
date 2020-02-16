package org.gavaghan.devtest.autostep;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestEvent;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.test.TestRunException;
import com.itko.util.CloneImplemented;
import com.itko.util.XMLUtils;

/**
 * Base class for DevTest steps that are defined declaratively.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public abstract class AutoStep extends TestNode implements CloneImplemented
{
   /** Logger. */
   static private final Logger LOG = LoggerFactory.getLogger(AutoStep.class);

   /** Default property values. */
   static private final Map<Class<?>, Object> sDefaultInitialValues = new HashMap<Class<?>, Object>();

   /** Boxed type mapping. */
   static private final Map<Class<?>, Class<?>> sBoxedTypes = new HashMap<Class<?>, Class<?>>();

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
   private final String mTypeName;
   
   /** The context key. */
   private String mStepKey;

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
    * Parse a string into a specified type.
    * 
    * @param text
    * @param klass
    * @return
    */
   @SuppressWarnings("boxing")
   private Object parseString(String text, Class<?> klass)
   {
      // NOTE this could be turned into a map as well
      if (String.class.equals(klass)) return text;
      if (Boolean.class.equals(klass)) return Boolean.parseBoolean(text);
      if (Integer.class.equals(klass)) return Integer.parseInt(text);

      throw new IllegalArgumentException(getString("TypeNotSupported", klass.getName(), mSubClass.getName()));
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
    * Given an XML element, get the text of the named child element.
    * 
    * @param element
    * @param childName
    * @return 'null' if element not found
    */
   private String findChildGetItsText(Element element, String childName)
   {
      String value = null;
      NodeList nodeList = element.getChildNodes();

      // loop through all of the child nodes
      for (int i = 0; i < nodeList.getLength(); i++)
      {
         Node node = nodeList.item(i);

         // is it an element node?
         if (node instanceof Element)
         {
            Element child = (Element) node;

            if (child.getTagName().contentEquals(childName))
            {
               Node textNode = child.getFirstChild();

               // is it an empty element?
               if (textNode == null)
               {
                  value = "";
               }
               // else, get the text
               else
               {
                  value = textNode.getNodeValue();
               }

               break;
            }
         }
      }

      return value;
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
            String propName = prop.value();
            Class<?> propType = prop.type();
            String descr = prop.description();

            // check if value should be boxed
            Class<?> box = sBoxedTypes.get(propType);

            if (box != null)
            {
               LOG.warn(getString("Boxing", propName, box.getSimpleName()));

               propType = box;
            }

            // look for duplicate name in descriptions
            if (mPropDescr.containsKey(propName))
            {
               throw new RuntimeException(getString("DupeProperty", propName, mSubClass.getName()));
            }

            // add description
            if (descr.length() == 0) descr = propName; // default
            if (prop.localized()) descr = AutoStepUtils.getString(mSubClass, descr);
            mPropDescr.put(propName, descr);
            if (LOG.isDebugEnabled()) LOG.debug("Property added.  " + propName + ": " + descr);

            // add default values
            mPropValues.put(propName, getDefault(propType));

            // add type
            mPropTypes.put(propName, propType);
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
      LOG.debug("Constructing AutoStep");
      try
      {
         // get concrete type
         mSubClass = getClass();
         if (LOG.isDebugEnabled()) LOG.debug("Constructing AutoStep of type: " + mSubClass.getName());
         
         // build the step key
         mStepKey = "lisa." + mSubClass.getName() + ".key";
         
         LOG.debug("About to reflect TypeName");
         mTypeName = AutoStepUtils.reflectSimpleGetter(mSubClass, "getTypeName", TypeName.class);
         
         LOG.debug("About to reflect properties");
         reflectProperties();
      }
      catch (RuntimeException exc)
      {
         String text = getString("FailedToInstantiate", getClass().getName());
         LOG.error(text, exc);
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
   @Override
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
    * Get the context key for this instance.
    * 
    * @return the context key
    */
   public String getStepKey()
   {
      return mStepKey;
   }

   /**
    * Returns the value of the @TypeName annotation on the class.
    * 
    * @return the type name.
    */
   @Override
   public String getTypeName()
   {
      return mTypeName;
   }

   /**
    * Get a property value.
    * 
    * @param propName property name
    * @return property value - never a null.
    */
   public Object getProperty(String propName)
   {
      Object value = mPropValues.get(propName);

      if (value == null) throw new RuntimeException(getString("NoSuchProperty", propName, mSubClass.getName()));

      return value;
   }

   /**
    * Get a parsed property
    * 
    * @param testExec test state
    * @param propName property name
    * @return parsed property value - never a null.
    */
   public String getParsedProperty(TestExec testExec, String propName)
   {
      Object value = getProperty(propName);

      return testExec.parseInState(value.toString());
   }

   /**
    * Set a property value.
    * 
    * @param propName property name
    * @param value    new value (may not be null)
    */
   public void setProperty(String propName, Object value)
   {
      if (propName == null) throw new NullPointerException("'name' may not be null.");
      if (value == null) throw new RuntimeException(getString("NoNullProperties", propName, mSubClass.getName()));

      Class<?> targetType = mPropTypes.get(propName);
      if (targetType == null) throw new RuntimeException(getString("NoSuchProperty", propName, mSubClass.getName()));
      
      Object assignValue = value;

      // make sure object matches type
      if (!targetType.isAssignableFrom(assignValue.getClass()))
      {
         // we might be able to coerce a String
         if (String.class.equals(assignValue.getClass()))
         {
            Object newValue;
            
            try
            {
               newValue = parseString((String) assignValue, targetType.getClass());
               assignValue = newValue;
            }
            catch (IllegalArgumentException exc)
            {
               if (LOG.isDebugEnabled())  LOG.debug("Failed to coerce '" + (String) value + ", into a '" + targetType.getSimpleName());
               throw new RuntimeException(getString("WrongType", propName, mSubClass.getName(), targetType.getSimpleName()), exc);
            }
         }
         else
         {
            throw new RuntimeException(getString("WrongType", propName, mSubClass.getName(), targetType.getSimpleName()));
         }
      }

      mPropValues.put(propName, assignValue);
   }

   /**
    * Build step from file system.
    * 
    * @param testCase the test case being built
    * @param element  XML element from test case
    * @throws TestDefException on any initialization failure
    */
   @Override
   public void initialize(TestCase testCase, Element element) throws TestDefException
   {
      LOG.debug("initialize()");

      try
      {
         for (String propName : mPropValues.keySet())
         {
            String text = findChildGetItsText(element, propName);

            // skip unassigned values.  the property was probably removed
            if (text == null) continue;

            Class<?> propType = mPropTypes.get(propName);
            Object value = parseString(text, propType);

            setProperty(propName, value);
         }
      }
      catch (Exception exc)
      {
         LOG.error(getString("MethodFailed", "initialize()"), exc);
         throw exc;
      }
   }

   /**
    * Save step to the file system.
    * 
    * @param pw target writer.
    */
   @Override
   public void writeSubXML(PrintWriter pw)
   {
      try
      {
         for (String propName : mPropValues.keySet())
         {
            XMLUtils.streamTagAndChild(pw, propName, getProperty(propName).toString());
         }
      }
      catch (Exception exc)
      {
         LOG.error(getString("MethodFailed", "writeSubXML()"), exc);
         throw exc;
      }
   }
}
