package com.example.expenso.KeywordCategory;

import com.example.expenso.common.dbUtils.DbUtils;

import java.util.List;

public class CategoryDataAccess {
    public KeywordCategory fetchCategory(String transferTo)throws Exception{
        String query ="SELECT * FROM KeywordCategory WHERE ? LIKE CONCAT('%', keyword, '%')";
        return new DbUtils().returnedAsObject(query,KeywordCategory.class,transferTo);
    }
    public List<KeywordCategory> fetchUserCategories(String mobileNumber)throws Exception{
        String query ="SELECT DISTINCT ON (category) * FROM KeywordCategory k WHERE k.userid = (Select id from userdetails where mobilenumber = ?)";
        return new DbUtils().returnedAsList(query,KeywordCategory.class,mobileNumber);
    }
}
