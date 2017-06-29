using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace Gavaghan.TrafficBuddy
{
  public partial class ConfigPanel : UserControl
  {
    public event EventHandler<EventArgs> ConfigChanged;

    private bool mDontFireEvents = true;

    private bool mConnected = false;

    public event EventHandler<EventArgs> Connect;

    public ConfigPanel()
    {
      InitializeComponent();
    }

    public void setProperties(TopicPanel topic)
    {
      mDontFireEvents = true;
      mBrokerURLTextBox.Text = topic.BrokerURL;
      mUsernameTextBox.Text = topic.BrokerUsername;
      mPasswordTextBox.Text = topic.BrokerPassword;
      mTopicTextBox.Text = topic.Topic;
      mProjectTextBox.Text = topic.Project;
      mServiceTextBox.Text = topic.Service;
      mDontFireEvents = false;

      if (ConfigChanged != null) ConfigChanged(this, EventArgs.Empty);
    }

    public void getProperties(TopicPanel topic)
    {
      topic.BrokerURL = mBrokerURLTextBox.Text;
      topic.BrokerUsername = mUsernameTextBox.Text;
      topic.BrokerPassword = mPasswordTextBox.Text;
      topic.Topic = mTopicTextBox.Text;
      topic.Project = mProjectTextBox.Text;
      topic.Service = mServiceTextBox.Text;
    }

    private void property_TextChanged(object sender, EventArgs e)
    {
      if (!mDontFireEvents)
      {
        if (ConfigChanged != null) ConfigChanged(this, e);
      }
    }

    public void setConnected(bool state)
    {
      mConnected = state;

      mBrokerURLTextBox.Enabled = !mConnected;
      mUsernameTextBox.Enabled = !mConnected;
      mPasswordTextBox.Enabled = !mConnected;
      mTopicTextBox.Enabled = !mConnected;
      mProjectTextBox.Enabled = !mConnected;
      mServiceTextBox.Enabled = !mConnected;

      if (mConnected)
      {
        mConnectButton.BackColor = Color.LightGreen;
        mConnectButton.Text = "Disconnect";
      }
      else
      {
        mConnectButton.BackColor = Color.LightYellow;
        mConnectButton.Text = "Connect";
      }
    }

    private void mConnectButton_Click(object sender, EventArgs e)
    {
      if (Connect != null) Connect(this, EventArgs.Empty);
    }
  }
}
