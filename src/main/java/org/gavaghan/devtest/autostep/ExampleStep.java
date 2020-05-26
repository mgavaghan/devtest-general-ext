package org.gavaghan.devtest.autostep;

import com.itko.lisa.test.TestExec;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@TypeName("Example Step Name")
@Property(name = "TextField", mandatory = false)
@Property(name = "PasswordField", mandatory = false, sensitive = true)
@Property(name = "TextArea", mandatory = false, multiline = true)
//@Property(name = "Integer", type=int.class, mandatory = false)
@Property(name = "CheckBox", type=boolean.class, mandatory = false)
public class ExampleStep extends AutoStep
{
   @Override
   protected Object doNodeLogic(TestExec testExec) throws Exception
   {
      return "";
   }
}
