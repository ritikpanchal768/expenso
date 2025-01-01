package com.example.expenso.transaction;

import com.example.expenso.common.dbUtils.DbUtils;

import java.util.List;

public class TransactionDataAccess {
    public TransactionInfo getByReferenceNo(String referenceNo)throws Exception{
        String query = "Select * from transactionInfo where referenceNumber = ?";
        return new DbUtils().returnedAsObject(query, TransactionInfo.class,referenceNo);
    }
    public List<TransactionDisplay> getByMobileNumber(String mobileNumber)throws Exception{
        String query = "Select t.*,ue.* from transactionInfo t " +
                "join userdetails u on u.id = t.userid " +
                "join userexpense ue on ue.referencenumber = t.referencenumber" +
                " where u.mobileNumber = ? " +
                "order by transactiondate desc";
        return new DbUtils().returnedAsList(query, TransactionDisplay.class,mobileNumber);
    }
}
