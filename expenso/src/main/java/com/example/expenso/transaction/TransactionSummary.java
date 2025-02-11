package com.example.expenso.transaction;

import java.math.BigDecimal;

public class TransactionSummary {
    private String category;
    private BigDecimal sum;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
