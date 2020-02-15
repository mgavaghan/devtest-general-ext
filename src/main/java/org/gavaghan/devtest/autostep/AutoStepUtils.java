package org.gavaghan.devtest.autostep;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
   /** Map class types to their resource bundles. */
   static private final Map<Class<?>, ResourceBundle> sBundles = new HashMap<Class<?>, ResourceBundle>();

   static
   {
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
               throw new RuntimeException(getString(AutoStep.class, "NoResourceBundle", klass.getName()));
            }

            sBundles.put(klass, bundle);
         }
      }

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
      try
      {
         return MessageFormat.format(getResourceBundle(klass).getString(key), args);
      }
      catch(MissingResourceException exc)
      {
         // the key in the resource file could not be found
         throw new RuntimeException(getString(AutoStep.class, "NoResourceKey", key, klass.getName()));
      }
   }
}
