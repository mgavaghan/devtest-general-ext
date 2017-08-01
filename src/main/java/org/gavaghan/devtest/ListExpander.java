package org.gavaghan.devtest;

import com.itko.lisa.test.ITestExec;

public class ListExpander
{
	/**
	 * Static methods only.
	 */
	private ListExpander()
	{
	}

	/**
	 * Get the arguments to a list
	 * 
	 * @param text
	 * @return
	 */
	static private ListArgs getListArgs(String text)
	{
		ListArgs args = new ListArgs();
		String remainder = text.trim();
		
		args.argEnd = text.length();

		// find leading paren
		if (!remainder.startsWith("(")) return args;
		remainder = remainder.substring(1).trim();

		// find comma
		int i = remainder.indexOf(',');
		if (i < 0) return args;
		args.prefix = remainder.substring(0, i).trim();
		remainder = remainder.substring(i + 1).trim();

		// find ending paren
		i = remainder.indexOf(')');
		if (i < 0)
		{
			args.clear();
			return args;
		}
		args.key = remainder.substring(0, i).trim();

		// get arg width
		args.argEnd = text.indexOf(')') + 1;

		return args;
	}

	static private int expandList(StringBuilder builder, String text, ITestExec testExec, String prefix, Object item)
	{
		System.out.println(text);
		return 0;
	}

	static public String expand(String text, ITestExec testExec)
	{
		StringBuilder builder = new StringBuilder();
		String remainder = text;
		int start = -1;

		while (true)
		{
			// look for start of next list
			start = remainder.indexOf("@@LIST", start + 1);
			if (start < 0)
			{
				builder.append(testExec.parseInState(remainder));
				break;
			}

			// get list parameters
			ListArgs listArgs = getListArgs(remainder.substring(start + "@@LIST".length()));

			// append everything up until the list tag
			builder.append(testExec.parseInState(text.substring(0, start)));

			// if parameters couldn't be parsed, continue
			if (listArgs.prefix == null)
			{
				builder.append(remainder.substring(start, start + "@@LIST".length() + listArgs.argEnd));
				remainder = remainder.substring(start + "@@LIST".length() + listArgs.argEnd);
				start = -1;
				continue;
			}

			start += "@@LIST".length() + listArgs.argEnd;

			// now we have a list, so expand it and find out how much was consumed
			StringBuilder expanded = new StringBuilder();
			Object item = testExec.getStateObject(listArgs.key);
			
			int consumed = expandList(builder, remainder.substring(start), testExec, listArgs.prefix, item);

			break;
		}

		return builder.toString();
	}

	static public void main(String[] args)
	{
		String text = "abc@@LIST( pre, list )<{{pre.item}}>@@ENDLIST";

		ITestExec testExec = new TestTestExec();

		System.out.println(ListExpander.expand(text, testExec));
	}
}

class ListArgs
{
	String prefix;
	String key;
	int argEnd;

	void clear()
	{
		prefix = null;
		key = null;
	}
}

