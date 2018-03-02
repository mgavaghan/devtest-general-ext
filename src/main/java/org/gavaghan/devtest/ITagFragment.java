package org.gavaghan.devtest;

/**
 * API to a document fragment containing untagged content followed by a
 * Tag.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public interface ITagFragment
{
	/**
	 * Get the fragment content.
	 * 
	 * @return
	 */
	public String getContent();

	/**
	 * Get the fragment tag.
	 * 
	 * @return
	 */
	public Tag getTag();
}
