package org.gavaghan.devtest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates a document fragment containing untagged content followed by a
 * Tag.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class TaggedDocument
{
	/** The list of fragments making up this document. */
	private final List<TagFragment> mFrags;

	/** The trailing, untagged content. */
	private final String mTrailer;

	/**
	 * Create a new TaggedDocument.
	 * 
	 * @param frags
	 * @param trailer
	 */
	public TaggedDocument(List<TagFragment> frags, String trailer)
	{
		mFrags = (frags == null) ? null : Collections.unmodifiableList(frags);
		mTrailer = trailer;
	}

	/**
	 * Get the tag fragments.
	 * 
	 * @return
	 */
	public List<TagFragment> getTagFragments()
	{
		return mFrags;
	}

	/**
	 * Get the trailer.
	 * 
	 * @return
	 */
	public String getTrailer()
	{
		return mTrailer;
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

		for (TagFragment frag : getTagFragments())
		{
			builder.append(frag);
		}

		if (getTrailer().length() > 0)
		{
			builder.append(getTrailer());
			builder.append("\n");
		}

		return builder.toString();
	}

	/**
	 * Read a tagged document.
	 * 
	 * @param text
	 * @return
	 */
	static public TaggedDocument read(String text)
	{
		List<TagFragment> mFrags = new ArrayList<TagFragment>();
		StringBuilder content = new StringBuilder();
		int i = 0;

		for (;;)
		{
			// look for tag
			int start = text.indexOf("@@", i);

			// if no more tags, the rest is the trailer
			if (start < 0)
			{
				content.append(text.substring(i));
				break;
			}

			// if out of data, tag start is content
			if ((start + 2) == text.length())
			{
				content.append(text.substring(i));
				break;
			}

			// if next char is '@' then it's a literal
			if (text.charAt(start + 2) == '@')
			{
				content.append(text.substring(i, start + 2));
				i = start + 3;
			}

			// else, it's possibly a real tag
			else
			{
				content.append(text.substring(i, start));

				Tag tag = Tag.read(text.substring(start + 2));
				if (tag.getName() != null)
				{
					TagFragment frag = new TagFragment(content.toString(), tag);

					mFrags.add(frag);
					content = new StringBuilder();

					i = start + 2 + tag.getLength();
				}
				else
				{
					content.append(text.substring(start, start + tag.getLength()));
					i = start + tag.getLength();
				}
			}
		}

		return new TaggedDocument(mFrags, content.toString());
	}
}
