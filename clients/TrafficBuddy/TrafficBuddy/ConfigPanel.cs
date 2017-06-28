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
    public ConfigPanel()
    {
      InitializeComponent();
    }

    public void setProperties(TopicPanel topic)
    {
      mBrokerURLTextBox.Text = topic.BrokerURL;
      mUsernameTextBox.Text = topic.BrokerUsername;
      mPasswordTextBox.Text = topic.BrokerPassword;
      mTopicTextBox.Text = topic.Topic;
      mProjectTextBox.Text = topic.Project;
      mServiceTextBox.Text = topic.Service;
    }
  }
}
