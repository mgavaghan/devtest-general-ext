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
      this.mTrafficPanel = new Gavaghan.TrafficBuddy.TrafficPanel();
      this.mConfigPanel = new Gavaghan.TrafficBuddy.ConfigPanel();
      this.SuspendLayout();
      // 
      // mTrafficPanel
      // 
      this.mTrafficPanel.Dock = System.Windows.Forms.DockStyle.Fill;
      this.mTrafficPanel.Location = new System.Drawing.Point(0, 132);
      this.mTrafficPanel.Name = "mTrafficPanel";
      this.mTrafficPanel.Size = new System.Drawing.Size(1095, 416);
      this.mTrafficPanel.TabIndex = 0;
      // 
      // mConfigPanel
      // 
      this.mConfigPanel.BackColor = System.Drawing.SystemColors.ActiveCaption;
      this.mConfigPanel.Dock = System.Windows.Forms.DockStyle.Top;
      this.mConfigPanel.Location = new System.Drawing.Point(0, 0);
      this.mConfigPanel.Name = "mConfigPanel";
      this.mConfigPanel.Size = new System.Drawing.Size(1095, 132);
      this.mConfigPanel.TabIndex = 1;
      this.mConfigPanel.VisibleChanged += new System.EventHandler(this.mConfigPanel_VisibleChanged);
      this.mConfigPanel.Connect += new System.EventHandler<System.EventArgs>(this.mConfigPanel_Connect);
      this.mConfigPanel.ConfigChanged += new System.EventHandler<System.EventArgs>(this.mConfigPanel_ConfigChanged);
      // 
      // TopicPanel
      // 
      this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
      this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
      this.Controls.Add(this.mTrafficPanel);
      this.Controls.Add(this.mConfigPanel);
      this.Name = "TopicPanel";
      this.Size = new System.Drawing.Size(1095, 548);
      this.VisibleChanged += new System.EventHandler(this.TopicPanel_VisibleChanged);
      this.ResumeLayout(false);

    }

    #endregion

    private TrafficPanel mTrafficPanel;
    private ConfigPanel mConfigPanel;
  }
}
