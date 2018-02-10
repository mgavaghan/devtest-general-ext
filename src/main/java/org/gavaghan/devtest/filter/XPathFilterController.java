package org.gavaghan.devtest.filter;

import java.io.PrintWriter;

import com.itko.lisa.editor.FilterController;
import com.itko.util.ParameterList;

/**
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class XPathFilterController extends FilterController
{
	@Override
	public void writeSubXML(PrintWriter ps)
	{
		ParameterList pl = getParameters();
		XPathFilter.writeXML(ps, pl);
	}
}
