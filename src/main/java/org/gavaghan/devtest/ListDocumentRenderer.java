package org.gavaghan.devtest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import com.itko.lisa.test.ITestExec;

/**
 * A specialized TaggedDocument that implements the ITagFragment interface. This
 * allows LIST tags to convert contained tags into ListDocument substructure.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class ListDocumentRenderer
{
	/**
	 * Render a ListDocument as a String.
	 * 
	 * @param doc
	 * @param testExec
	 * @return
	 */
	public String render(ListDocument doc, ITestExec testExec)
	{
		String list;

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(baos, "UTF-8");)
		{
			render(doc, testExec, writer);
			writer.flush();
			list = new String(baos.toByteArray(), "UTF-8");
		}
		catch (IOException exc)
		{
			throw new RuntimeException("Unexplained failure rendering a ListDocument", exc);
		}

		return list;
	}

	/**
	 * Render a List document to a stream.
	 * 
	 * @param doc
	 * @param testExec
	 * @param writer
	 * @throws IOException
	 */
	public void render(ListDocument doc, ITestExec testExec, Writer writer) throws IOException
	{
		List<ITagFragment>  frags = doc.getTagFragments();
		
		for (ITagFragment frag : frags)
		{
			// are we rendering a LIST
			if (frag instanceof ListDocument)
			{
				ListDocument subdoc = (ListDocument) frag;
				writer.write(testExec.parseInState(frag.getContent()));
				render(subdoc, testExec, writer);
			}
			// are we rendering an INDEX?
			else
			{
				writer.write(testExec.parseInState(frag.getContent()));
			}
		}
		
		writer.write(testExec.parseInState(doc.getTrailer()));
	}
}
