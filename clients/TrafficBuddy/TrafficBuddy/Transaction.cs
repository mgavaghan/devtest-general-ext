using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Gavaghan.TrafficBuddy
{
  public class Transaction
  {
    public Transaction(Request request)
    {
      TxnID = request.TxnID;
    }

    public void AddResponse(Response response)
    {
    }

    public decimal TxnID { get; private set; }
  }
}
