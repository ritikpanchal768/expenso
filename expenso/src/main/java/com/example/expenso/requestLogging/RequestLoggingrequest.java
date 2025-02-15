package com.example.expenso.requestLogging;

import com.example.expenso.common.commonExtend.CommonExtend;

public class RequestLoggingrequest extends CommonExtend {
    private Object request;
    private Object response;

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
