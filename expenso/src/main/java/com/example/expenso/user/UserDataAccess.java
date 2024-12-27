package com.example.expenso.user;

import com.example.expenso.common.dbUtils.DbUtils;

public class UserDataAccess {
    public UserDetails viewByMobileNumber(String mobileNumber)throws Exception{
        String query = "Select * from userdetails where mobilenumber = ?";
        return new DbUtils().returnedAsObject(query,UserDetails.class,mobileNumber);
    }
}
