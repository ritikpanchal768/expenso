package com.example.expenso.userExpense;

import com.example.expenso.KeywordCategory.KeywordCategory;
import com.example.expenso.KeywordCategory.CategoryDataAccess;
import com.example.expenso.common.ExpensoUtils;
import com.example.expenso.common.commonResponse.CommonResponse;
import com.example.expenso.common.dbUtils.DbUtils;
import com.example.expenso.sms.Sms;
import com.example.expenso.sms.SmsHelper;
import com.example.expenso.transaction.TransactionInfo;
import com.example.expenso.user.UserDetails;
import com.example.expenso.user.UserHelper;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;

public class ExpenseHelper {
    public CommonResponse<UserExpense> addExpenseRequest(AddUserExpenseRequest request)throws Exception{

//        ************* Declaration ****************
        CommonResponse<UserExpense> commonResponse = new CommonResponse();
        UserExpense userExpense = new UserExpense();
        TransactionInfo transactionInfo = new TransactionInfo();

//        *********** Fetch User Details **********

        UserDetails userDetails = new UserHelper().viewByMobileNumber(request.getMobileNumber());

//         ************** Parsing SMS ***************

        Sms parsedSms = new SmsHelper().parseSms(request.getSms());

//        ************ Cateogry fetching**************

        String category = categorizeExpense(parsedSms.getTransferTo());
        if(!ObjectUtils.isEmpty(category)) userExpense.setCategory(category);

//        ************** Populating transaction info **************
        ExpensoUtils.copyNonNullFields(parsedSms,transactionInfo);
        transactionInfo.setUserId(userDetails.getId());
        transactionInfo.setCreatedBy("SYSTEM");
        transactionInfo.setCreatedOn(new Timestamp(System.currentTimeMillis()));

//        ************** Populating transaction info **************
        ExpensoUtils.copyNonNullFields(parsedSms,userExpense);
        userExpense.setUserId(userDetails.getId());
        userExpense.setSms(request.getSms());
        userExpense.setCreatedBy("SYSTEM");
        userExpense.setCreatedOn(new Timestamp(System.currentTimeMillis()));

//        ********** Save DataBase Details **************

        new DbUtils().saveObject(userExpense,"userExpense");
        new DbUtils().saveObject(transactionInfo,"transactionInfo");

//        ********* Common Response **************
        if(ObjectUtils.isEmpty(category)){
            commonResponse.setCode("200");
            commonResponse.setResponseMessage("Expense Added Successfully But this is a new Category");
            commonResponse.setResponseObject(userExpense);

        }
        else{
            commonResponse.setCode("200");
            commonResponse.setResponseMessage("Expense Added Successfully");
            commonResponse.setResponseObject(userExpense);

        }

        return commonResponse;
    }

    //       Method to categorize the expense
    private String categorizeExpense(String transferTo)throws Exception {
        // Lowercase the transferTo to make comparison case-insensitive
        String transferToLower = transferTo.toLowerCase();

//        ********** Get Catogry Based on transferTo from DB*********
        KeywordCategory keywordCategory = new CategoryDataAccess().fetchCategory(transferToLower);
        if(!ObjectUtils.isEmpty(keywordCategory)) return keywordCategory.getCategory();
        return null;
    }

}
