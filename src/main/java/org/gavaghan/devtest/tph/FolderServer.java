package org.gavaghan.devtest.tph;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager for multiple clients watching a particular folder.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class FolderServer
{
	/** Logger. */
	static private final Logger LOG = LoggerFactory.getLogger(FolderServer.class);

	/** Maps files to file folder watchers. */
	static private final Map<File, FolderServer> sServers = new HashMap<File, FolderServer>();

	/** The outbound watcher task. */
	private FolderWatcher mWatcher;

	/**
	 * Create a new FileFolderServer.
	 * 
	 * @param watchFolder
	 * @param pollInterval
	 */
	private FolderServer(File watchFolder, int pollInterval)
	{
		if (LOG.isDebugEnabled()) LOG.debug("Constructing watcher on outbound folder: " + watchFolder);
		mWatcher = new FolderWatcher(watchFolder, pollInterval);

		// start the watcher
		Thread t = new Thread(mWatcher);
		t.setDaemon(true);
		t.setName("FolderServer: " + watchFolder);
		t.start();
	}

	/**
	 * Get the watcher for a folder.
	 * 
	 * @param watchFolder
	 * @return 'null' if not created
	 */
	static public synchronized FolderServer get(File watchFolder)
	{
		FolderServer watcher = sServers.get(watchFolder);
		return watcher;
	}

	/**
	 * Create a new watcher.
	 * 
	 * @param watchFolder
	 * @param pollInterval
	 * @return
	 * @throws IOException
	 */
	static public synchronized FolderServer create(File watchFolder, int pollInterval)
	{
		FolderServer server = get(watchFolder);
		if (server != null) throw new RuntimeException("Watcher already on folder: " + watchFolder);

		// create the watcher
		if (LOG.isDebugEnabled()) LOG.debug("Creating watcher on folder: " + watchFolder);
		server = new FolderServer(watchFolder, pollInterval);
		sServers.put(watchFolder, server);

		return server;
	}

	/**
	 * Get the watcher for a folder - creating the watcher if necessary.
	 * 
	 * @param watchFolder
	 * @param pollInterval
	 * @return
	 * @throws IOException
	 */
	static public synchronized FolderServer getOrCreate(File watchFolder, int pollInterval)
	{
		FolderServer server = get(watchFolder);
		if (server == null) server = create(watchFolder, pollInterval);
		return server;
	}

	/**
	 * Shutdown watcher on a folder and remove from list.
	 * 
	 * @param watchFolder
	 * @return
	 */
	static public synchronized FolderServer remove(File watchFolder)
	{
		FolderServer server = get(watchFolder);
		if (server != null)
		{
			sServers.remove(watchFolder);
			server.stop();
		}
		return server;
	}

	/**
	 * Get the next available file.
	 * 
	 * @return
	 */
	public FileContent getNextFile()
	{
		return mWatcher.getNextFile();
	}

	/**
	 * Stop the watcher.
	 */
	public synchronized void stop()
	{
		mWatcher.stop();
	}
}
