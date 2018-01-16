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
public class FolderSaveEditor extends CustomEditor
{
	/** Initialized flag. */
	private boolean mInit = false;

	/** Response property. */
	protected JTextField mResponseListProperty = new JTextField();

	/** Filename property. */
	protected JTextField mFilenameProperty = new JTextField();

	/** Folder to watch. */
	protected JTextField mTargetFolder = new JTextField();

	/**
	 * Get the response property.
	 *
	 * @return
	 */
	public JTextField getResponseListProperty()
	{
		return mResponseListProperty;
	}

	/**
	 * Get the file name property.
	 *
	 * @return
	 */
	public JTextField getFilenameProperty()
	{
		return mFilenameProperty;
	}

	/**
	 * Get the target folder.
	 *
	 * @return
	 */
	public JTextField getTargetFolder()
	{
		return mTargetFolder;
	}

	@Override
	public String isEditorValid()
	{
		if (mResponseListProperty.getText().trim().length() == 0) return "Please specify a response property.";
		if (mFilenameProperty.getText().trim().length() == 0) return "Please specify a filename property.";
		if (mTargetFolder.getText().trim().length() == 0) return "Please specify a target folder.";

		return null;
	}

	/**
	 * Save to the step.
	 */
	@Override
	public void save()
	{
		FolderSaveController controller = (FolderSaveController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		FolderSaveStep step = (FolderSaveStep) controller.getAttribute(FolderSaveController.STEP_KEY);

		step.setResponseListProperty(getResponseListProperty().getText());
		step.setFilenameProperty(getFilenameProperty().getText());
		step.setTargetFolder(getTargetFolder().getText());
	}

	/**
	 * Render the GUI.
	 */
	@Override
	public void display()
	{
		setupEditor();

		FolderSaveController controller = (FolderSaveController) getController();
		FolderSaveStep step = (FolderSaveStep) controller.getAttribute(FolderSaveController.STEP_KEY);

		getResponseListProperty().setText(step.getResponseListProperty());
		getFilenameProperty().setText(step.getFilenameProperty());
		getTargetFolder().setText(step.getTargetFolder());
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

		// add response property to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Response property: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mResponseListProperty, gbc);

		// add filename property to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Filename property: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mFilenameProperty, gbc);

		// add watch folder to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Target folder: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mTargetFolder, gbc);

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
