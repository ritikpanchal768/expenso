package com.example.expenso.transaction;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Setter
@Getter
public class TransactionInfo {
    private String userId;
    private String referenceNumber;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private String description;
    private String via;
    private String bank;
    private String transferTo;
    private String debitedAccountNumber;
    private String transactionType;
    private String infoFrom;
}
