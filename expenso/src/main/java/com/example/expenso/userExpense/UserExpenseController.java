package com.example.expenso.userExpense;

import com.example.expenso.common.commonResponse.CommonResponse;
import com.example.expenso.requestLogging.RequestLoggingHelper;
import com.example.expenso.sms.Sms;
import com.example.expenso.user.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController                                                     //Add REST Support
@CrossOrigin
@RequestMapping("/api/v1/expense")
public class UserExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(UserExpenseController.class);
    @PostMapping("/create/expense")
    public CommonResponse<AddUserExpenseResponse> addExpense(@RequestBody AddUserExpenseRequest request)throws Exception {
        logger.info("************* Inside Add Expense Controller ****************");

        CommonResponse<AddUserExpenseResponse> commonResponse;
        try {
            commonResponse = new ExpenseHelper().addExpenseRequest(request);
        } catch (Exception e) {
            new RequestLoggingHelper().requestLogging(request,e.toString());
            throw new Exception(e);
        }
        new RequestLoggingHelper().requestLogging(request,commonResponse);
        return commonResponse;

    }
    @PostMapping("/create/cashExpense")
    public CommonResponse<AddUserExpenseResponse> addCashExpense(@RequestBody AddCashExpenseRequest request)throws Exception {
        logger.info("************* Inside Cash Add Expense Controller ****************");

        CommonResponse<AddUserExpenseResponse> commonResponse;
        try {
            commonResponse = new ExpenseHelper().addCashExpenseRequest(request);
        } catch (Exception e) {
            new RequestLoggingHelper().requestLogging(request,e.toString());
            throw new Exception(e);
        }
        new RequestLoggingHelper().requestLogging(request,commonResponse);
        return commonResponse;

    }

}
