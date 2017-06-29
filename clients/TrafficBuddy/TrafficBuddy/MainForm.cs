using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Text;
using System.Windows.Forms;

using Gavaghan.JSON;

namespace Gavaghan.TrafficBuddy
{
  public partial class MainForm : Form
  {
    public MainForm()
    {
      InitializeComponent();
    }

    private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
    {
      mTopicPanel.CloseTopic();
      mTopicPanel.SaveConfiguration();
    }

    private void exitToolStripMenuItem_Click(object sender, EventArgs e)
    {
      Close();
    }

    private void exportTrafficToolStripMenuItem_Click(object sender, EventArgs e)
    {
      DialogResult result = mExportFileDialog.ShowDialog();

      if (result == DialogResult.Cancel) return;

      // FIXME catch exceptions
      JSONObject traffic = mTopicPanel.RawTraffic;

      using (Stream str = new FileStream(mExportFileDialog.FileName, FileMode.Create, FileAccess.Write, FileShare.None))
      using (TextWriter wrt = new StreamWriter(str, Encoding.UTF8))
      {
        wrt.Write(traffic.ToPrettyString());
      }
    }
  }
}