package org.gavaghan.devtest.dph;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Writer;

import org.gavaghan.json.AbstractJSONValue;
import org.gavaghan.json.JSONException;
import org.gavaghan.json.JSONValueFactory;

/**
 * Special JSONValue implementation to represent DevTest magic strings. This is
 * necessary when any value, other than a string, is magic stringed during the
 * service image recording process. The {{}} notation causes the document to be
 * non-compliant JSON. So, using the proper JSONValueFactory, we can now read
 * and reformat JSON documents containing magic strings.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class JSONMagicString extends AbstractJSONValue
{
	/** The underlying value. **/
	private String mValue;

	/**
	 * Create a new JSONMagicString.
	 */
	public JSONMagicString()
	{
		mValue = "";
	}

	/**
	 * Set the underlying value.
	 * 
	 * @param value
	 */
	public void setValue(String value)
	{
		if (value == null) throw new NullPointerException("Null value not allowed.  Use JSONNull instead.");
		mValue = value;
	}

	/**
	 * Get the underlying value.
	 * 
	 * @return
	 */
	@Override
	public Object getValue()
	{
		return mValue;
	}

	/**
	 * Read a JSON value (presumes the key has already been read).
	 * 
	 * @param path
	 *           path to the value being read
	 * @param pbr
	 *           source reader
	 * @throws IOException
	 *            on read failure
	 * @throws JSONException
	 *            on grammar error
	 */
	@Override
	public void read(String path, PushbackReader pbr) throws IOException, JSONException
	{
		StringBuilder builder = new StringBuilder();

		char c = JSONValueFactory.demand(pbr);
		if (c != '{') throw new JSONException(path, "Leading '{{' expected at start of magic string.");

		c = JSONValueFactory.demand(pbr);
		if (c != '{') throw new JSONException(path, "Leading '{{' expected at start of magic string.");

		for (;;)
		{
			c = JSONValueFactory.demand(pbr);

			// check for close
			if (c == '}')
			{
				c = JSONValueFactory.demand(pbr);

				if (c == '}') break;

				pbr.unread(c);
				builder.append('}');
			}
			else
			{
				builder.append(c);
			}

		}

		mValue = builder.toString();
	}

	/**
	 * Render this JSON value to a Writer.
	 * 
	 * @param indent
	 * @param writer
	 * @throws IOException
	 */
	@Override
	public void write(String indent, Writer writer, boolean pretty) throws IOException
	{
		writer.write("{{");
		writer.write(mValue);
		writer.write("}}");
	}
}
