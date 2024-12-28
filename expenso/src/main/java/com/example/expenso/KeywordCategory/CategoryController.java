package com.example.expenso.KeywordCategory;

import com.example.expenso.common.commonResponse.CommonResponse;
import com.example.expenso.userExpense.AddUserExpenseRequest;
import com.example.expenso.userExpense.ExpenseHelper;
import com.example.expenso.userExpense.UserExpense;
import com.example.expenso.userExpense.UserExpenseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController                                                     //Add REST Support
@CrossOrigin
@RequestMapping("/api/v1/category")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @PostMapping("/create/category")
    public CommonResponse addExpense(@RequestBody AddCategoryRequest request)throws Exception {
        logger.info("************* Inside Add Expense Controller ****************");

        CommonResponse commonResponse = new CommonResponse();
        try {
            commonResponse = new CategoryHelper().addCategory(request);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return commonResponse;
    }
}
