package com.example.expenso.transaction;

import com.example.expenso.common.dbUtils.DbUtils;

public class TransactionDataAccess {
    public TransactionInfo getByReferenceNo(String referenceNo)throws Exception{
        String query = "Select * from transactionInfo where referenceNumber = ?";
        return new DbUtils().returnedAsObject(query, TransactionInfo.class,referenceNo);
    }
}
