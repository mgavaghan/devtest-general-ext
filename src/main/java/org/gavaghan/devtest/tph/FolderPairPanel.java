package org.gavaghan.devtest.tph;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itko.lisa.gui.WizardStep;
import com.itko.lisa.vse.stateful.recorder.RecordingWizard;
import com.itko.util.swing.TextMessage;

/**
 * Panel for capturing the outbound and inbound folders for the recording
 * configuration.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class FolderPairPanel extends WizardStep
{
	/** Logger. */
	static private final Logger LOG = LoggerFactory.getLogger(FolderPairPanel.class);

	/** This constant defines the header message for this step. */
	private static final TextMessage mHeader = new TextMessage("Watch Folder Paths", true);

	/** Our wizard. */
	@SuppressWarnings("unused")
	private RecordingWizard mWizard;

	/** Pointer to our protocol. */
	private FolderProtocol mProtocol;

	/** The inbound folder. */
	private JTextField mOutboundFolder = new JTextField();

	/** The outbound folder. */
	private JTextField mInboundFolder = new JTextField();

	/**
	 * This is the only constructor for the
	 * <code>FileFolderConnectionPanel</code> class.
	 * 
	 * @param wizard
	 * @param protocol
	 */
	FolderPairPanel(RecordingWizard wizard, FolderProtocol protocol)
	{
		LOG.debug("FileFolderPairPanel - constructed");

		mWizard = wizard;
		mProtocol = protocol;

		GridBagConstraints gbc;

		// build the main editor panel
		JPanel mainPanel = new JPanel(new GridBagLayout());

		// add listen port to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 3, 0, 0);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Outbound Folder: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 0, 3);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mOutboundFolder, gbc);

		// add target host to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 3, 0, 0);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("Inbound Folder: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 0, 0, 3);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mInboundFolder, gbc);

		// add main panel to editor
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(mainPanel, gbc);
	}

	@Override
	public Component getHeaderComponent()
	{
		return mHeader.getComponent();
	}

	@Override
	public void activate()
	{
		LOG.debug("FolderPairPanel - activate");

		String outbound = mProtocol.getConfig().getOutboundFolder();
		String inbound = mProtocol.getConfig().getInboundFolder();

		mOutboundFolder.setText(outbound);
		mInboundFolder.setText((inbound == null) ? "" : inbound);
	}

	@Override
	public boolean save()
	{
		String outbound = mOutboundFolder.getText().trim();
		String inbound = mInboundFolder.getText().trim();

		mProtocol.getConfig().setOutboundFolder(outbound);
		mProtocol.getConfig().setInboundFolder(inbound);

		return true;
	}
}
