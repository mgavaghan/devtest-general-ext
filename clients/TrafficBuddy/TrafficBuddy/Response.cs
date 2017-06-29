using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Gavaghan.JSON;

namespace Gavaghan.TrafficBuddy
{
  public class Response
  {
    public Response(JSONObject response)
    {
      // txnId
      object txnId = Utils.GetNotNull(response["txnId"]);
      if (txnId is Decimal) TxnID = (Decimal)txnId;
      else throw new BadTrafficException("Transaction ID not understood: " + txnId.GetType());
    }

    public decimal TxnID { get; private set; }
  }
}
