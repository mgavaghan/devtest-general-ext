package org.gavaghan.devtest.step;

import java.io.PrintWriter;

import javax.swing.Icon;

import com.itko.lisa.editor.TestNodeInfo;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class FolderSaveController extends TestNodeInfo
{
	/** Step key. */
	static final String STEP_KEY = "lisa.FolderSaveStep.key";

	@Override
	public void initNewOne()
	{
		FolderSaveStep node = new FolderSaveStep();
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
		FolderSaveStep node = (FolderSaveStep) getAttribute(STEP_KEY);
		node.writeSubXML(pw);
	}

	@Override
	public void migrate(Object obj)
	{
		FolderSaveStep node = (FolderSaveStep) obj;
		putAttribute(STEP_KEY, node);
	}

	@Override
	public String getEditorName()
	{
		return "Folder Save";
	}

	/**
	 */
	@Override
	public String getHelpString()
	{
		return "Folder Save";
	}
}
