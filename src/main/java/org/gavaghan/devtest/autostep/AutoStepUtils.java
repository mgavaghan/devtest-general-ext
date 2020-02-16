package org.gavaghan.devtest.autostep;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <P>
 * Utilities enabling finding resource bundles associated with class and the
 * data within those bundles.
 * </p>
 * <P>
 * The naming convention for base resources is [Fully qualified class
 * name].properties
 * </p>
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class AutoStepUtils
{
   /** Logger. */
   static private final Logger LOG = LoggerFactory.getLogger(AutoStepUtils.class);

   /** Map class types to their resource bundles. */
   static private final Map<Class<?>, ResourceBundle> sBundles = new HashMap<Class<?>, ResourceBundle>();

   /** Convenient constant for reflection. */
   static private final Class<?>[] NO_PARAMS = new Class<?>[0];

   /** Convenient constant for reflection. */
   static private final Object[] NO_ARGS = new Object[0];

   static
   {
      LOG.debug("Static initialization on AutoStepUtils");

      // make sure we at least find the AutoStep bundle because it's used to localize errors
      sBundles.put(AutoStep.class, ResourceBundle.getBundle(AutoStep.class.getName()));
   }

   /**
    * Disallow instantiation.
    */
   private AutoStepUtils()
   {
   }

   /**
    * Get the ResourceBundle of a class.
    * 
    * @param klass target class
    * 
    * @return the resource bundle
    * @throws RuntimeException if the resource file could not be found
    */
   static public ResourceBundle getResourceBundle(Class<?> klass)
   {
      ResourceBundle bundle;

      LOG.debug("getResourceBundle() - ENTER");

      synchronized (sBundles)
      {
         // look for it in the map
         bundle = sBundles.get(klass);

         // if not in the map, load it from the callers class loader
         if (bundle == null)
         {
            try
            {
               bundle = ResourceBundle.getBundle(klass.getName());
            }
            catch (MissingResourceException exc)
            {
               // the resource file could not be found
               String text = getString(AutoStep.class, "NoResourceBundle", klass.getName());
               LOG.error(text);
               throw new RuntimeException(text);
            }

            sBundles.put(klass, bundle);
         }
      }

      LOG.debug("getResourceBundle() - EXIT");

      return bundle;
   }

   /**
    * Get a locale specific string from a class's resource bundle.
    * 
    * @param klass target class
    * @param key   resource bundle key
    * @param args  message arguments
    * @return the formatted string
    */
   static public String getString(Class<?> klass, String key, Object... args)
   {
      LOG.debug("getString() - ENTER");

      try
      {
         LOG.debug("getString() - EXIT");
         return MessageFormat.format(getResourceBundle(klass).getString(key), args);
      }
      catch (MissingResourceException exc)
      {
         // the key in the resource file could not be found
         String text = getString(AutoStep.class, "NoResourceKey", key, klass.getName());
         LOG.error(text);
         throw new RuntimeException(getString(AutoStep.class, "NoResourceKey", key, klass.getName()));
      }

   }

   static public String reflectSimpleGetter(Class<?> klass, String methodName, Class annotation)
   {
      String retval;

      LOG.debug("reflectSimpleGetter() - ENTER");

      try
      {
         // do some reflections on the input
         String annotName = annotation.getSimpleName();
         Method method = klass.getMethod(methodName, NO_PARAMS);
         @SuppressWarnings("unchecked")
         Object annotObj = klass.getAnnotation(annotation);

         if (LOG.isDebugEnabled())
         {
            LOG.debug("Annotation type: " + annotName);
            LOG.debug("annot = " + ((annotObj == null) ? "null" : annotObj.getClass().getSimpleName()));
         }

         // if there's no annotation on the class..
         if (annotObj == null)
         {
            if (LOG.isDebugEnabled()) LOG.debug("@" + annotName + " not found on " + klass.getSimpleName());
            // at least ensure the method was overwritten
            if (method.getDeclaringClass().equals(AutoStep.class))
            {
               throw new RuntimeException(getString(AutoStep.class, "NoGetterValue", klass.getName(), annotName, methodName));
            }
            retval = null;
         }

         // else, we found an annotation
         else
         {
            if (LOG.isDebugEnabled()) LOG.debug("@" + annotName + " was found on " + klass.getSimpleName());

            // lookup 'localized'
            @SuppressWarnings("unchecked")
            Method localizedMethod = annotation.getMethod("localized", NO_PARAMS);
            boolean localized = ((Boolean) localizedMethod.invoke(annotObj, NO_ARGS)).booleanValue();

            // lookup 'value'
            @SuppressWarnings("unchecked")
            Method valueMethod = annotation.getMethod("value", NO_PARAMS);
            String value = valueMethod.invoke(annotObj, NO_ARGS).toString();

            // if getter wants to be localized
            if (localized)
            {
               retval = AutoStepUtils.getString(klass, value);
            }
            // else, take the literal value
            else
            {
               retval = value;
            }
         }
      }
      catch (InvocationTargetException exc)
      {
         Throwable t = exc.getCause();
         // FIXME localize this
         throw new RuntimeException("Failure trying to reflect on getter: " + methodName, t);
      }
      catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException exc)
      {
         // FIXME localize this
         throw new RuntimeException("Failure trying to reflect on getter: " + methodName, exc);
      }

      if (LOG.isDebugEnabled())
      {
         LOG.debug("reflected = " + retval);
         LOG.debug("reflectSimpleGetter() - EXIT");
      }

      return retval;
   }

   /**
    * Parse a string into a specified type.
    * 
    * @param text
    * @param klass
    * @throws IllegalArgumentException if value could not be parsed
    * @return
    */
   @SuppressWarnings("boxing")
   static public Object parseString(String text, Class<?> klass) throws IllegalArgumentException
   {
      Object retval = null;

      // NOTE this could be turned into a map
      if (String.class.equals(klass))
      {
         retval = text;
      }
      else if (Boolean.class.equals(klass))
      {
         retval = Boolean.parseBoolean(text);
      }
      else if (Integer.class.equals(klass))
      {
         try
         {
            retval = Integer.parseInt(text);
         }
         catch (NumberFormatException ignored)
         {
         }
      }

      if (retval == null)
      {
         throw new IllegalArgumentException(getString(AutoStep.class, "FailedToParse", text, klass));
      }

      return retval;
   }
}
