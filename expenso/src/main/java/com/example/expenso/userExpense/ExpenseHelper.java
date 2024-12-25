package com.example.expenso.userExpense;

import com.example.expenso.sms.Sms;
import com.example.expenso.sms.SmsHelper;
import com.example.expenso.transaction.TransactionInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

public class ExpenseHelper {
    public ResponseEntity<Sms> addRequest(AddUserExpenseRequest request)throws Exception{

        UserExpense userExpense = new UserExpense();
        TransactionInfo transactionInfo = new TransactionInfo();
        Sms parsedSms = new SmsHelper().parseSms(request.getSms());
        if(!ObjectUtils.isEmpty(parsedSms.getTransferTo())) parsedSms.setTransactionType("DEBIT");
        // Categorize the expense based on transferTo (recipient)
        parsedSms.setCategory(categorizeExpense(parsedSms.getTransferTo()));
        parsedSms.setDescription("Rs. "+parsedSms.getAmount() + " Transfered to " + parsedSms.getTransferTo());
        return ResponseEntity.ok(parsedSms);
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
