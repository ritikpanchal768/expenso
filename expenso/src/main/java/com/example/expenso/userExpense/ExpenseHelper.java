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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ExpenseHelper {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseHelper.class);
    public CommonResponse<AddUserExpenseResponse> addExpenseRequest(AddUserExpenseRequest request)throws Exception{

//        ************* Declaration ****************
        CommonResponse<AddUserExpenseResponse> commonResponse = new CommonResponse();
        AddUserExpenseResponse addUserExpenseResponse = new AddUserExpenseResponse();
        UserExpense userExpense = new UserExpense();
        TransactionInfo transactionInfo = new TransactionInfo();

//        *********** Fetch User Details **********

        UserDetails userDetails = new UserHelper().viewByMobileNumber(request.getMobileNumber());

//         ************** Parsing SMS ***************

        Sms parsedSms = new SmsHelper().parseSms(request.getSms());
//        ************** Validate request After Parsing ***************
        commonResponse = new AddExpenseBusinessValidations().addExpenseBVAfterParsing(parsedSms,addUserExpenseResponse);
        if(!commonResponse.getValid()){
            logger.info("repeat : {}",addUserExpenseResponse.getRepeat());
            return commonResponse;
        }

//        ************ Cateogry fetching**************

        String category = categorizeExpense(parsedSms.getTransferTo(),parsedSms.getTransferFrom());
        if(!ObjectUtils.isEmpty(category)) userExpense.setCategory(category);

//        ************** Populating transaction info **************
        ExpensoUtils.copyNonNullFields(parsedSms,transactionInfo);
        transactionInfo.setUserId(userDetails.getId());
        transactionInfo.setCreatedBy("SYSTEM");
        if(!StringUtils.isEmpty(request.getTimeStamp())){

            // Convert String to long
            long timestampMillis = Long.parseLong(request.getTimeStamp());
            transactionInfo.setCreatedOn(new Timestamp(timestampMillis));
        }
        else transactionInfo.setCreatedOn(new Timestamp(System.currentTimeMillis()));

//        ************** Populating transaction info **************
        ExpensoUtils.copyNonNullFields(parsedSms,userExpense);
        userExpense.setUserId(userDetails.getId());
        userExpense.setSms(request.getSms());
        userExpense.setCreatedBy("SYSTEM");
        if(!StringUtils.isEmpty(request.getTimeStamp())){
            // Convert String to long
            long timestampMillis = Long.parseLong(request.getTimeStamp());
            userExpense.setCreatedOn(new Timestamp(timestampMillis));
        }
        else userExpense.setCreatedOn(new Timestamp(System.currentTimeMillis()));

//        ********** Save DataBase Details **************

        new DbUtils().saveObject(userExpense,"userExpense");
        new DbUtils().saveObject(transactionInfo,"transactionInfo");

//        ********* Common Response **************
        ExpensoUtils.copyNonNullFields(userExpense,addUserExpenseResponse);
        logger.info("repeat : {}",addUserExpenseResponse.getRepeat());
        if(ObjectUtils.isEmpty(category)){
            commonResponse.setCode("200");
            commonResponse.setResponseMessage("Expense Added Successfully But this is a new Category");
            commonResponse.setResponseObject(addUserExpenseResponse);
        }
        else{
            commonResponse.setCode("200");
            commonResponse.setResponseMessage("Expense Added Successfully");
            commonResponse.setResponseObject(addUserExpenseResponse);
        }

        return commonResponse;
    }

    public CommonResponse<AddUserExpenseResponse> addCashExpenseRequest(AddCashExpenseRequest request)throws Exception{

//        ************* Declaration ****************
        CommonResponse<AddUserExpenseResponse> commonResponse = new CommonResponse();
        AddUserExpenseResponse addUserExpenseResponse = new AddUserExpenseResponse();
        UserExpense userExpense = new UserExpense();
        TransactionInfo transactionInfo = new TransactionInfo();

//        *********** Fetch User Details **********

        UserDetails userDetails = new UserHelper().viewByMobileNumber(request.getMobileNumber());

//        ************ Cateogry fetching**************

        if(!ObjectUtils.isEmpty(request.getCategory())) userExpense.setCategory(request.getCategory());

//        ************** Populating transaction info **************
        String referenceNumber = new ExpensoUtils().generateCashExpenseId("CASH");
        transactionInfo.setAmount(request.getAmount());
        transactionInfo.setTransactionDate(Timestamp.valueOf(request.getTransactionDate()));
        transactionInfo.setInfoFrom("Cash Expense");
        transactionInfo.setUserId(userDetails.getId());
        transactionInfo.setReferenceNumber(referenceNumber);
        transactionInfo.setCreatedOn(new Timestamp(System.currentTimeMillis()));

//        ************** Populating transaction info **************
        userExpense.setUserId(userDetails.getId());
        userExpense.setAmount(request.getAmount());
        userExpense.setCategory(request.getCategory());
        userExpense.setReferenceNumber(referenceNumber);
        userExpense.setCreatedBy(userDetails.getId());
        userExpense.setCreatedOn(new Timestamp(System.currentTimeMillis()));

//        ********** Save DataBase Details **************

        new DbUtils().saveObject(userExpense,"userExpense");
        new DbUtils().saveObject(transactionInfo,"transactionInfo");

//        ********* Common Response **************
        ExpensoUtils.copyNonNullFields(userExpense,addUserExpenseResponse);

        commonResponse.setCode("200");
        commonResponse.setResponseMessage(" Cash Expense Added Successfully");
        commonResponse.setResponseObject(addUserExpenseResponse);


        return commonResponse;
    }
    //       Method to categorize the expense
    private String categorizeExpense(String transferTo,String transferFrom)throws Exception {

        if(transferTo!=null){
            // Lowercase the transferTo to make comparison case-insensitive
            String transferToLower = transferTo.toLowerCase();

//        ********** Get Catogry Based on transferTo from DB*********
            KeywordCategory keywordCategory = new CategoryDataAccess().fetchCategory(transferToLower);
            if(!ObjectUtils.isEmpty(keywordCategory)) return keywordCategory.getCategory();
        }
        else{
            // Lowercase the transferTo to make comparison case-insensitive
            String transferFromLower = transferFrom.toLowerCase();

//        ********** Get Catogry Based on transferTo from DB*********
            KeywordCategory keywordCategory = new CategoryDataAccess().fetchCategory(transferFromLower);
            if(!ObjectUtils.isEmpty(keywordCategory)) return keywordCategory.getCategory();
        }

        return null;
    }

}
