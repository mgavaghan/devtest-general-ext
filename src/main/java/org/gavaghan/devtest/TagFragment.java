package org.gavaghan.devtest;

/**
 * Encapsulates a document fragment containing untagged content followed by a
 * Tag.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class TagFragment
{
	/** Preceding untagged content. */
	private final String mContent;

	/** The Tag. */
	private final Tag mTag;

	/**
	 * Create a new TagFragment.
	 * 
	 * @param content
	 * @param tag
	 */
	public TagFragment(String content, Tag tag)
	{
		mContent = content;
		mTag = tag;
	}

	/**
	 * Get the fragment content.
	 * 
	 * @return
	 */
	public String getContent()
	{
		return mContent;
	}

	/**
	 * Get the fragment tag.
	 * 
	 * @return
	 */
	public Tag getTag()
	{
		return mTag;
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

		builder.append(mContent);
		if (mContent.length() != 0) builder.append("\n");
		builder.append(mTag);
		builder.append("\n");
		
		return builder.toString();
	}
}
