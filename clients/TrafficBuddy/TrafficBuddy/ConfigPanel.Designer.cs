namespace Gavaghan.TrafficBuddy
{
  partial class ConfigPanel
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
      this.label1 = new System.Windows.Forms.Label();
      this.label2 = new System.Windows.Forms.Label();
      this.label3 = new System.Windows.Forms.Label();
      this.mBrokerURLTextBox = new System.Windows.Forms.TextBox();
      this.mUsernameTextBox = new System.Windows.Forms.TextBox();
      this.mPasswordTextBox = new System.Windows.Forms.TextBox();
      this.groupBox1 = new System.Windows.Forms.GroupBox();
      this.groupBox2 = new System.Windows.Forms.GroupBox();
      this.label4 = new System.Windows.Forms.Label();
      this.mTopicTextBox = new System.Windows.Forms.TextBox();
      this.label5 = new System.Windows.Forms.Label();
      this.mProjectTextBox = new System.Windows.Forms.TextBox();
      this.label6 = new System.Windows.Forms.Label();
      this.mServiceTextBox = new System.Windows.Forms.TextBox();
      this.mImportButton = new System.Windows.Forms.Button();
      this.mExportButton = new System.Windows.Forms.Button();
      this.mPauseButton = new System.Windows.Forms.Button();
      this.mConnectButton = new System.Windows.Forms.Button();
      this.groupBox1.SuspendLayout();
      this.groupBox2.SuspendLayout();
      this.SuspendLayout();
      // 
      // label1
      // 
      this.label1.AutoSize = true;
      this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.label1.Location = new System.Drawing.Point(20, 27);
      this.label1.Name = "label1";
      this.label1.Size = new System.Drawing.Size(87, 17);
      this.label1.TabIndex = 0;
      this.label1.Text = "BrokerURL";
      // 
      // label2
      // 
      this.label2.AutoSize = true;
      this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.label2.Location = new System.Drawing.Point(305, 27);
      this.label2.Name = "label2";
      this.label2.Size = new System.Drawing.Size(81, 17);
      this.label2.TabIndex = 1;
      this.label2.Text = "Username";
      // 
      // label3
      // 
      this.label3.AutoSize = true;
      this.label3.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.label3.Location = new System.Drawing.Point(498, 27);
      this.label3.Name = "label3";
      this.label3.Size = new System.Drawing.Size(77, 17);
      this.label3.TabIndex = 2;
      this.label3.Text = "Password";
      // 
      // mBrokerURLTextBox
      // 
      this.mBrokerURLTextBox.Location = new System.Drawing.Point(113, 24);
      this.mBrokerURLTextBox.Name = "mBrokerURLTextBox";
      this.mBrokerURLTextBox.Size = new System.Drawing.Size(186, 22);
      this.mBrokerURLTextBox.TabIndex = 3;
      this.mBrokerURLTextBox.Text = "tcp://localhost:61616";
      // 
      // mUsernameTextBox
      // 
      this.mUsernameTextBox.Location = new System.Drawing.Point(392, 24);
      this.mUsernameTextBox.Name = "mUsernameTextBox";
      this.mUsernameTextBox.Size = new System.Drawing.Size(100, 22);
      this.mUsernameTextBox.TabIndex = 4;
      this.mUsernameTextBox.Text = "username";
      // 
      // mPasswordTextBox
      // 
      this.mPasswordTextBox.Location = new System.Drawing.Point(581, 24);
      this.mPasswordTextBox.Name = "mPasswordTextBox";
      this.mPasswordTextBox.PasswordChar = '*';
      this.mPasswordTextBox.Size = new System.Drawing.Size(100, 22);
      this.mPasswordTextBox.TabIndex = 5;
      this.mPasswordTextBox.Text = "username";
      // 
      // groupBox1
      // 
      this.groupBox1.Controls.Add(this.label3);
      this.groupBox1.Controls.Add(this.mPasswordTextBox);
      this.groupBox1.Controls.Add(this.label1);
      this.groupBox1.Controls.Add(this.mUsernameTextBox);
      this.groupBox1.Controls.Add(this.label2);
      this.groupBox1.Controls.Add(this.mBrokerURLTextBox);
      this.groupBox1.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
      this.groupBox1.Location = new System.Drawing.Point(3, 3);
      this.groupBox1.Name = "groupBox1";
      this.groupBox1.Size = new System.Drawing.Size(694, 58);
      this.groupBox1.TabIndex = 6;
      this.groupBox1.TabStop = false;
      this.groupBox1.Text = "Broker Configuration";
      // 
      // groupBox2
      // 
      this.groupBox2.Controls.Add(this.mServiceTextBox);
      this.groupBox2.Controls.Add(this.label6);
      this.groupBox2.Controls.Add(this.mProjectTextBox);
      this.groupBox2.Controls.Add(this.label5);
      this.groupBox2.Controls.Add(this.mTopicTextBox);
      this.groupBox2.Controls.Add(this.label4);
      this.groupBox2.Location = new System.Drawing.Point(3, 67);
      this.groupBox2.Name = "groupBox2";
      this.groupBox2.Size = new System.Drawing.Size(694, 58);
      this.groupBox2.TabIndex = 7;
      this.groupBox2.TabStop = false;
      this.groupBox2.Text = "Filter Configuration";
      // 
      // label4
      // 
      this.label4.AutoSize = true;
      this.label4.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.label4.Location = new System.Drawing.Point(18, 29);
      this.label4.Name = "label4";
      this.label4.Size = new System.Drawing.Size(48, 17);
      this.label4.TabIndex = 1;
      this.label4.Text = "Topic";
      // 
      // mTopicTextBox
      // 
      this.mTopicTextBox.Location = new System.Drawing.Point(72, 26);
      this.mTopicTextBox.Name = "mTopicTextBox";
      this.mTopicTextBox.Size = new System.Drawing.Size(102, 22);
      this.mTopicTextBox.TabIndex = 4;
      this.mTopicTextBox.Text = "SomeTopic";
      // 
      // label5
      // 
      this.label5.AutoSize = true;
      this.label5.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.label5.Location = new System.Drawing.Point(180, 29);
      this.label5.Name = "label5";
      this.label5.Size = new System.Drawing.Size(59, 17);
      this.label5.TabIndex = 5;
      this.label5.Text = "Project";
      // 
      // mProjectTextBox
      // 
      this.mProjectTextBox.Location = new System.Drawing.Point(245, 26);
      this.mProjectTextBox.Name = "mProjectTextBox";
      this.mProjectTextBox.Size = new System.Drawing.Size(130, 22);
      this.mProjectTextBox.TabIndex = 6;
      this.mProjectTextBox.Text = "SomeTopic";
      // 
      // label6
      // 
      this.label6.AutoSize = true;
      this.label6.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.label6.Location = new System.Drawing.Point(389, 29);
      this.label6.Name = "label6";
      this.label6.Size = new System.Drawing.Size(62, 17);
      this.label6.TabIndex = 7;
      this.label6.Text = "Service";
      // 
      // mServiceTextBox
      // 
      this.mServiceTextBox.Location = new System.Drawing.Point(457, 26);
      this.mServiceTextBox.Name = "mServiceTextBox";
      this.mServiceTextBox.Size = new System.Drawing.Size(130, 22);
      this.mServiceTextBox.TabIndex = 8;
      this.mServiceTextBox.Text = "SomeTopic";
      // 
      // mImportButton
      // 
      this.mImportButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.mImportButton.Location = new System.Drawing.Point(715, 26);
      this.mImportButton.Name = "mImportButton";
      this.mImportButton.Size = new System.Drawing.Size(125, 40);
      this.mImportButton.TabIndex = 8;
      this.mImportButton.Text = "Import";
      this.mImportButton.UseVisualStyleBackColor = true;
      // 
      // mExportButton
      // 
      this.mExportButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.mExportButton.Location = new System.Drawing.Point(857, 26);
      this.mExportButton.Name = "mExportButton";
      this.mExportButton.Size = new System.Drawing.Size(125, 40);
      this.mExportButton.TabIndex = 9;
      this.mExportButton.Text = "Export";
      this.mExportButton.UseVisualStyleBackColor = true;
      // 
      // mPauseButton
      // 
      this.mPauseButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.mPauseButton.Location = new System.Drawing.Point(715, 77);
      this.mPauseButton.Name = "mPauseButton";
      this.mPauseButton.Size = new System.Drawing.Size(125, 40);
      this.mPauseButton.TabIndex = 10;
      this.mPauseButton.Text = "Pause";
      this.mPauseButton.UseVisualStyleBackColor = true;
      // 
      // mConnectButton
      // 
      this.mConnectButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(255)))), ((int)(((byte)(255)))), ((int)(((byte)(192)))));
      this.mConnectButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
      this.mConnectButton.Location = new System.Drawing.Point(857, 77);
      this.mConnectButton.Name = "mConnectButton";
      this.mConnectButton.Size = new System.Drawing.Size(125, 40);
      this.mConnectButton.TabIndex = 11;
      this.mConnectButton.Text = "Connect";
      this.mConnectButton.UseVisualStyleBackColor = false;
      // 
      // ConfigPanel
      // 
      this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
      this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
      this.BackColor = System.Drawing.SystemColors.ActiveCaption;
      this.Controls.Add(this.mConnectButton);
      this.Controls.Add(this.mPauseButton);
      this.Controls.Add(this.mExportButton);
      this.Controls.Add(this.mImportButton);
      this.Controls.Add(this.groupBox2);
      this.Controls.Add(this.groupBox1);
      this.Name = "ConfigPanel";
      this.Size = new System.Drawing.Size(1000, 132);
      this.groupBox1.ResumeLayout(false);
      this.groupBox1.PerformLayout();
      this.groupBox2.ResumeLayout(false);
      this.groupBox2.PerformLayout();
      this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.Label label1;
    private System.Windows.Forms.Label label2;
    private System.Windows.Forms.Label label3;
    private System.Windows.Forms.TextBox mBrokerURLTextBox;
    private System.Windows.Forms.TextBox mUsernameTextBox;
    private System.Windows.Forms.TextBox mPasswordTextBox;
    private System.Windows.Forms.GroupBox groupBox1;
    private System.Windows.Forms.GroupBox groupBox2;
    private System.Windows.Forms.Label label4;
    private System.Windows.Forms.TextBox mTopicTextBox;
    private System.Windows.Forms.Label label6;
    private System.Windows.Forms.TextBox mProjectTextBox;
    private System.Windows.Forms.Label label5;
    private System.Windows.Forms.TextBox mServiceTextBox;
    private System.Windows.Forms.Button mImportButton;
    private System.Windows.Forms.Button mExportButton;
    private System.Windows.Forms.Button mPauseButton;
    private System.Windows.Forms.Button mConnectButton;
  }
}
