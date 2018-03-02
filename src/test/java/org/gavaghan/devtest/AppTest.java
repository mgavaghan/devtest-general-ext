package org.gavaghan.devtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.itko.lisa.test.ITestExec;

public class AppTest
{
	static private String loadContent() throws IOException
	{
		StringBuilder builder = new StringBuilder();
		char[] buffer = new char[1024];

		try (InputStream is = AppTest.class.getResourceAsStream("ListTaggedContent.xml"); InputStreamReader isr = new InputStreamReader(is))
		{
			for (;;)
			{
				int got = isr.read(buffer);
				if (got < 0) break;

				builder.append(buffer, 0, got);
			}
		}

		return builder.toString();
	}

	static public void writeFrags(String indent, List<ITagFragment> frags)
	{
		for (ITagFragment frag : frags)
		{
			if (frag instanceof ListDocument)
			{
				ListDocument doc = (ListDocument) frag;
				System.out.print(frag.getContent());
				writeFrags("", doc.getTagFragments());
			}
			else
			{
				System.out.print(indent + frag);
			}
		}
	}

	static public void main(String[] args) throws IOException
	{
		String content = loadContent();

		ListDocument doc = ListDocument.read(content);

		//writeFrags(">", doc.getTagFragments());

		//System.out.println(doc.getTrailer());
		
		ListDocumentRenderer ldr = new ListDocumentRenderer();
		ITestExec testExec = new TestTestExec();
		
		testExec.setStateValue("color", "blue");
		
		System.out.println(ldr.render(doc, testExec));
		
		//System.out.println(testExec.parseInState(content));
	}
}
