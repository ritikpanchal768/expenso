package com.example.expenso.KeywordCategory;

import com.example.expenso.common.commonResponse.CommonResponse;
import com.example.expenso.userExpense.AddUserExpenseRequest;
import com.example.expenso.userExpense.ExpenseHelper;
import com.example.expenso.userExpense.UserExpense;
import com.example.expenso.userExpense.UserExpenseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                                                     //Add REST Support
@CrossOrigin
@RequestMapping("/api/v1/category")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @PostMapping("/create/category")
    public CommonResponse addExpense(@RequestBody AddCategoryRequest request)throws Exception {
        logger.info("************* Inside Add Category Controller ****************");

        CommonResponse commonResponse = new CommonResponse();
        try {
            commonResponse = new CategoryHelper().addCategory(request);
            logger.info("Category : {}{}",request.getCategory(),request.getTransferTo());
        } catch (Exception e) {
            throw new Exception(e);
        }
        return commonResponse;
    }

    @GetMapping("/list/{mobileNumber}")
    public CommonResponse<List<KeywordCategory>> getUserExistingCategories(@PathVariable String mobileNumber)throws Exception {
        logger.info("************* Inside fetch Category Controller ****************");

        CommonResponse<List<KeywordCategory>> commonResponse = new CommonResponse<>();
        try {
            commonResponse = new CategoryHelper().getExistingCategories(mobileNumber);

        } catch (Exception e) {
            throw new Exception(e);
        }
        return commonResponse;
    }
}
