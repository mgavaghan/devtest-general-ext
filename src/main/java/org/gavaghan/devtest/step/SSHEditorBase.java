package org.gavaghan.devtest.step;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.itko.lisa.editor.CustomEditor;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public abstract class SSHEditorBase extends CustomEditor
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
		if ((mPassword.getText().trim().length() == 0) && (mPrivateKey.getText().trim().length() == 0)) return "Please specify a Password, a Private Key, or both";
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void save()
	{
		SSHControllerBase controller = (SSHControllerBase) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		SSHStepBase step = (SSHStepBase) controller.getAttribute(controller.getStepKey());

		step.setUsername(getUsername().getText());
		step.setHostname(getHostname().getText());
		step.setPort(getPort().getText());
		step.setTimeout(getTimeout().getText());
		step.setPassword(getPassword().getText());
		step.setPrivateKey(getPrivateKey().getText());
		step.setPassphrase(getPassphrase().getText());
	}

	protected void populate()
	{
		SSHControllerBase controller = (SSHControllerBase) getController();
		SSHStepBase step = (SSHStepBase) controller.getAttribute(controller.getStepKey());

		getUsername().setText(step.getUsername());
		getHostname().setText(step.getHostname());
		getPort().setText(step.getPort());
		getTimeout().setText(step.getTimeout());
		getPassword().setText(step.getPassword());
		getPrivateKey().setText(step.getPrivateKey());
		getPassphrase().setText(step.getPassphrase());
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
		populate();
	}

	/**
	 * Helper to provide formatted labels.
	 * 
	 * @param text
	 * @param tip
	 * @return
	 */
	protected JLabel getLabel(String text, String tip)
	{
		JLabel label = new JLabel(text);
		Font font = label.getFont();

		label.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
		label.setToolTipText(tip);

		return label;
	}

	protected void setupEditor()
	{
		if (mInit) return;

		mInit = true;

		// build the main editor panel
		JPanel mainPanel = new JPanel(new GridBagLayout());
		setMinimumSize(new Dimension(300, 300));

		// add all of our components
		addComponents(mainPanel);

		// add main panel to editor
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(mainPanel, gbc);

	}

	protected void addComponents(JPanel mainPanel)
	{
		GridBagConstraints gbc;

		// add Username label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getLabel("Username: ", "Specify user to login as"), gbc);

		// add Username to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getUsername(), gbc);

		// add Hostname label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getLabel("Hostname: ", "Specify remote hostname"), gbc);

		// add Hostname to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getHostname(), gbc);

		// add Port label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getLabel("Port: ", "Specify SSH port"), gbc);

		// add Port to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getPort(), gbc);

		// add Timeout label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getLabel("Timeout (sec): ", "Specify socket timeout in seconds"), gbc);

		// add Timeout to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 15, 0);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getTimeout(), gbc);

		// add Password label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getLabel("Password: ", "(Optional) Specify password if using password authentication"), gbc);

		// add Password to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 15, 0);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getPassword(), gbc);

		// add PrivateKey label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getLabel("Private Key: ", "(Optional) Path to private key file if using private key authentication"), gbc);

		// add PrivateKey to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getPrivateKey(), gbc);

		// add Passphrase label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getLabel("Passphrase: ", "(Optional) Passphrase for private key"), gbc);

		// add Passphrase to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 15, 0);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getPassphrase(), gbc);
	}
}
