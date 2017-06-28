using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

using Apache.NMS;
using Apache.NMS.Util;
using Apache.NMS.ActiveMQ;
using Apache.NMS.ActiveMQ.Commands;

using log4net;

using Gavaghan.JSON;

namespace Gavaghan.TrafficBuddy
{
  /// <summary>
  /// Event args when traffic is received.
  /// </summary>
  public class TrafficReceivedEventArgs : EventArgs
  {
    /// <summary>
    /// Create new TrafficReceivedEventArgs.
    /// </summary>
    /// <param name="traffic"></param>
    public TrafficReceivedEventArgs(JSONObject traffic)
    {
      Traffic = traffic;
    }

    /// <summary>
    /// Get the Traffic
    /// </summary>
    public JSONObject Traffic { get; private set; }
  }

  /// <summary>
  /// Event args when an exception occurs.
  /// </summary>
  public class ListenerFailedEventArgs : EventArgs
  {
    /// <summary>
    /// Create new ListenerFailedEventArgs.
    /// </summary>
    /// <param name="cause"></param>
    public ListenerFailedEventArgs(Exception cause)
    {
      Cause = cause;
    }

    /// <summary>
    /// Get the Traffic
    /// </summary>
    public Exception Cause { get; private set; }
  }

  /// <summary>
  /// Listen for topic messages and raise event on them.
  /// </summary>
  public class TopicListener
  {
    /// <summary>
    /// Our logger.
    /// </summary>
    static private readonly ILog LOG = LogManager.GetLogger(typeof(TopicListener));

    /// <summary>
    /// The consumer to listen to.
    /// </summary>
    private IMessageConsumer mMessageConsumer;

    /// <summary>
    /// Event to indicate traffic received.
    /// </summary>
    public event EventHandler<TrafficReceivedEventArgs> Traffic;

    /// <summary>
    /// Event to indicate an exception occurred.
    /// </summary>
    public event EventHandler<ListenerFailedEventArgs> Failed;

    /// <summary>
    /// Create a new TopicListener.
    /// </summary>
    /// <param name="consumer"></param>
    public TopicListener(IMessageConsumer consumer)
    {
      mMessageConsumer = consumer;
    }

    /// <summary>
    /// Parse a string as JSON.
    /// </summary>
    /// <param name="text"></param>
    /// <returns>'null' if not JSON</returns>
    private JSONObject ParseAsJSON(string text)
    {
      IJSONValue json;

      try
      {
        using (StringReader rdr = new StringReader(text))
        {
          json = JSONValueFactory.DEFAULT.Read(rdr);
        }
      }
      catch (Exception exc)
      {
        json = null;

        LOG.Warn("Failed to parse text as JSON: " + text, exc);
      }

      return json as JSONObject;
    }

    /// <summary>
    /// Get the next topic message.
    /// </summary>
    private void GetNextMessage()
    {
      IMessage message = mMessageConsumer.Receive();
      LOG.Debug("Message received");

      // ensure it's a text message
      if (message is ITextMessage)
      {
        ITextMessage textMessage = (ITextMessage)message;
        string text = textMessage.Text;

        // parse text as JSON
        JSONObject json = ParseAsJSON(text);

        if (json != null)
        {
          if (LOG.IsDebugEnabled) LOG.Debug("Received:\n" + json.ToPrettyString());

          if (Traffic != null) Traffic(this, new TrafficReceivedEventArgs(json));
        }
      }

      // if it's not a text message
      else
      {
        if (message != null)
        {
          LOG.Warn("Incoming message was not a text message: " + message.GetType().ToString());
        }
      }
    }

    /// <summary>
    /// Start listening on the topic.
    /// </summary>
    public void Listen()
    {
      LOG.Info("Started listening");

      try
      {
        while (true) GetNextMessage();
      }
      catch (Exception exc)
      {
        LOG.Info("Listen() caught an exception", exc);

        if (Failed != null) Failed(this, new ListenerFailedEventArgs(exc));
      }

      LOG.Info("Stop listening");
    }
  }
}
