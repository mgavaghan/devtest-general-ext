package org.gavaghan.devtest.step;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.itko.lisa.editor.CustomEditor;

/**
 *
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SaveToFileEditor extends CustomEditor
{
   /** Initialized flag. */
  private boolean mInit = false;

   /** Filename */
   private JTextField mFilename = new JTextField();

   /** Encoding */
   private JTextField mEncoding = new JTextField();

   /** Content */
   private JTextField mContent = new JTextField();
   
   /**
    * Get Filename.
    *
    * @return Filename
    */
   public JTextField getFilename()
   {
      return mFilename;
   }

   /**
    * Get Encoding.
    *
    * @return Encoding
    */
   public JTextField getEncoding()
   {
      return mEncoding;
   }

   /**
    * Get Content.
    *
    * @return Content
    */
   public JTextField getContent()
   {
      return mContent;
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.CustomEditor#isEditorValid()
    */
   @Override
   public String isEditorValid()
   {
      if (mFilename.getText().trim().length() == 0) return "Please specify a Filename";
      if (mEncoding.getText().trim().length() == 0) return "Please specify an Encoding";
      if (mContent.getText().trim().length() == 0) return "Please specify Content";
      return null;
   }

   /*
    * (non-Javadoc)
    * @see com.itko.lisa.editor.CustomEditor#save()
    */
   @Override
   public void save()
   {
      SaveToFileController controller = (SaveToFileController) getController();
      controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
      SaveToFileStep step = (SaveToFileStep) controller.getAttribute(SaveToFileController.STEP_KEY);

      step.setProperty("filename", getFilename().getText());
      step.setProperty("encoding", getEncoding().getText());
      step.setProperty("content", getContent().getText());
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
      SaveToFileStep step = (SaveToFileStep) controller.getAttribute(SaveToFileController.STEP_KEY);

      getFilename().setText((String) step.getProperty("filename"));
      getEncoding().setText((String) step.getProperty("encoding"));
      getContent().setText((String) step.getProperty("content"));
   }

   /**
    * Build the UI.
    */
   private void setupEditor()
   {
      if (mInit)  return;
      
      mInit = true;

      GridBagConstraints gbc;

      // build the main editor panel
      JPanel mainPanel = new JPanel(new GridBagLayout());
      setMinimumSize(new Dimension(300,300));
      
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
      mainPanel.add(mFilename, gbc);

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
      mainPanel.add(mEncoding, gbc);

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
      mainPanel.add(mContent, gbc);
 
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
