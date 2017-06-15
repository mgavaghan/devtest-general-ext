package org.gavaghan.devtest.dph;

import java.io.StringReader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.gavaghan.json.JSONValue;
import org.gavaghan.json.JSONValueFactory;

import com.itko.lisa.test.TestExec;
import com.itko.lisa.vse.stateful.model.Response;
import com.itko.lisa.vse.stateful.protocol.DataProtocol;

/**
 * During the recording process (only) pretty-print JSON documents.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class JSONResponseFormatter extends DataProtocol
{
	/** Logger. */
	static private final Logger LOG = LogManager.getLogger(JSONResponseFormatter.class);

	/**
	 * Use this for parsing JSON responses - it can deal with magic strings.
	 */
	static private final JSONValueFactory sJVF = new DevTestJSONValueFactory();

	/**
	 * Fix-up response during recording.
	 */
	@Override
	public void updateResponse(TestExec testExec, Response response)
	{
		String jsonData = response.getBodyAsString();

		try (StringReader rdr = new StringReader(jsonData))
		{
			JSONValue value = sJVF.read(rdr);
			response.setBody(value.toPrettyString());
		}
		catch (Exception exc)
		{
			LOG.warn("Failed to format as JSON", exc);
		}
	}
}
