using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Gavaghan.JSON;

namespace Gavaghan.TrafficBuddy
{
  public class Request
  {
    public Request(JSONObject request)
    {
      // txnId
      object txnId = Utils.GetNotNull(request["txnId"]);
      if (txnId is Decimal) TxnID = (Decimal)txnId;
      else throw new BadTrafficException("Transaction ID not understood: " + txnId.GetType());
    }

    public decimal TxnID { get; private set; }
  }
}
