package org.gavaghan.devtest.filter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.itko.lisa.editor.CustomEditor;

/**
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class XPathFilterEditor extends CustomEditor
{
	/** Initialized flag. */
	private boolean mInit = false;

	/** The path to query. */
	private JTextField mXPath = new JTextField();
	
	/** Flag if we should choose a namespace aware parser. */
	private JCheckBox mNamespaceAware = new JCheckBox();
	
	// FIXME need radio buttons for NODE, NODE_LIST, and STRING
	
	@Override
	public String isEditorValid()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void display()
	{
		setupEditor();
		
	}

	@Override
	public void save()
	{
		// TODO Auto-generated method stub
		
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

		// add XPath to main panel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(new JLabel("XPath: "), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mXPath, gbc);

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
}
