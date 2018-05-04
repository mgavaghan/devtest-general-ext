package org.gavaghan.devtest.step;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SSHExecuteEditor extends SSHEditorBase
{
	/** Command */
	private JTextField mCommand = new JTextField();

	/**
	 * Get Command.
	 *
	 * @return Command
	 */
	public JTextField getCommand()
	{
		return mCommand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itko.lisa.editor.CustomEditor#isEditorValid()
	 */
	@Override
	public String isEditorValid()
	{
		String valid = super.isEditorValid();

		if (valid == null)
		{
			if (mCommand.getText().trim().length() == 0) valid = "Please specify a Command";
		}
		
		return valid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itko.lisa.editor.CustomEditor#save()
	 */
	@Override
	public void save()
	{
		super.save();
		
		SSHExecuteController controller = (SSHExecuteController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		SSHExecuteStep step = (SSHExecuteStep) controller.getAttribute(SSHExecuteController.STEP_KEY);

		step.setCommand(getCommand().getText());
	}
	
	@Override
	protected void populate()
	{
		super.populate();

		SSHExecuteController controller = (SSHExecuteController) getController();
		SSHExecuteStep step = (SSHExecuteStep) controller.getAttribute(SSHExecuteController.STEP_KEY);

		getCommand().setText(step.getCommand());
	}

	/**
	 * Build the UI.
	 */
	@Override
	protected void addComponents( JPanel mainPanel)
	{
		super.addComponents(mainPanel);
		
		GridBagConstraints gbc;

		// add Command label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getLabel("Command: ", "Specify the command to execute"), gbc);

		// add Command to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getCommand(), gbc);
	}
}
