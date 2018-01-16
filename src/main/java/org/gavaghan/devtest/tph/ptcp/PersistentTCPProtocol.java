package org.gavaghan.devtest.tph.ptcp;

import com.itko.lisa.gui.WizardStep;
import com.itko.lisa.test.TestCase;
import com.itko.lisa.test.TestExec;
import com.itko.lisa.test.TestNode;
import com.itko.lisa.vse.stateful.protocol.TransportProtocol;
import com.itko.lisa.vse.stateful.recorder.RecordingWizard;
import com.itko.lisa.vse.stateful.recorder.WizardPhase;

/**
 * Protocol handler for file based protocol handling.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class PersistentTCPProtocol extends TransportProtocol implements IPersistentTCPTransactionRecorder
{
	@Override
	public void record(String session, byte[] request, byte[] response)
	{
		// TODO Auto-generated method stub		
	}

	@Override
	protected void beginRecordProcess(TestExec arg0) throws Exception
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void endRecordProcess()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public WizardStep[] getWizardSteps(RecordingWizard paramRecordingWizard, WizardPhase paramWizardPhase)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestNode createListenStep(TestCase arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestNode createRespondStep(TestCase arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
