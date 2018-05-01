package org.gavaghan.devtest.dph;

import org.gavaghan.devtest.ListDocument;
import org.gavaghan.devtest.ListDocumentRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itko.lisa.test.TestExec;
import com.itko.lisa.vse.stateful.model.Request;
import com.itko.lisa.vse.stateful.model.Response;
import com.itko.lisa.vse.stateful.model.TransientResponse;
import com.itko.lisa.vse.stateful.protocol.DataProtocol;

/**
 * <p>
 * On response, expand list components bounded by @@LIST(state) and @@ENDLIST.
 * 'state' is a list of maps and the content of the LIST will be repeated for
 * each item in 'state'. On each iteration, the map found in the list is copied
 * into testExec.
 * </p>
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class ListProcessorHandler extends DataProtocol
{
	/** Logger. */
	static private final Logger LOG = LoggerFactory.getLogger(ListProcessorHandler.class);

	/** Our singleton, threadsafe rendered. */
	static private final ListDocumentRenderer sRenderer = new ListDocumentRenderer();
	
	/*
	 * (non-Javadoc)
	 * @see com.itko.lisa.vse.stateful.protocol.DataProtocol#updateRequest(com.itko.lisa.test.TestExec, com.itko.lisa.vse.stateful.model.Request)
	 */
	@Override
	public void updateRequest(TestExec testExec, Request request)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see com.itko.lisa.vse.stateful.protocol.DataProtocol#updateResponse(com.itko.lisa.test.TestExec, com.itko.lisa.vse.stateful.model.Response)
	 */
	@Override
	public void updateResponse(TestExec testExec, Response response)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see com.itko.lisa.vse.stateful.protocol.DataProtocol#updateResponse(com.itko.lisa.test.TestExec, com.itko.lisa.vse.stateful.model.TransientResponse)
	 */
	@Override
	public void updateResponse(TestExec testExec, TransientResponse response)
	{
		LOG.trace("updateResponse(TestExec testExec, TransientResponse response)");
		String body = response.getBodyAsString();
		
		if (LOG.isDebugEnabled())  LOG.debug("Incoming body: \n" + body);
		
		ListDocument doc = ListDocument.read(body);
		String expanded = sRenderer.render(doc, testExec);
		
		if (LOG.isDebugEnabled())  LOG.debug("Outgoing body: \n" + expanded);
		
		response.setBody(expanded);
	}
}
