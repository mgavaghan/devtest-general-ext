package org.gavaghan.devtest.dph;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.BorderFactory;

import com.itko.lisa.gui.WizardStep;
import com.itko.util.swing.TextMessage;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class TrafficPublisherWizardPanel extends WizardStep
{
	private final TrafficPublisherEditor mEditor;
	private final TrafficPublisher mDataProtocol;
	private boolean mIsRequestSide;
	

	/**
	 * @param dataProtocol
	 */
	public TrafficPublisherWizardPanel(final TrafficPublisher dataProtocol, boolean isRequestSide)
	{
		mDataProtocol = dataProtocol;
		mIsRequestSide = isRequestSide;
		setLayout(new GridLayout(1, 1));
		setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
		mEditor = new TrafficPublisherEditor();
		add(mEditor);
		mDataProtocol.setEditor(mEditor);
	}

	@Override
	public Component getHeaderComponent()
	{
		if (mIsRequestSide)  return new TextMessage("Configure connection to ActiveMQ for REQUESTS", true).getComponent();
		return new TextMessage("Configure connection to ActiveMQ for RESPONSES", true).getComponent();
	}

	@Override
	public void activate()
	{
		mEditor.display(mDataProtocol.getConfig());
	}

	@Override
	public boolean save()
	{
		return mEditor.save(mDataProtocol.getConfig());
	}
}
