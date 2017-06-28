using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;

using log4net;
using log4net.Config;

namespace Gavaghan.TrafficBuddy
{
  static class Program
  {
    /// <summary>
    /// Our logger.
    /// </summary>
    static private readonly ILog LOG = LogManager.GetLogger(typeof(Program));

    /// <summary>
    /// The main entry point for the application.
    /// </summary>
    [STAThread]
    static void Main()
    {
      BasicConfigurator.Configure();

      LOG.Info("TrafficBuddy launched");

      Application.EnableVisualStyles();
      Application.SetCompatibleTextRenderingDefault(false);
      Application.Run(new MainForm());
    }
  }
}
