package com.example.expenso.requestLogging;

import com.example.expenso.common.ExpensoUtils;
import com.example.expenso.common.dbUtils.DbUtils;

import java.sql.Timestamp;

public class RequestLoggingHelper {
    public void requestLogging (Object request, Object response )throws Exception{
        RequestLoggingrequest requestLoggingrequest = new RequestLoggingrequest();
        String requestLogedIn = ExpensoUtils.toJSON(request);
        String responseLogedIn = ExpensoUtils.toJSON(response);
        requestLoggingrequest.setRequest(requestLogedIn);
        requestLoggingrequest.setResponse(responseLogedIn);
        requestLoggingrequest.setCreatedBy("SYSTEM");
        requestLoggingrequest.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        new DbUtils().saveObject(requestLoggingrequest,"requestlogging");
    }
}
