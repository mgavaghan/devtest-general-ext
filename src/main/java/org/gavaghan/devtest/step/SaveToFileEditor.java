package org.gavaghan.devtest.step;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gavaghan.devtest.autostep.AutoEditor;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SaveToFileEditor extends AutoEditor<SaveToFileStep>
{
   /** Initialized flag. */
   private boolean mInit = false;

   public SaveToFileEditor()
   {
      super(SaveToFileStep.class);
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.CustomEditor#display()
    */
   @Override
   public void display()
   {
      setupEditor();

      SaveToFileController controller = (SaveToFileController) getController();
      SaveToFileStep step = (SaveToFileStep) controller.getAttribute(controller.getStepKey());

      // FIXME - these might not be JTextFields
      ((JTextField) getComponent("filename")).setText((String) step.getProperty("filename"));
      ((JTextField) getComponent("encoding")).setText((String)  step.getProperty("encoding"));
      ((JTextField) getComponent("content")).setText((String) step.getProperty("content"));
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

      // add Filename label
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 1;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      mainPanel.add(new JLabel("Filename: "), gbc);

      // add Filename to main panel
      gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbc.gridwidth = 1;
      gbc.weightx = 1;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      mainPanel.add(getComponent("filename"), gbc);

      // add Encoding label
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.gridwidth = 1;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      mainPanel.add(new JLabel("Encoding: "), gbc);

      // add Encoding to main panel
      gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 2;
      gbc.gridwidth = 1;
      gbc.weightx = 1;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      mainPanel.add(getComponent("encoding"), gbc);

      // add Content label
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 3;
      gbc.gridwidth = 1;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      mainPanel.add(new JLabel("Content: "), gbc);
      
      // add Content to main panel
      gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 3;
      gbc.gridwidth = 1;
      gbc.weightx = 1;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      mainPanel.add(getComponent("content"), gbc);

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
