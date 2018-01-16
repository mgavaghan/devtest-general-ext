package org.gavaghan.devtest.tph;

import java.io.File;

/**
 * Encapsulates the pairing of a filename and the file contents.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class FileContent
{
	/** The filename. */
	private final File mFilename;

	/** The file content. **/
	private final byte[] mContent;

	/**
	 * Create a new FileContent.
	 * 
	 * @param filename
	 * @param content
	 */
	public FileContent(File filename, byte[] content)
	{
		mFilename = filename;
		mContent = content;
	}

	/**
	 * Get the filename.
	 * 
	 * @return
	 */
	public File getFilename()
	{
		return mFilename;
	}

	/**
	 * Get the content.
	 * 
	 * @return
	 */
	public byte[] getContent()
	{
		return mContent;
	}
}
