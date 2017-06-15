package org.gavaghan.devtest.dph;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class TrafficPublisherEditor extends JPanel
{
	private JTextField mBrokerUrl = new JTextField();
	private JTextField mBrokerUser = new JTextField();
	private JTextField mBrokerPassword = new JPasswordField();
	private JTextField mTopic = new JTextField();
	
	/**
	 * Create new TrafficPublisherEditor.
	 */
	public TrafficPublisherEditor()
	{
		setLayout(new GridBagLayout());
		
		mBrokerUrl.setText("tcp://localhost:61616");
		
		GridBagConstraints gbc;

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Broker URL: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(mBrokerUrl, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Broker Username: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(mBrokerUser, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Broker Password: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(mBrokerPassword, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("MQ Topic: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(mTopic, gbc);
	}
	
	/**
	 * This method should return an error message if there are entry errors the
	 * user needs to correct before our activity can proceed. If all is well,
	 * then {@code null} should be returned.
	 * 
	 * 
	 * @param dataProtocolConfig
	 *           a {@code ScramblerDataProtocol}
	 * @return the model type information.
	 */
	public String isEditorValid(final TrafficPublisherConfiguration dataProtocolConfig)
	{
		return null;
	}

	/**
	 * This method is used to inform the panel that it has been activated. We use
	 * this as a trigger to include our singleton file chooser.
	 * 
	 * @param dataProtocolConfig
	 *           a {@code ScramblerDataProtocol}
	 */
	public void display(final TrafficPublisherConfiguration dataProtocolConfig)
	{
	}

	/**
	 * Save the results to the (@code ScramblerDataProtocolConfiguration} object.
	 * 
	 * @param dataProtocolConfig
	 *           a {@code ScramblerDataProtocol}
	 * @return true if this saves successfully and false if it doesn't.
	 */
	public boolean save(final TrafficPublisherConfiguration dataProtocolConfig)
	{
		return true;
	}
}
