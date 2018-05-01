package org.gavaghan.devtest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itko.lisa.test.ITestExec;

/**
 * A specialized TaggedDocument that implements the ITagFragment interface. This
 * allows LIST tags to convert contained tags into ListDocument substructure.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class ListDocumentRenderer
{
	/** Logger. */
	static private final Logger LOG = LoggerFactory.getLogger(ListDocumentRenderer.class);

	/**
	 * Render a List document to a stream.
	 * 
	 * @param doc
	 * @param testExec
	 * @param writer
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void renderFrags(ListDocument doc, ITestExec testExec, Writer writer) throws IOException
	{
		List<ITagFragment> frags = doc.getTagFragments();

		for (ITagFragment frag : frags)
		{
			// are we rendering a LIST fragment?
			if (frag instanceof ListDocument)
			{
				// cast it and write the fragment header
				ListDocument subdoc = (ListDocument) frag;
				writer.write(testExec.parseInState(frag.getContent()));

				// get the argument and render the frag for each item in the passed
				// list
				Tag tag = subdoc.getTag();
				List<String> args = tag.getArguments();

				if ((args.size() == 1) || (args.size() == 2))
				{
					String listName = args.get(0);
					String prefix = (args.size() == 2) ? args.get(1) : null;
					Object listObject = testExec.getStateValue(listName);

					// only render if the state value is a List instance
					if (listObject instanceof List)
					{
						List list = (List) listObject;
						int index = 0;

						for (Object itemObj : list)
						{
							if (itemObj instanceof Map)
							{
								// put map values into state
								Map<String, Object> values = (Map) itemObj;
								String prefixUnder = (prefix != null) ? (prefix + "_") : "";

								for (String key : values.keySet())
								{
									testExec.setStateValue(prefixUnder + key, values.get(key));
									if (LOG.isDebugEnabled()) LOG.debug("Setting state value: " + prefixUnder + key);
								}
								testExec.setStateValue(prefixUnder + "index", new Integer(index));

								// render the fragment
								renderFrags(subdoc, testExec, writer);

								// remove map values from state
								for (String key : values.keySet())
								{
									testExec.removeState(prefixUnder + key);
									if (LOG.isDebugEnabled()) LOG.debug("Removing state value: " + prefixUnder + key);
								}
								testExec.removeState(prefixUnder + "index");
								index++;
							}
							else
							{
								LOG.warn("Item in list '" + listName + "' is not a java.util.Map and won't be rendered");
							}
						}
					}
					else
					{
						LOG.warn("List named '" + listName + "' is not a java.util.List and won't be rendered.");
					}
				}
			}
			// otherwise, we're just rendering regular content
			else
			{
				writer.write(testExec.parseInState(frag.getContent()));
			}
		}
	}

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
	 * Render a ListDocument to a Writer
	 * 
	 * @param doc
	 * @param testExec
	 * @param writer
	 * @throws IOException
	 */
	public void render(ListDocument doc, ITestExec testExec, Writer writer) throws IOException
	{
		renderFrags(doc, testExec, writer);

		writer.write(testExec.parseInState(doc.getTrailer()));
	}
}
