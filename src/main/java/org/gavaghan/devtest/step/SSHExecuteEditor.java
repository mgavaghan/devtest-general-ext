package org.gavaghan.devtest.step;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.itko.lisa.editor.CustomEditor;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SSHExecuteEditor extends CustomEditor
{
	/** Initialized flag. */
	private boolean mInit = false;

	/** Username */
	private JTextField mUsername = new JTextField();

	/** Hostname */
	private JTextField mHostname = new JTextField();

	/** Port */
	private JTextField mPort = new JTextField();

	/** Timeout */
	private JTextField mTimeout = new JTextField();

	/** Command */
	private JTextField mCommand = new JTextField();

	/** Password */
	private JPasswordField mPassword = new JPasswordField();

	/** PrivateKey */
	private JTextField mPrivateKey = new JTextField();

	/** Passphrase */
	private JPasswordField mPassphrase = new JPasswordField();

	/**
	 * Get Username.
	 *
	 * @return Username
	 */
	public JTextField getUsername()
	{
		return mUsername;
	}

	/**
	 * Get Hostname.
	 *
	 * @return Hostname
	 */
	public JTextField getHostname()
	{
		return mHostname;
	}

	/**
	 * Get Port.
	 *
	 * @return Port
	 */
	public JTextField getPort()
	{
		return mPort;
	}

	/**
	 * Get Timeout.
	 *
	 * @return Timeout
	 */
	public JTextField getTimeout()
	{
		return mTimeout;
	}

	/**
	 * Get Command.
	 *
	 * @return Command
	 */
	public JTextField getCommand()
	{
		return mCommand;
	}

	/**
	 * Get Password.
	 *
	 * @return Password
	 */
	public JPasswordField getPassword()
	{
		return mPassword;
	}

	/**
	 * Get PrivateKey.
	 *
	 * @return PrivateKey
	 */
	public JTextField getPrivateKey()
	{
		return mPrivateKey;
	}

	/**
	 * Get Passphrase.
	 *
	 * @return Passphrase
	 */
	public JTextField getPassphrase()
	{
		return mPassphrase;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itko.lisa.editor.CustomEditor#isEditorValid()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public String isEditorValid()
	{
		if (mUsername.getText().trim().length() == 0) return "Please specify a Username";
		if (mHostname.getText().trim().length() == 0) return "Please specify a Hostname";
		if (mPort.getText().trim().length() == 0) return "Please specify a Port";
		if (mTimeout.getText().trim().length() == 0) return "Please specify a Timeout";
		if (mCommand.getText().trim().length() == 0) return "Please specify a Command";
		if ((mPassword.getText().trim().length() == 0) && (mPrivateKey.getText().trim().length() == 0)) return "Please specify a Password, a Private Key, or both";
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itko.lisa.editor.CustomEditor#save()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void save()
	{
		SSHExecuteController controller = (SSHExecuteController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		SSHExecuteStep step = (SSHExecuteStep) controller.getAttribute(SSHExecuteController.STEP_KEY);

		step.setUsername(getUsername().getText());
		step.setHostname(getHostname().getText());
		step.setPort(getPort().getText());
		step.setTimeout(getTimeout().getText());
		step.setCommand(getCommand().getText());
		step.setPassword(getPassword().getText());
		step.setPrivateKey(getPrivateKey().getText());
		step.setPassphrase(getPassphrase().getText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itko.lisa.editor.CustomEditor#display()
	 */
	@Override
	public void display()
	{
		setupEditor();

		SSHExecuteController controller = (SSHExecuteController) getController();
		SSHExecuteStep step = (SSHExecuteStep) controller.getAttribute(SSHExecuteController.STEP_KEY);

		getUsername().setText(step.getUsername());
		getHostname().setText(step.getHostname());
		getPort().setText(step.getPort());
		getTimeout().setText(step.getTimeout());
		getCommand().setText(step.getCommand());
		getPassword().setText(step.getPassword());
		getPrivateKey().setText(step.getPrivateKey());
		getPassphrase().setText(step.getPassphrase());
	}

	/**
	 * Build the UI.
	 */
	private void setupEditor()
	{
		if (mInit) return;

		mInit = true;

		GridBagConstraints gbc;

		// build the main editor panel
		JPanel mainPanel = new JPanel(new GridBagLayout());
		setMinimumSize(new Dimension(300, 300));

		// add Username label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Username: "), gbc);

		// add Username to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mUsername, gbc);

		// add Hostname label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Hostname: "), gbc);

		// add Hostname to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mHostname, gbc);

		// add Port label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Port: "), gbc);

		// add Port to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mPort, gbc);

		// add Timeout label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Timeout: "), gbc);

		// add Timeout to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mTimeout, gbc);

		// add Command label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Command: "), gbc);

		// add Command to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mCommand, gbc);

		// add Password label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Password: "), gbc);

		// add Password to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mPassword, gbc);

		// add PrivateKey label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("PrivateKey: "), gbc);

		// add PrivateKey to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mPrivateKey, gbc);

		// add Passphrase label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Passphrase: "), gbc);

		// add Passphrase to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mPassphrase, gbc);

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
