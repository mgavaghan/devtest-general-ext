using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Apache.NMS;
using Apache.NMS.Util;
using Apache.NMS.ActiveMQ;
using Apache.NMS.ActiveMQ.Commands;

namespace Prototype
{
  class Program
  {
    static void Main(string[] args)
    {
      string brokerUrl = "tcp://localhost:61616";
      string brokerUsername = "";
      string brokerPassword = "";
      string topicName = "SomeTopic";

      // create a Connection Factory
      ConnectionFactory connFactory = new ConnectionFactory(brokerUrl);
      connFactory.UserName = brokerUsername;
      connFactory.Password = brokerPassword;

      // create a Connection
      IConnection connection = connFactory.CreateConnection();
      connection.Start();

      // create a Session
      ISession session = connection.CreateSession();

      // create the Topic to which messages will be sent
      IDestination topic = session.GetTopic(topicName);

      // create a MessageProducer for sending messages
      IMessageConsumer messageConsumer = session.CreateConsumer(topic);

      while (true)
      {
        IMessage received = messageConsumer.Receive();
        if (received is ITextMessage)
        {
          ITextMessage text = (ITextMessage)received;
          Console.WriteLine(text.Text);
        }
      }

      connection.Stop();
      messageConsumer.Close();
      session.Close();
      connection.Close();


      Console.ReadLine();
    }
  }
}
