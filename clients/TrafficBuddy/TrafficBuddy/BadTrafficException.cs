using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Gavaghan.TrafficBuddy
{
  public class BadTrafficException : Exception
  {
    public BadTrafficException(string message)
      : base(message)
    {
    }
  }
}
