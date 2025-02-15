package com.example.expenso.userExpense;

import com.example.expenso.common.commonResponse.CommonResponse;
import com.example.expenso.sms.Sms;
import com.example.expenso.transaction.TransactionDataAccess;
import com.example.expenso.transaction.TransactionInfo;
import org.springframework.util.ObjectUtils;

public class AddExpenseBusinessValidations {
    public CommonResponse addExpenseBVAfterParsing(Sms request,AddUserExpenseResponse addUserExpenseResponse)throws Exception{

        CommonResponse commonResponse = new CommonResponse();

        if(ObjectUtils.isEmpty(request.getReferenceNumber()) || (ObjectUtils.isEmpty(request.getTransferTo()) && ObjectUtils.isEmpty(request.getTransferFrom()))
                || ObjectUtils.isEmpty(request.getTransactionType()) || ObjectUtils.isEmpty(request.getTransactionDate())){
            commonResponse.setCode("401");
            commonResponse.setResponseMessage("Bad Sms not parsed properly");
            commonResponse.setResponseObject(addUserExpenseResponse);
            commonResponse.setValid(false);
            return commonResponse;
        }

        TransactionInfo transactionInfo = new TransactionDataAccess().getByReferenceNo(request.getReferenceNumber());
        if(!ObjectUtils.isEmpty(transactionInfo)){
            addUserExpenseResponse.setRepeat(true);
            commonResponse.setCode("400");
            commonResponse.setResponseMessage("Already Present");
            commonResponse.setResponseObject(addUserExpenseResponse);
            commonResponse.setValid(false);
            return commonResponse;
        }

        return commonResponse;
    }
}
