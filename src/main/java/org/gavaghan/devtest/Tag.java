package org.gavaghan.devtest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Encapsulates a document tag (denoted by @@) and associated arguments.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class Tag
{
	/** Logger. */
	static private final Logger LOG = LogManager.getLogger(Tag.class);

	/** Tag name. */
	private final String mName;

	/** Tag arguments, */
	private final List<String> mArgs;

	/** Tag length. */
	private final int mLength;

	/**
	 * Create a new Tag.
	 * 
	 * @param name
	 * @param args
	 * @param length
	 */
	public Tag(String name, List<String> args, int length)
	{
		mName = name;
		mArgs = (args == null) ? null : Collections.unmodifiableList(args);
		mLength = length;
	}

	/**
	 * Get the Tag name.
	 * 
	 * @return
	 */
	public String getName()
	{
		return mName;
	}

	/**
	 * Get the list of Tag arguments.
	 * 
	 * @return
	 */
	public List<String> getArguments()
	{
		return mArgs;
	}

	/**
	 * Get length of tag and arguments.
	 * 
	 * @return
	 */
	public int getLength()
	{
		return mLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		if (getName() != null)  builder.append("@@");
		
		builder.append(getName());

		if (getArguments() != null)
		{
			boolean first = true;

			builder.append("(");
			for (String arg : getArguments())
			{
				if (first) first = false;
				else builder.append(", ");
				builder.append(arg);
			}
			builder.append(")");
		}

		return builder.toString();
	}

	/**
	 * Read a tag (assumes the leading @@ has already been read).
	 * 
	 * If Tag name is null, it means we failed to parse a Tag.
	 * 
	 * @param text
	 * @return
	 */
	static public Tag read(String text)
	{
		// make sure there is content
		if (text.length() == 0)
		{
			LOG.debug("Out of data looking for a Tag");
			return new Tag(null, null, 0);
		}

		// read the tag name
		String name;
		int i = 0;

		for (; i < text.length(); i++)
		{
			int c = text.charAt(i);

			if (!Character.isAlphabetic((char) c) && !Character.isDigit((char) c)) break;
		}

		name = text.substring(0, i);

		if (name.length() == 0)
		{
			LOG.warn("No tag named found in: " + text);
			return new Tag(null, null, i + 1);
		}

		// if out of data, we're done
		if (i == text.length()) return new Tag(name, new ArrayList<String>(0), i);

		// look for opening paren
		if (text.charAt(i) != '(') return new Tag(name, new ArrayList<String>(0), i);
		i++;

		// read all of the arguments
		List<String> args = new ArrayList<String>();

		// ensure a closing paren
		int nextClose = text.indexOf(')', i);
		if (nextClose < 0)
		{
			LOG.warn("Unclosed Tag arguments: " + text);
			return new Tag(null, null, i);
		}

		for (;;)
		{
			int nextComma = text.indexOf(',', i);

			// if there's no comma, use the close
			if (nextComma < 0)
			{
				String arg = text.substring(i, nextClose).trim();
				if (arg.length() > 0) args.add(arg);
				i = nextClose + 1;
				break;
			}

			// else, comma marks next arg if it's less than close
			if (nextComma < nextClose)
			{
				args.add(text.substring(i, nextComma).trim());
				i = nextComma + 1;
			}
			
			else
			{
				args.add(text.substring(i, nextClose).trim());
				i = nextClose + 1;
				break;
			}
		}

		return new Tag(name, args, i);
	}
}
