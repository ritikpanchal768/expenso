package com.example.expenso.userExpense;

import com.example.expenso.common.dbUtils.DbUtils;

import java.util.List;

public class ExpenseDataAccess {
    public List<UserExpense> getUserExpenseBytransferTo(String transferTo) throws Exception{
        String query = "Select u.* From userexpense u " +
                "join transactioninfo t on u.referencenumber = t.referencenumber " +
                "where t.transferto = ?";
        return new DbUtils().returnedAsList(query,UserExpense.class,transferTo);
    }
}
