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
public @interface Property
{
   /**
    * The appearance index of this property in the default UI layout. This is
    * ignored if a custom editor is used.
    * 
    * @return appearance index
    */
   int index() default 0;

   /**
    * The name of the property.
    * 
    * @return property name
    */
   String name();

   /**
    * The data type of the property.
    * 
    * @return <code>Class</code> indicating the type of a property. Defaults to
    *         <code>String.class</code>.
    */
   Class<?> type() default String.class;

   /**
    * <p>
    * A description of the property. This is used as the label in the editor panel.
    * </p>
    * <p>
    * If 'localized' is true, get it from the resource bundle. Otherwise, it's a
    * literal.
    * </p>
    * 
    * @return property description. Default is simply the name of the property.
    */
   String description() default "";

   /**
    * Flag indicating if 'description' is a resource bundle key
    * 
    * @return 'true' if using a resource bundle. Defaults is false.
    */
   boolean localized() default false;

   /**
    * Indicates if a field is mandatory.
    * 
    * @return 'true' if mandatory. This is the default.
    */
   boolean mandatory() default true;

   /**
    * Specifies initial value for this property. It must be a <code>String</code>,
    * but if the property type is something else, it will be parsed.
    * 
    * @return default property value. Default is an empty string.
    */
   String initial() default "";

   /**
    * Specifies if the editor should render this field as a
    * <code>JPasswordField</code>. Only applies to components that would otherwise
    * be a <code>JTextField</code?. If set, 'sensitive' takes precedence over
    * 'multiline'.
    * 
    * @return flag indicating if property should be masked in UI. Default is
    *         'false'.
    */
   boolean sensitive() default false;

   /**
    * <p>
    * Specifies if the editor should render this field as a <code>JTextArea</code>.
    * Only applies to components that would otherwise be a <code>JTextField</code>.
    * The value of 'rows' indicates how many rows are visible in the
    * <code>JScrollPane</code>.
    * </p>
    * <p>
    * If 'sensitive' is set to 'true', this value is ignored.
    * </p>
    * 
    * @return
    */
   boolean multiline() default false;

   /**
    * For multiline properties, 'rows' indicates how many rows should be visible in
    * the <code>JScrollPane</code>. The default is 10.
    * 
    * @return
    */
   int rows() default 10;
}
