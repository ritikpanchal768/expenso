package com.example.expenso.userExpense;

import com.example.expenso.common.commonResponse.CommonResponse;
import com.example.expenso.sms.Sms;
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
    public CommonResponse<Sms> addExpense(@RequestBody AddUserExpenseRequest request)throws Exception {
        logger.info("************* Inside Add Expense Controller ****************");

        CommonResponse<Sms> commonResponse;
        try {
            commonResponse = new ExpenseHelper().addRequest(request);

        } catch (Exception e) {
            throw new Exception(e);
        }
        return commonResponse;

    }

}
