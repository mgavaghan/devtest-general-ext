package org.gavaghan.devtest.autostep;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class TypeParameterFactory<T extends Object> implements Supplier<T>
{
   /** The constructor for our type parameter. */
   private final Constructor<? extends T> mCtx;

   /**
    * Create a new TypeParameterFactory for creating instances of the type
    * parameter.
    * 
    * @param klass class to instantiate
    */
   public TypeParameterFactory(Class<? extends T> klass)
   {
      Objects.requireNonNull(klass);

      try
      {
         mCtx = klass.getConstructor();
      }
      catch (NoSuchMethodException exc)
      {
         throw new RuntimeException("'" + klass.getName() + "' does not have a default constructor");
      }
   }

   /**
    * Create a new instance of T.
    * 
    * @return a new instance of T
    */
   @Override
   public T get()
   {
      try
      {
         return mCtx.newInstance();
      }
      catch (InvocationTargetException exc)
      {
         throw new RuntimeException(mCtx.getName() + " threw an exception", exc.getCause());
      }
      catch (InstantiationException | IllegalAccessException | IllegalArgumentException exc)
      {
         throw new RuntimeException(mCtx.getName() + " threw an exception", exc);
      }
   }
}
