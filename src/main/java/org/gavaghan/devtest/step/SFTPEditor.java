package org.gavaghan.devtest.step;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SFTPEditor extends SSHEditorBase
{
	/** Send button. */
	private JRadioButton mSend = new JRadioButton("Send");
	
	/** Receive button. */
	private JRadioButton mReceive = new JRadioButton("Receive");

	/** LocalFile */
	private JTextField mLocalFile = new JTextField();

	/** RemoteFile */
	private JTextField mRemoteFile = new JTextField();
	
	/**
	 * Get LocalFile.
	 *
	 * @return LocalFile
	 */
	public JTextField getLocalFile()
	{
		return mLocalFile;
	}

	/**
	 * Get RemoteFile.
	 *
	 * @return RemoteFile
	 */
	public JTextField getRemoteFile()
	{
		return mRemoteFile;
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
			if (mLocalFile.getText().trim().length() == 0) valid = "Please specify a Local File";
			else if (mRemoteFile.getText().trim().length() == 0) valid = "Please specify a Remote File";
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
		
		SFTPController controller = (SFTPController) getController();
		controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
		SFTPStep step = (SFTPStep) controller.getAttribute(SFTPController.STEP_KEY);

		step.setSendRecv(mSend.isSelected() ? "send" : "recv");
		step.setLocalFile(getLocalFile().getText());
		step.setRemoteFile(getRemoteFile().getText());
	}
	
	@Override
	protected void populate()
	{
		super.populate();

		SFTPController controller = (SFTPController) getController();
		SFTPStep step = (SFTPStep) controller.getAttribute(SFTPController.STEP_KEY);

		if ("send".equals(step.getSendRecv().toLowerCase())) mSend.setSelected(true);
		else mReceive.setSelected(true);
		
		getLocalFile().setText(step.getLocalFile());
		getRemoteFile().setText(step.getRemoteFile());
	}

	/**
	 * Build the UI.
	 */
	@Override
	protected void addComponents( JPanel mainPanel)
	{
		super.addComponents(mainPanel);
		
		GridBagConstraints gbc;
		ButtonGroup group = new ButtonGroup();
		
		group.add(mSend);
		group.add(mReceive);
		
		// build button box
		JPanel bbox = new JPanel(new GridBagLayout());
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		bbox.add(mSend, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		bbox.add(mReceive, gbc);
		
		// add SendRecv label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getLabel("Direction: ", "File transmit direction"), gbc);

		// add SendRecv to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(bbox, gbc);

		// add LocalFile label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getLabel("Local File: ", ""), gbc);

		// add LocalFile to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mLocalFile, gbc);

		// add RemoteFile label
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(getLabel("Remote File: ", ""), gbc);

		// add RemoteFile to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 9;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mRemoteFile, gbc);
	}
}
