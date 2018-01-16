package org.gavaghan.devtest.step;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.itko.lisa.editor.CustomEditor;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class FolderWatcherEditor extends CustomEditor
{
	/** Initialized flag. */
	private boolean mInit = false;

	/** Folder to watch. */
	protected JTextField mWatchFolder = new JTextField();

	/** Poll interval. */
	protected JTextField mPollInterval = new JTextField();

	/**
	 * Get the watch folder.
	 * 
	 * @return
	 */
	public JTextField getWatchFolder()
	{
		return mWatchFolder;
	}

	/**
	 * Get the poll interval.
	 * 
	 * @return
	 */
	public JTextField getPollInterval()
	{
		return mPollInterval;
	}

	@Override
	public String isEditorValid()
	{
		if (mWatchFolder.getText().trim().length() == 0) return "Please specify a folder to watch.";
		if (mPollInterval.getText().trim().length() == 0) return "Please specify a poll interval.";
		return null;
	}

	/**
	 * Save to the step.
	 */
	@Override
	public void save()
	{
		FolderWatcherController controller = (FolderWatcherController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		FolderWatcherStep step = (FolderWatcherStep) controller
				.getAttribute(FolderWatcherController.STEP_KEY);

		step.setWatchFolder(getWatchFolder().getText());
		step.setPollInterval(getPollInterval().getText());
	}

	/**
	 * Render the GUI.
	 */
	@Override
	public void display()
	{
		setupEditor();

		FolderWatcherController controller = (FolderWatcherController) getController();
		FolderWatcherStep step = (FolderWatcherStep) controller
				.getAttribute(FolderWatcherController.STEP_KEY);

		getWatchFolder().setText(step.getWatchFolder());
		getPollInterval().setText(step.getPollInterval());
	}

	/**
	 * 
	 */
	protected void setupEditor()
	{
		if (mInit) return;

		mInit = true;

		GridBagConstraints gbc;

		// build the main editor panel
		JPanel mainPanel = new JPanel(new GridBagLayout());

		// add watch folder to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Watch folder: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mWatchFolder, gbc);

		// add poll interval to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Poll interval (ms): "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mPollInterval, gbc);

		// add main panel to editor
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(mainPanel, gbc);
	}
}
