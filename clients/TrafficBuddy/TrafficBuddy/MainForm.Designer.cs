namespace Gavaghan.TrafficBuddy
{
  partial class MainForm
  {
    /// <summary>
    /// Required designer variable.
    /// </summary>
    private System.ComponentModel.IContainer components = null;

    /// <summary>
    /// Clean up any resources being used.
    /// </summary>
    /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
    protected override void Dispose(bool disposing)
    {
      if (disposing && (components != null))
      {
        components.Dispose();
      }
      base.Dispose(disposing);
    }

    #region Windows Form Designer generated code

    /// <summary>
    /// Required method for Designer support - do not modify
    /// the contents of this method with the code editor.
    /// </summary>
    private void InitializeComponent()
    {
      this.mTopicPanel = new Gavaghan.TrafficBuddy.TopicPanel();
      this.SuspendLayout();
      // 
      // mTopicPanel
      // 
      this.mTopicPanel.BrokerPassword = null;
      this.mTopicPanel.BrokerURL = "tcp://localhost:61616";
      this.mTopicPanel.BrokerUsername = null;
      this.mTopicPanel.Dock = System.Windows.Forms.DockStyle.Fill;
      this.mTopicPanel.Location = new System.Drawing.Point(0, 0);
      this.mTopicPanel.Name = "mTopicPanel";
      this.mTopicPanel.Project = null;
      this.mTopicPanel.Service = null;
      this.mTopicPanel.Size = new System.Drawing.Size(887, 548);
      this.mTopicPanel.TabIndex = 0;
      this.mTopicPanel.Topic = "SomeTopic";
      // 
      // MainForm
      // 
      this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
      this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
      this.ClientSize = new System.Drawing.Size(887, 548);
      this.Controls.Add(this.mTopicPanel);
      this.Name = "MainForm";
      this.Text = "Form1";
      this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.MainForm_FormClosing);
      this.ResumeLayout(false);

    }

    #endregion

    private TopicPanel mTopicPanel;
  }
}

