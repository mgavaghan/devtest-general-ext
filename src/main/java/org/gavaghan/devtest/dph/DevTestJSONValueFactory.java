package org.gavaghan.devtest.dph;

import java.io.IOException;
import java.io.PushbackReader;

import org.gavaghan.json.JSONException;
import org.gavaghan.json.JSONValue;
import org.gavaghan.json.JSONValueFactory;

/**
 * Specialized <code>JSONValueFactory</code> implementation that is capable of
 * reading magic-stringed values in a JSON document as a JSONMagicString.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class DevTestJSONValueFactory extends JSONValueFactory
{
	/**
	 * We need an extra pushback to identify a magic string.
	 */
	@Override
	public int getPushbackBufferSize()
	{
		return Math.max(super.getPushbackBufferSize(), 2);
	}

	/**
	 * Determine if next value is an object or a magic string.
	 */
	@Override
	protected JSONValue onObject(String path, PushbackReader pbr) throws IOException, JSONException
	{
		char c = demand(pbr);
		pbr.unread(c);

		if (c == '{') return new JSONMagicString();

		return super.onObject(path, pbr);
	}
}
