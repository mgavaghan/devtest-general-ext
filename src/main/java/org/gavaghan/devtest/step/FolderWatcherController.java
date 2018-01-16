package org.gavaghan.devtest.step;

import java.io.PrintWriter;

import javax.swing.Icon;

import com.itko.lisa.editor.TestNodeInfo;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class FolderWatcherController extends TestNodeInfo
{
	static final String STEP_KEY = "lisa.FolderWatcherStep.key";

	@Override
	public void initNewOne()
	{
		FolderWatcherStep node = new FolderWatcherStep();
		putAttribute(STEP_KEY, node);
	}

	/**
	 * Large icon is 32x32
	 */
	@Override
	public Icon getLargeIcon()
	{
		return FolderProtocolIcons.getLargeIcon();
	}

	/**
	 * Small icon is 16x16
	 */
	@Override
	public Icon getSmallIcon()
	{
		return FolderProtocolIcons.getSmallIcon();
	}

	@Override
	public void writeSubXML(PrintWriter pw)
	{
		FolderWatcherStep node = (FolderWatcherStep) getAttribute(STEP_KEY);
		node.writeSubXML(pw);
	}

	@Override
	public void migrate(Object obj)
	{
		FolderWatcherStep node = (FolderWatcherStep) obj;
		putAttribute(STEP_KEY, node);
	}

	@Override
	public String getEditorName()
	{
		return "Folder Watcher";
	}

	/**
	 */
	@Override
	public String getHelpString()
	{
		return "Folder Watcher";
	}
}
