package org.gavaghan.devtest.templates;

import java.io.IOException;

public class AppTest
{
	static public void main(String... args) throws IOException
	{
		App.main(new String[] { "src/test/resources/NewStep.json" });
	}
}
