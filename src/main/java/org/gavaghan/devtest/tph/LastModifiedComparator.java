package org.gavaghan.devtest.tph;

import java.io.File;
import java.util.Comparator;

/**
 * Compare one File to another and order them, oldest to newest, by the time
 * they were last modified.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class LastModifiedComparator implements Comparator<File>
{
	/**
	 * Compare timestamps of two files
	 */
	@Override
	public int compare(File file1, File file2)
	{
		if (file1 == null)
		{
			return (file2 == null) ? 0 : -1;
		}
		if (file2 == null) return +1;
		if (file1 == file2) return 0;
		if (file1.lastModified() == file2.lastModified()) return 0;
		return (file1.lastModified() < file2.lastModified()) ? -1 : +1;
	}
}
