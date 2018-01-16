package org.gavaghan.devtest.tph;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread runnable that monitors a folder. When a file appears, it is loaded,
 * appended to the end of a list, and deleted.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class FolderWatcher implements Runnable
{
	/** Logger. */
	static private final Logger LOG = LoggerFactory.getLogger(FolderWatcher.class);

	/** Timestamp comparator. */
	static private Comparator<File> LAST_MODIFIED_COMPARATOR = new LastModifiedComparator();

	/** The folder to watch. */
	private final File mWatchFolder;

	/** The poll interval. */
	private final int mPollInterval;

	/** Files pending return. */
	private LinkedList<FileContent> mPending = new LinkedList<FileContent>();

	/** Flag indicating a stop request. */
	private boolean mStopRequested = false;

	/**
	 * Wait on timer before polling.
	 */
	private synchronized void sleepBeforePolling()
	{
		try
		{
			if (LOG.isDebugEnabled()) LOG.debug("Waiting to poll: " + mWatchFolder);
			wait(mPollInterval);
		}
		catch (InterruptedException ignored)
		{
		}
	}

	/**
	 * Load and delete a file.
	 * 
	 * @param file
	 * @throws IOException
	 */
	private void loadAndDeleteFile(File file) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int got;

		// read the file into an array
		try (FileInputStream fis = new FileInputStream(file))
		{
			for (;;)
			{
				got = fis.read(buffer);
				if (got < 0) break;

				baos.write(buffer, 0, got);
			}
		}

		// delete the file
		file.delete();

		// add it to the pending list.
		synchronized (this)
		{
			FileContent fc = new FileContent(file, baos.toByteArray());
			mPending.add(fc);
			notifyAll();
		}
	}

	/**
	 * Create a new FileFolderWatcher.
	 * 
	 * @param watchFolder
	 *           the folder to watch for new files.
	 * @param pollInterval
	 *           milliseconds between polling.
	 */
	public FolderWatcher(File watchFolder, int pollInterval)
	{
		mWatchFolder = watchFolder;
		mPollInterval = pollInterval;
	}

	/**
	 * Get the watch folder.
	 * 
	 * @return
	 */
	public File getWatchFolder()
	{
		return mWatchFolder;
	}

	/**
	 * Request that the watcher terminate.
	 */
	public synchronized void stop()
	{
		mStopRequested = true;
		notifyAll();
	}

	/**
	 * Get the next available file.
	 * 
	 * @return
	 */
	public synchronized FileContent getNextFile()
	{
		while ((mPending.size() == 0) && !mStopRequested)
		{
			try
			{
				wait();
			}
			catch (InterruptedException exc)
			{
			}
		}

		return mStopRequested ? null : mPending.removeFirst();
	}

	/**
	 * The Thread work loop.
	 */
	@Override
	public void run()
	{
		while (!mStopRequested)
		{
			// wait for poll interval
			sleepBeforePolling();
			if (mStopRequested) break;

			// check the folder for arriving files
			File[] files = mWatchFolder.listFiles();
			if (files == null) continue;

			// sort by timestamp
			Arrays.sort(files, LAST_MODIFIED_COMPARATOR);

			// loop through the files
			for (File file : files)
			{
				if (!file.isFile()) continue; // skip directories
				if (!file.canRead()) continue; // skip unreadable files

				if (LOG.isDebugEnabled()) LOG.debug("Loading: " + file);

				try
				{
					loadAndDeleteFile(file);
				}
				catch (IOException exc)
				{
					LOG.error("Failed to load: " + file, exc);
				}
			}
		}

		LOG.info("Closing FolderWatcher: " + mWatchFolder);
	}
}
