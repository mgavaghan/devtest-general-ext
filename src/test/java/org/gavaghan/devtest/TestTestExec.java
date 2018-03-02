package org.gavaghan.devtest;

import java.util.HashMap;
import java.util.Map;

import com.itko.lisa.test.ITestExec;

public class TestTestExec implements ITestExec
{
	private Map<String,Object> mMap = new HashMap<String,Object>();
	
	@Override
	public void raiseEvent(int paramInt, String paramString1, String paramString2, Throwable paramThrowable)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void raiseEvent(int paramInt, String paramString1, String paramString2)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void raiseEvent(int paramInt, String paramString1, String paramString2, boolean paramBoolean)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void raiseEvent(int paramInt, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public long getLastNodeResponseTime()
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void log(String paramString)
	{
	}

	@Override
	public void log(String paramString1, String paramString2)
	{
	}

	@Override
	public void setNextNode(String paramString)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getNextNode()
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getCurrentNodeName()
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setLastResponse(Object paramObject)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Object getNodeResponse(String paramString)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Object getLastResponse()
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean getStateBoolean(String paramString, boolean paramBoolean)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getStateInt(String paramString, int paramInt)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getStateString(String paramString1, String paramString2)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setEncryptedStateValue(String paramString1, String paramString2, String paramString3)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getEncryptedStateString(String paramString1, String paramString2, String paramString3)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Object getStateObject(String paramString)
	{
		return getStateValue(paramString);
	}

	@Override
	public Object getStateValue(String paramString)
	{
		return mMap.get(paramString);
	}

	@Override
	public void setStateValue(String paramString, Object paramObject)
	{
		mMap.put(paramString, paramObject);
	}

	@Override
	public void setStateObject(String paramString, Object paramObject)
	{
		setStateValue(paramString, paramObject);
	}

	@Override
	public void setStateValue(String paramString, Object paramObject, boolean paramBoolean)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void removeState(String paramString)
	{
		mMap.remove(paramString);
	}

	@Override
	public void setStateValues(Map paramMap, boolean paramBoolean)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setStateValues(Map paramMap, boolean paramBoolean1, boolean paramBoolean2)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Map getAllState()
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean isShootBlanks()
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String[] getStateProps()
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Object exececuteExpression(String paramString)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String parseInState(String paramString)
	{
		// if nothing to expand
		if (paramString.indexOf("{{") < 0)  return paramString;
		
		StringBuilder builder = new StringBuilder();
		int index = 0;
		
		for (;;)
		{
			int start = paramString.indexOf("{{", index);
			if (start < 0)  break;
			
			int end = paramString.indexOf("}}", index+2);
			
			// get content up to expression
			builder.append(paramString.substring(index, start));
			
			// get the value to insert
			String name = paramString.substring(start+2, end);
			Object value = mMap.get(name);
			
			if (value != null)  builder.append(parseInState(value.toString()));
			
			index = end + 2;
		}
		
		builder.append(paramString.substring(index));
		
		return builder.toString();
	}

	@Override
	public void parseInState(StringBuffer paramStringBuffer)
	{
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void parseInState(StringBuilder builder)
	{
		throw new RuntimeException("Not implemented");
	}
	
	static public void main(String[] args)
	{
		TestTestExec testExec = new TestTestExec();
		
		testExec.setStateObject("color", "red");
		testExec.setStateObject("number", "five");
		testExec.setStateObject("size", "large");
		testExec.setStateObject("mashup", "{{color}} plus {{number}}");
		
		System.out.println(testExec.parseInState("size = {{size}}; unknown = {{unknown}}; mashup = {{mashup}}"));
		
		System.out.println(testExec.parseInState("<outer>\r\n   <test>{{color}}</test>\r\n   <inner>"));
	}
}
