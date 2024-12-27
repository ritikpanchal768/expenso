package com.example.expenso.user;

import com.example.expenso.common.ExpensoUtils;
import com.example.expenso.common.commonExtend.CommonExtend;
import com.example.expenso.common.commonResponse.CommonResponse;
import com.example.expenso.common.dbUtils.DbUtils;

import java.sql.Timestamp;

public class UserHelper {
    public CommonResponse<UserDetails> addUser(AddUserRequest request) throws Exception {
        CommonResponse<UserDetails> commonResponse = new CommonResponse<>();
        UserDetails userDetails=new UserDetails();
        ExpensoUtils.copyNonNullFields(request,userDetails);
        userDetails.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        new DbUtils().saveObject(userDetails,"userDetails");

        commonResponse.setCode("200");
        commonResponse.setResponseMessage("User OnBoarded Successfullyyyyyyyyyyyy");
        commonResponse.setResponseObject(userDetails);
        return commonResponse;
    }
    public UserDetails viewByMobileNumber(String mobileNumber) throws Exception{
        return new UserDataAccess().viewByMobileNumber(mobileNumber);
    }
}
