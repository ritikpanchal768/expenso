package com.example.expenso.KeywordCategory;

import com.example.expenso.common.commonResponse.CommonResponse;
import com.example.expenso.common.dbUtils.DbUtils;
import com.example.expenso.user.UserDetails;
import com.example.expenso.user.UserHelper;
import com.example.expenso.userExpense.ExpenseDataAccess;
import com.example.expenso.userExpense.UserExpense;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CategoryHelper {
    public CommonResponse addCategory(AddCategoryRequest request)throws Exception{

//        ************* Declaration ************

        CommonResponse commonResponse = new CommonResponse<>();
        KeywordCategory keywordCategory = new KeywordCategory();

//        ************ Populate Category ***********

        keywordCategory.setCategory(request.getCategory());
        if(request.getTransferTo()!=null){
            keywordCategory.setKeyword(request.getTransferTo().toLowerCase());
        }
        else keywordCategory.setKeyword(request.getTransferFrom().toLowerCase());

        keywordCategory.setCreatedBy("SYSTEM");
        keywordCategory.setCreatedOn(new Timestamp(System.currentTimeMillis()));

//        ********** Fetch UserExpense **********

        List<UserExpense> userExpenseList = new ArrayList<>();
        if(request.getTransferTo()!=null){
            userExpenseList = new ExpenseDataAccess().getUserExpenseBytransferTo(request.getTransferTo());
        }
        else{
            userExpenseList = new ExpenseDataAccess().getUserExpenseBytransferFrom(request.getTransferFrom());
        }
        for(UserExpense userExpense : userExpenseList){
            userExpense.setCategory(request.getCategory());
            new DbUtils().updateObject(userExpense,"userExpense", userExpense.getId());
        }

//        ************ User Details ***********
        UserDetails userDetails = new UserHelper().viewByMobileNumber(request.getMobileNumber());
        keywordCategory.setUserId(userDetails.getId());

//        ************* Add Category to Db ***********

        new DbUtils().saveObject(keywordCategory,"keywordCategory");

//        ************ Common Response *************

        commonResponse.setCode("200");
        commonResponse.setResponseMessage("Category Successfully Added");

        return commonResponse;
    }

    public CommonResponse<List<KeywordCategory>> getExistingCategories(String mobileNumber) throws Exception{

        CommonResponse commonResponse = new CommonResponse<>();

        List<KeywordCategory> keywordCategories = new CategoryDataAccess().fetchUserCategories(mobileNumber);
        if(ObjectUtils.isEmpty(keywordCategories)){
            commonResponse.setCode("404");
            commonResponse.setResponseMessage("No Category Mapped for this user");
        }

//        ************ Common Response *************
        commonResponse.setCode("200");
        commonResponse.setResponseMessage("Category Fetched");
        commonResponse.setResponseObject(keywordCategories);

        return commonResponse;
    }
}
