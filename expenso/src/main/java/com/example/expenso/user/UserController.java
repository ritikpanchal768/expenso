package com.example.expenso.user;


import com.example.expenso.common.commonResponse.CommonResponse;
import com.example.expenso.requestLogging.RequestLoggingHelper;
import com.example.expenso.sms.Sms;
import com.example.expenso.userExpense.AddUserExpenseRequest;
import com.example.expenso.userExpense.ExpenseHelper;
import com.example.expenso.userExpense.UserExpenseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController                                                     //Add REST Support
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/create/user")
    public CommonResponse<UserDetails> addExpense(@RequestBody AddUserRequest request)throws Exception {
        logger.info("************* Inside Add Expense Controller ****************");

        CommonResponse<UserDetails> commonResponse;
        try {
            commonResponse = new UserHelper().addUser(request);
        } catch (Exception e) {
            new RequestLoggingHelper().requestLogging(request,e.toString());
            throw new Exception(e);
        }
        new RequestLoggingHelper().requestLogging(request,commonResponse);
        return commonResponse;
    }
}
