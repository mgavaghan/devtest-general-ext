namespace Gavaghan.TrafficBuddy
{
  partial class TopicPanel
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

    #region Component Designer generated code

    /// <summary> 
    /// Required method for Designer support - do not modify 
    /// the contents of this method with the code editor.
    /// </summary>
    private void InitializeComponent()
    {
      this.mConfigPanel = new Gavaghan.TrafficBuddy.ConfigPanel();
      this.mTrafficPanel = new Gavaghan.TrafficBuddy.TrafficPanel();
      this.SuspendLayout();
      // 
      // mConfigPanel
      // 
      this.mConfigPanel.Dock = System.Windows.Forms.DockStyle.Top;
      this.mConfigPanel.Location = new System.Drawing.Point(0, 0);
      this.mConfigPanel.Name = "mConfigPanel";
      this.mConfigPanel.Size = new System.Drawing.Size(1095, 132);
      this.mConfigPanel.TabIndex = 1;
      // 
      // mTrafficPanel
      // 
      this.mTrafficPanel.Location = new System.Drawing.Point(22, 41);
      this.mTrafficPanel.Name = "mTrafficPanel";
      this.mTrafficPanel.Size = new System.Drawing.Size(1026, 468);
      this.mTrafficPanel.TabIndex = 0;
      this.mTrafficPanel.VisibleChanged += new System.EventHandler(this.mTrafficPanel_VisibleChanged);
      // 
      // TopicPanel
      // 
      this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
      this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
      this.Controls.Add(this.mConfigPanel);
      this.Controls.Add(this.mTrafficPanel);
      this.Name = "TopicPanel";
      this.Size = new System.Drawing.Size(1095, 548);
      this.ResumeLayout(false);

    }

    #endregion

    private TrafficPanel mTrafficPanel;
    private ConfigPanel mConfigPanel;
  }
}
