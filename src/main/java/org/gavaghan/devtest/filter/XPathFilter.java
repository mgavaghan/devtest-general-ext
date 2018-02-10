package org.gavaghan.devtest.filter;

import java.io.PrintWriter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.itko.lisa.test.FilterBaseImpl;
import com.itko.lisa.test.TestDefException;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestRunException;
import com.itko.util.Parameter;
import com.itko.util.ParameterList;
import com.itko.util.XMLUtils;

/**
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
@SuppressWarnings("deprecation")
public class XPathFilter extends FilterBaseImpl
{
	/** Logger. */
	static private final Logger LOG = LogManager.getLogger(XPathFilter.class);

	/**
	 * Create a new TracingFilter.
	 */
	public XPathFilter()
	{
		markFilterAsGlobal(true);
		setThreadSafe(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itko.lisa.test.FilterBaseImpl#getTypeName()
	 */
	@Override
	public String getTypeName()
	{
		return "XPath Filter";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itko.lisa.test.FilterBaseImpl#supportsDesignTimeExecution()
	 */
	@Override
	public boolean supportsDesignTimeExecution()
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itko.lisa.test.FilterBaseImpl#supportsDynamicResponseToFilter()
	 */
	@Override
	public boolean supportsDynamicResponseToFilter()
	{
		return false;
	}

	@Override
	public ParameterList getParameters()
	{
		ParameterList p = new ParameterList();

		return p;
	}

	/*
	 * (non-Javadoc)
	 * @see com.itko.lisa.test.FilterBaseImpl#initialize(org.w3c.dom.Element)
	 */
	@Override
	public void initialize(Element elem) throws TestDefException
	{
		com.itko.lisa.xml.FilterXMLXPath foo;
		com.itko.lisa.xml.FilterXMLXPathController bar;
		com.itko.lisa.xml.FilterXMLXPathEditor editor;
	}
	
	static public void writeXML(PrintWriter printWriter, ParameterList parameterList)
	{
		Parameter parameter = parameterList.getParameter("prop");
		XMLUtils.streamTagAndChild(printWriter, "prop", parameter != null ? parameter.getValue() : "");

		parameter = parameterList.getParameter("xpathq");
		XMLUtils.streamTagAndChild(printWriter, "xpathq", parameter != null ? parameter.getValue() : "");
	}

	/*
	 * (non-Javadoc)
	 * @see com.itko.lisa.test.FilterBaseImpl#subPreFilter(com.itko.lisa.test.TestExec)
	 */
	@Override
	public boolean subPreFilter(TestExec arg0) throws TestRunException
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.itko.lisa.test.FilterBaseImpl#subPostFilter(com.itko.lisa.test.TestExec)
	 */
	@Override
	public boolean subPostFilter(TestExec arg0) throws TestRunException
	{
		// TODO Auto-generated method stub
		return false;
	}
}
