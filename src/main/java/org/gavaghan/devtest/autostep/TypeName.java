package org.gavaghan.devtest.autostep;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tag a DevTest step with the desired response to getTypeName().
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface TypeName {
   /**
    * Name of the step type. If 'localized' is true, get it from the resource bundle.
    * Otherwise, it's a literal.
    * 
    * @return getTypeName() response
    */
   String value();

   /**
    * Flag indicating if 'value' is a resource bundle key.
    *  
    * @return 'true' if using a resource bundle
    */
   boolean localized() default false;
}
