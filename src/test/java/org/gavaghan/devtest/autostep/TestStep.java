package org.gavaghan.devtest.autostep;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.itko.lisa.test.TestExec;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@TypeName(value = "TypeName", localized = true)
@Property(name = "first", localized=true)
@Property(name = "second", description = "My Second Prop", type=int.class)
public class TestStep extends AutoStep
{
   /** Our logger. */
   static private final Logger LOG = LogManager.getLogger(TestStep.class);

   public TestStep()
   {
   }

   @Override
   public String getTypeName()
   {
      return "foo";
   }

   @Override
   protected Object doNodeLogic(TestExec testExec) throws Exception
   {
      // TODO Auto-generated method stub
      return null;
   }
}
