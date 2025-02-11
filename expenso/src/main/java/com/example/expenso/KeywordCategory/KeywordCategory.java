package com.example.expenso.KeywordCategory;

import com.example.expenso.common.commonExtend.CommonExtend;

public class KeywordCategory extends CommonExtend {
    private String transferFrom;
    private String keyword;
    private String category;
    private String userId;

    // Getters and Setters

    public String getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(String transferFrom) {
        this.transferFrom = transferFrom;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

