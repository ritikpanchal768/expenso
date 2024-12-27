package com.example.expenso.userExpense;

import com.example.expenso.common.ExpensoUtils;
import com.example.expenso.common.commonResponse.CommonResponse;
import com.example.expenso.sms.Sms;
import com.example.expenso.sms.SmsHelper;
import com.example.expenso.transaction.TransactionInfo;
import com.example.expenso.user.UserDetails;
import com.example.expenso.user.UserHelper;

public class ExpenseHelper {
    public CommonResponse addRequest(AddUserExpenseRequest request)throws Exception{

        CommonResponse commonResponse = new CommonResponse();
        UserExpense userExpense = new UserExpense();
        TransactionInfo transactionInfo = new TransactionInfo();
//        *********** Fetch User Details **********
        UserDetails userDetails = new UserHelper().viewByMobileNumber(request.getMobileNumber());

//         ************** Parsing SMS ***************

        Sms parsedSms = new SmsHelper().parseSms(request.getSms());

//        ************** Populating Values **************

        ExpensoUtils.copyNonNullFields(parsedSms,transactionInfo);
        ExpensoUtils.copyNonNullFields(parsedSms,userExpense);
        userExpense.setUserId(userDetails.getId());
        userExpense.setCategory(categorizeExpense(parsedSms.getTransferTo()));

        return commonResponse;
    }

    // Method to categorize the expense
    private String categorizeExpense(String transferTo) {
        // Lowercase the transferTo to make comparison case-insensitive
        String transferToLower = transferTo.toLowerCase();

        // Keywords for categorization
        if (transferToLower.contains("swiggy") || transferToLower.contains("zomato") || transferToLower.contains("restaurant")) {
            return "Food and Restaurant";
        } else if (transferToLower.contains("uber") || transferToLower.contains("ola") || transferToLower.contains("travel")) {
            return "Travel";
        } else if (transferToLower.contains("zerodha") || transferToLower.contains("investment")) {
            return "Investment";
        } else if (transferToLower.contains("flipkart") || transferToLower.contains("amazon") || transferToLower.contains("shopping")) {
            return "Shopping";
        } else {
            return "Miscellaneous"; // Default category if no match found
        }
    }

}
