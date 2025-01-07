package com.example.expenso.login;

import com.example.expenso.common.commonResponse.CommonResponse;
import com.example.expenso.user.UserDataAccess;
import com.example.expenso.user.UserDetails;
import com.example.expenso.userExpense.AddUserExpenseRequest;
import com.example.expenso.userExpense.ExpenseHelper;
import com.example.expenso.userExpense.UserExpense;
import com.example.expenso.userExpense.UserExpenseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController                                                     //Add REST Support
@CrossOrigin
@RequestMapping("/api/v1/login")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @PostMapping("/verify/login")
    public CommonResponse<UserDetails> verifyLogin (@RequestBody LoginRequest request)throws Exception {
        logger.info("************* Inside Add Expense Controller ****************");

        CommonResponse<UserDetails> commonResponse = new CommonResponse<>();
        try {
            commonResponse.setResponseObject( new LoginDataAccess().viewByMobileNumberAndPass(request.getMobileNumber(),request.getPasswordHash()));
            if(!ObjectUtils.isEmpty(commonResponse.getResponseObject())){
                commonResponse.setCode("200");
                commonResponse.setResponseMessage("SUCCESSFULLY FETCHED");
            }
            else {
                commonResponse.setCode("404");
                commonResponse.setResponseMessage("NOT FOUND");
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return commonResponse;

    }

}
