package org.gavaghan.devtest.autostep;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a named property of a step
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(Properties.class)
public @interface Property {
   /**
    * The name of the property.
    */
   String value();
   
   /**
    * The data type of the property.  Defaults to String.class  
    */
   Class<?> type() default String.class;

   /**
    * <p>
    * A description of the property. This is used as the label in the editor panel.
    * </p>
    * <p>
    * The default description is simply the name of the property.
    * </p>
    * <p>
    * If 'localized' is true, get it from the resource bundle. Otherwise, it's a
    * literal.
    * </p>
    */
   String description() default "";

   /**
    * Flag indicating if 'description' is a resource bundle key.
    *  
    * @return 'true' if using a resource bundle
    */
   boolean localized() default false;
}
