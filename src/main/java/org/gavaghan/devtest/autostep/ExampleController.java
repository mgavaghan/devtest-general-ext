package org.gavaghan.devtest.autostep;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@EditorName("Example Step Editor")
@HelpString("Example Step Help String")
public class ExampleController extends AutoController<ExampleStep>
{
   public ExampleController()
   {
      super(ExampleStep.class);
   }
}
