package org.gavaghan.devtest.dph;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.itko.commons.logging.Log;
import com.itko.commons.logging.LogFactory;
import com.itko.util.StrUtil;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class TrafficPublisherEditor extends JPanel
{
	/** Logger. */
	static private final Log LOG = LogFactory.getLog(TrafficPublisherEditor.class);

	private JTextField mBrokerUrl = new JTextField();
	private JTextField mBrokerUsername = new JTextField();
	private JPasswordField mBrokerPassword = new JPasswordField();
	private JTextField mTopic = new JTextField();
	
	/**
	 * Create new TrafficPublisherEditor.
	 */
	public TrafficPublisherEditor()
	{
		LOG.debug("ENTER - TrafficPublisherEditor()");
		
		setLayout(new GridBagLayout());
		
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
		add(mBrokerUsername, gbc);

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
		LOG.debug("ENTER - isEditorValid()");
		
		if (StrUtil.isEmpty(mBrokerUrl.getText()))  return "Please specify a broker URL.";
		if (StrUtil.isEmpty(mTopic.getText()))  return "Please specify an ActiveMQ topic.";
		return null;
	}

	/**
	 * This method is used to inform the panel that it has been activated.
	 * 
	 * @param config
	 */
	public void display(final TrafficPublisherConfiguration config)
	{
		LOG.debug("ENTER - display()");
		
		mBrokerUrl.setText(config.getBrokerURL());
		mBrokerUsername.setText(config.getBrokerUsername());
		mBrokerPassword.setText(config.getBrokerPassword());
		mTopic.setText(config.getTopic());
	}

	/**
	 * Save the results to the configuration object.
	 * 
	 * @param config
	 * @return true if this saves successfully and false if it doesn't.
	 */
	@SuppressWarnings("deprecation")
	public boolean save(final TrafficPublisherConfiguration config)
	{
		LOG.debug("ENTER - save()");
		
		config.setBrokerURL(mBrokerUrl.getText());
		config.setBrokerUsername(mBrokerUsername.getText());
		config.setBrokerPassword(mBrokerPassword.getText());
		config.setTopic(mTopic.getText());
		return true;
	}
}
