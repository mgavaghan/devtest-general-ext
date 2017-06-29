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
      System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainForm));
      this.mMenuBar = new System.Windows.Forms.MenuStrip();
      this.fileToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
      this.importTrafficToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
      this.exportTrafficToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
      this.toolStripSeparator1 = new System.Windows.Forms.ToolStripSeparator();
      this.exitToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
      this.mExportFileDialog = new System.Windows.Forms.SaveFileDialog();
      this.mTopicPanel = new Gavaghan.TrafficBuddy.TopicPanel();
      this.mMenuBar.SuspendLayout();
      this.SuspendLayout();
      // 
      // mMenuBar
      // 
      this.mMenuBar.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.fileToolStripMenuItem});
      this.mMenuBar.Location = new System.Drawing.Point(0, 0);
      this.mMenuBar.Name = "mMenuBar";
      this.mMenuBar.Size = new System.Drawing.Size(1013, 28);
      this.mMenuBar.TabIndex = 1;
      this.mMenuBar.Text = "menuStrip1";
      // 
      // fileToolStripMenuItem
      // 
      this.fileToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.importTrafficToolStripMenuItem,
            this.exportTrafficToolStripMenuItem,
            this.toolStripSeparator1,
            this.exitToolStripMenuItem});
      this.fileToolStripMenuItem.Name = "fileToolStripMenuItem";
      this.fileToolStripMenuItem.Size = new System.Drawing.Size(44, 24);
      this.fileToolStripMenuItem.Text = "&File";
      // 
      // importTrafficToolStripMenuItem
      // 
      this.importTrafficToolStripMenuItem.Name = "importTrafficToolStripMenuItem";
      this.importTrafficToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.I)));
      this.importTrafficToolStripMenuItem.Size = new System.Drawing.Size(226, 24);
      this.importTrafficToolStripMenuItem.Text = "&Import Traffic...";
      // 
      // exportTrafficToolStripMenuItem
      // 
      this.exportTrafficToolStripMenuItem.Name = "exportTrafficToolStripMenuItem";
      this.exportTrafficToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.E)));
      this.exportTrafficToolStripMenuItem.Size = new System.Drawing.Size(226, 24);
      this.exportTrafficToolStripMenuItem.Text = "&Export Traffic...";
      this.exportTrafficToolStripMenuItem.Click += new System.EventHandler(this.exportTrafficToolStripMenuItem_Click);
      // 
      // toolStripSeparator1
      // 
      this.toolStripSeparator1.Name = "toolStripSeparator1";
      this.toolStripSeparator1.Size = new System.Drawing.Size(223, 6);
      // 
      // exitToolStripMenuItem
      // 
      this.exitToolStripMenuItem.Name = "exitToolStripMenuItem";
      this.exitToolStripMenuItem.Size = new System.Drawing.Size(226, 24);
      this.exitToolStripMenuItem.Text = "E&xit";
      this.exitToolStripMenuItem.Click += new System.EventHandler(this.exitToolStripMenuItem_Click);
      // 
      // mExportFileDialog
      // 
      this.mExportFileDialog.DefaultExt = "json";
      this.mExportFileDialog.Filter = "JSON Files|*.json|All Files|*.*";
      this.mExportFileDialog.Title = "Save Traffic";
      // 
      // mTopicPanel
      // 
      this.mTopicPanel.BrokerPassword = "";
      this.mTopicPanel.BrokerURL = "tcp://localhost:61616";
      this.mTopicPanel.BrokerUsername = "";
      this.mTopicPanel.Dock = System.Windows.Forms.DockStyle.Fill;
      this.mTopicPanel.Location = new System.Drawing.Point(0, 28);
      this.mTopicPanel.Name = "mTopicPanel";
      this.mTopicPanel.Project = "";
      this.mTopicPanel.Service = "";
      this.mTopicPanel.Size = new System.Drawing.Size(1013, 552);
      this.mTopicPanel.TabIndex = 0;
      this.mTopicPanel.Topic = "";
      // 
      // MainForm
      // 
      this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
      this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
      this.ClientSize = new System.Drawing.Size(1013, 580);
      this.Controls.Add(this.mTopicPanel);
      this.Controls.Add(this.mMenuBar);
      this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
      this.MainMenuStrip = this.mMenuBar;
      this.Name = "MainForm";
      this.Text = "Traffic Buddy";
      this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.MainForm_FormClosing);
      this.mMenuBar.ResumeLayout(false);
      this.mMenuBar.PerformLayout();
      this.ResumeLayout(false);
      this.PerformLayout();

    }

    #endregion

    private TopicPanel mTopicPanel;
    private System.Windows.Forms.MenuStrip mMenuBar;
    private System.Windows.Forms.ToolStripMenuItem fileToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem exitToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem importTrafficToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem exportTrafficToolStripMenuItem;
    private System.Windows.Forms.ToolStripSeparator toolStripSeparator1;
    private System.Windows.Forms.SaveFileDialog mExportFileDialog;
  }
}

