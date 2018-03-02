package org.gavaghan.devtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Test;

public class TaggedDocumentTest
{
	static private String loadContent() throws IOException
	{
		StringBuilder builder = new StringBuilder();
		char[] buffer = new char[1024];
		
		try (InputStream is = TaggedDocumentTest.class.getResourceAsStream("TaggedContent.txt"); InputStreamReader isr = new InputStreamReader(is))
		{
			for (;;)
			{
				int got = isr.read(buffer);
				if (got < 0)  break;
				
				builder.append(buffer, 0, got);
			}
		}
		
		return builder.toString();
	}
	
	@Test
	public void testAll() throws IOException
	{
		String content = loadContent();
		
		TaggedDocument doc = TaggedDocument.read(content);

		Assert.assertEquals(4, doc.getTagFragments().size());
		
		ITagFragment frag = doc.getTagFragments().get(0);
		
		Assert.assertEquals("TAG1", frag.getTag().getName());
		Assert.assertEquals(0, frag.getTag().getArguments().size());
		Assert.assertEquals("Lorem ipsum dolor ", frag.getContent());
	}
}
