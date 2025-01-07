package com.example.expenso.login;

import com.example.expenso.common.dbUtils.DbUtils;
import com.example.expenso.user.UserDetails;

public class LoginDataAccess {
    public UserDetails viewByMobileNumberAndPass(String mobileNumber, String password)throws Exception{
        String query = "Select * from userdetails where mobilenumber = ? and passwordhash = ?";
        return new DbUtils().returnedAsObject(query,UserDetails.class,mobileNumber,password);
    }
}
