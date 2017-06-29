using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Gavaghan.JSON;

namespace Gavaghan.TrafficBuddy
{
  public class Utils
  {
    static Utils()
    {
    }

    static public string ToString(IJSONValue json)
    {
      if (json == null) return "";

      object value = json.Value;

      string retval = "";

      if (value != null) retval = value.ToString();

      return retval;
    }

    static public object GetNotNull(IJSONValue json)
    {
      if (json == null) throw new BadTrafficException("'null' not allowed");
      return json.Value;
    }
  }
}
