using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.IO;
using System.IO.IsolatedStorage;
using System.Text;
using System.Threading;
using System.Windows.Forms;

using Apache.NMS;
using Apache.NMS.Util;
using Apache.NMS.ActiveMQ;
using Apache.NMS.ActiveMQ.Commands;

using log4net;

using Gavaghan.JSON;

namespace Gavaghan.TrafficBuddy
{
  public partial class TopicPanel : UserControl
  {
    /// <summary>
    /// Our logger.
    /// </summary>
    static private readonly ILog LOG = LogManager.GetLogger(typeof(TopicPanel));

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
    private TopicListener mListener;

    /// <summary>
    /// Open connection to a topic.
    /// </summary>
    public void OpenTopic()
    {
      LOG.Info("Topic opening");

      // don't open twice
      if (mOpened) return;

      mOpened = true;

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

        // create the Listener
        mListener = new TopicListener(mMessageConsumer);
        Thread thread = new Thread(new ThreadStart(mListener.Listen));

        mListener.Failed += mListener_Failed;
        mListener.Traffic += mListener_Traffic;

        thread.Start();
      }
      catch (Exception exc)
      {
        CloseTopic();

        StringBuilder builder = new StringBuilder();

        builder.Append("Unable to connect to broker at ").Append(BrokerURL);
        builder.Append("\n\n");
        builder.Append(exc.Message);

        MessageBox.Show(this, builder.ToString(), "Failed to Connect to Broker", MessageBoxButtons.OK, MessageBoxIcon.Error);
      }
    }

    /// <summary>
    /// Close connection to a topic.
    /// </summary>
    public void CloseTopic()
    {
      LOG.Info("Topic closing");

      mOpened = false;

      if (mListener != null)
      {
        mListener.Failed -= mListener_Failed;
        mListener.Traffic -= mListener_Traffic;
      }

      // stop everything
      try
      {
        if (mConnection != null) mConnection.Stop();
        if (mMessageConsumer != null) mMessageConsumer.Close();
        if (mSession != null) mSession.Close();
        if (mConnection != null) mConnection.Close();
      }
      catch (Exception exc)
      {
        LOG.Warn("Unexpected failure closing topic", exc);
      }

      mConnection = null;
      mMessageConsumer = null;
      mSession = null;
    }

    private void mConfigPanel_Connect(object sender, EventArgs e)
    {
      if (mOpened)
      {
        CloseTopic();
        mConfigPanel.setConnected(false);
      }
      else
      {
        OpenTopic();
        if (mOpened) mConfigPanel.setConnected(true);
      }
    }

    /// <summary>
    /// Event to handle failure of Listener.
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="args"></param>
    /// 
    private delegate void ExceptionDelegate(Exception exc);

    public void mListener_Failed(object sender, ListenerFailedEventArgs args)
    {
      CloseTopic();
      Invoke(new ExceptionDelegate(ShowLostConnection), args.Cause);
    }

    private void ShowLostConnection(Exception exc)
    {
      mConfigPanel.setConnected(false);

      StringBuilder builder = new StringBuilder();

      builder.Append("Lost connection to topic '").Append(Topic).Append("' at ").Append(BrokerURL);
      builder.Append("\n\n");
      builder.Append(exc.Message);

      MessageBox.Show(this, builder.ToString(), "Lost Connection to Topic", MessageBoxButtons.OK, MessageBoxIcon.Error);
    }
    #endregion
    #region Load and Save configuration
    public void LoadConfiguration()
    {
      IsolatedStorageFile isoStore = IsolatedStorageFile.GetUserStoreForAssembly();
      JSONObject config = null;

      LOG.Debug("Loading configuration");

      // load the file
      try
      {
        using (IsolatedStorageFileStream isoStream = new IsolatedStorageFileStream("Config.json", FileMode.Open, isoStore))
        {
          if (isoStream == null)
          {
            LOG.Debug("Configuration file does not exist");
          }
          else
          {
            using (StreamReader reader = new StreamReader(isoStream))
            {
              config = JSONValueFactory.DEFAULT.Read(reader) as JSONObject;
            }
          }
        }
      }
      catch (Exception exc)
      {
        LOG.Error("Failure loading configuration file", exc);
      }

      // check the version
      if (config != null)
      {
        if (LOG.IsDebugEnabled) LOG.Debug("Loaded configuration:\n" + config.ToPrettyString());

        JSONNumber version = config["version"] as JSONNumber;
        if (version != null)
        {
          if (!version.Value.Equals(new Decimal(1)))
          {
            LOG.Debug("Version mismatch");
            config = null;
          }
        }
        else
        {
          LOG.Debug("Version not available");
          config = null;
        }
      }

      // copy configuration
      if (config == null)
      {
        LOG.Debug("Using default configuration");
        BrokerURL = "tcp://localhost:61616";
        BrokerUsername = "";
        BrokerPassword = "";
        Topic = "";
        Project = "";
        Service = "";
      }
      else
      {
        BrokerURL = Utils.ToString(config["brokerURL"]);
        BrokerUsername = Utils.ToString(config["username"]);
        BrokerPassword = Utils.ToString(config["password"]);
        Topic = Utils.ToString(config["topic"]);
        Project = Utils.ToString(config["project"]);
        Service = Utils.ToString(config["service"]);
      }
    }

    public void SaveConfiguration()
    {
      JSONObject config = new JSONObject();
      config["version"] = new JSONNumber(1);
      config["brokerURL"] = new JSONString(BrokerURL);
      config["username"] = new JSONString(BrokerUsername);
      config["password"] = new JSONString(BrokerPassword);
      config["topic"] = new JSONString(Topic);
      config["project"] = new JSONString(Project);
      config["service"] = new JSONString(BrokerPassword);

      IsolatedStorageFile isoStore = IsolatedStorageFile.GetUserStoreForAssembly();

      using (IsolatedStorageFileStream isoStream = new IsolatedStorageFileStream("Config.json", FileMode.Create, isoStore))
      {
        using (StreamWriter writer = new StreamWriter(isoStream))
        {
          if (LOG.IsDebugEnabled) LOG.Debug("Saving configuration:\n" + config.ToPrettyString());
          writer.Write(config.ToFlatString());
        }
      }
    }

    private void mConfigPanel_ConfigChanged(object sender, EventArgs e)
    {
      mConfigPanel.getProperties(this);
    }

    private void TopicPanel_VisibleChanged(object sender, EventArgs e)
    {
      if (Visible)
      {
        LOG.Debug("TopicPanel is visible");
        LoadConfiguration();
      }
    }

    private void mConfigPanel_VisibleChanged(object sender, EventArgs e)
    {
      if (mConfigPanel.Visible)
      {
        LOG.Debug("ConfigPanel is visible");
        mConfigPanel.setProperties(this);
        mConfigPanel.setConnected(false);
      }
    }
    #endregion
    #region Traffic Operations
    private JSONArray mRawTraffic = new JSONArray();
    private IList<Transaction> mTraffic = new List<Transaction>();

    public JSONObject RawTraffic
    {
      get
      {
        JSONObject traffic = new JSONObject();

        lock (mRawTraffic)
        {
          traffic.Add("traffic", mRawTraffic.DeepCopy());
        }

        return traffic;
      }
    }

    public void mListener_Traffic(object sender, TrafficReceivedEventArgs args)
    {
      //FIXME apply filters

      LOG.Debug("Got traffic");
      JSONObject traffic = args.Traffic;
      string type = Utils.ToString(traffic["type"]);

      try
      {
        if ("request".Equals(type))
        {
          Request request = new Request(traffic);
          Transaction txn = new Transaction(request);
          mTraffic.Add(txn);
          lock (mRawTraffic)
          {
            ((IList<IJSONValue>)mRawTraffic.Value).Add(traffic);
          }
          LOG.Debug("Request added");
        }
        else if ("response".Equals(type))
        {
          Response response = new Response(traffic);

          for (int i = mTraffic.Count - 1; i >= 0; i--)
          {
            Transaction cand = mTraffic[i];
            if (cand.TxnID == response.TxnID)
            {
              LOG.Debug("Matching request found");
              cand.AddResponse(response);
              lock (mRawTraffic)
              {
                ((IList<IJSONValue>)mRawTraffic.Value).Add(traffic);
              }
            }
          }
        }
      }
      catch (BadTrafficException exc)
      {
        LOG.Warn("Received bad traffic", exc);
      }
    }
    #endregion
  }
}
