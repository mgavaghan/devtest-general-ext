using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;

using Apache.NMS;
using Apache.NMS.Util;
using Apache.NMS.ActiveMQ;
using Apache.NMS.ActiveMQ.Commands;

namespace Gavaghan.TrafficBuddy
{
  public partial class TopicPanel : UserControl
  {
    public TopicPanel()
    {
      InitializeComponent();
    }

    #region Properties
    /// <summary>Broker URL</summary>
    public string BrokerURL { get; set; }

    /// <summary>Broker Username</summary>
    public string BrokerUsername { get; set; }

    /// <summary>Broker Password</summary>
    public string BrokerPassword { get; set; }

    /// <summary>opic to listen to</summary>
    public string Topic { get; set; }

    /// <summary>Project to filer on</summary>
    public string Project { get; set; }

    /// <summary>Service to filter on</summary>
    public string Service { get; set; }
    #endregion
    #region Open and Close Topic
    private bool mOpened = false;
    private IConnection mConnection;
    private ISession mSession;
    private IMessageConsumer mMessageConsumer;

    /// <summary>
    /// Open connection to a topic.
    /// </summary>
    public void OpenTopic()
    {
      // don't open twice
      if (mOpened) return;

      try
      {
        // create a Connection Factory
        ConnectionFactory connFactory = new ConnectionFactory(BrokerURL);
        connFactory.UserName = BrokerUsername;
        connFactory.Password = BrokerPassword;

        // create a Connection
        mConnection = connFactory.CreateConnection();
        mConnection.Start();

        // create a Session
        mSession = mConnection.CreateSession();

        // create the Topic to which messages will be sent
        IDestination topic = mSession.GetTopic(Topic);

        // create a MessageProducer for sending messages
        mMessageConsumer = mSession.CreateConsumer(topic);

        mOpened = true;
      }
      catch (Exception exc)
      {
        CloseTopic();

        StringBuilder builder = new StringBuilder();

        builder.Append("Unable to connect to broker at ").Append(BrokerURL);
        builder.Append("\n\n");
        builder.Append(exc.Message);

        MessageBox.Show(builder.ToString(), "Failed to Connect to Broker", MessageBoxButtons.OK, MessageBoxIcon.Error);
      }
    }

    /// <summary>
    /// Close connection to a topic.
    /// </summary>
    public void CloseTopic()
    {
      mOpened = false;

      // stop everything
      try
      {
        if (mConnection != null) mConnection.Stop();
        if (mMessageConsumer != null) mMessageConsumer.Close();
        if (mSession != null) mSession.Close();
        if (mConnection != null) mConnection.Close();
      }
      catch
      {
        // TODO log this
      }

      mConnection = null;
      mMessageConsumer = null;
      mSession = null;
    }
    #endregion

    /// <summary>
    /// Open topic on visibility.
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    private void mTrafficPanel_VisibleChanged(object sender, EventArgs e)
    {
      OpenTopic();
    }
  }
}
