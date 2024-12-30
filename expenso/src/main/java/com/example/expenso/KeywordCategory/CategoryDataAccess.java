package com.example.expenso.KeywordCategory;

import com.example.expenso.common.dbUtils.DbUtils;

public class CategoryDataAccess {
    public KeywordCategory fetchCategory(String transferTo)throws Exception{
        String query ="SELECT * FROM KeywordCategory WHERE ? LIKE CONCAT('%', keyword, '%')";
        return new DbUtils().returnedAsObject(query,KeywordCategory.class,transferTo);
    }
}
