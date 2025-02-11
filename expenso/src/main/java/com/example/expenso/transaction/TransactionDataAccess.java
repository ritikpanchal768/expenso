package com.example.expenso.transaction;

import com.example.expenso.common.dbUtils.DbUtils;

import java.util.List;

public class TransactionDataAccess {
    public TransactionInfo getByReferenceNo(String referenceNo)throws Exception{
        String query = "Select * from transactionInfo where referenceNumber = ?";
        return new DbUtils().returnedAsObject(query, TransactionInfo.class,referenceNo);
    }
    public List<TransactionDisplay> getByMobileNumber(String mobileNumber)throws Exception{
        String query = "Select DISTINCT t.*,ue.category from transactionInfo t " +
                "join userdetails u on u.id = t.userid " +
                "join userexpense ue on ue.referencenumber = t.referencenumber" +
                " where u.mobileNumber = ? " +
                "order by createdon desc";
        return new DbUtils().returnedAsList(query, TransactionDisplay.class,mobileNumber);
    }
    public List<TransactionSummary> getCategoryWiseTotal(String mobileNumber)throws Exception{
        String query = "SELECT ue.category, SUM(t.amount) FROM transactionInfo t " +
                "JOIN userdetails u ON u.id = t.userid " +
                "JOIN userexpense ue ON ue.referencenumber = t.referencenumber " +
                "WHERE u.mobileNumber = ? and t.transactiontype!='CREDIT'" +
                "AND t.createdon >= (" +
                "SELECT MAX(createdon) FROM transactionInfo " +
                "WHERE userid = u.id AND referencenumber IN " +
                "(SELECT referencenumber FROM userexpense WHERE category = 'Income')) " +
                "GROUP BY ue.category  ORDER BY sum desc \n" ;
        return new DbUtils().returnedAsList(query, TransactionSummary.class,mobileNumber);
    }
}
