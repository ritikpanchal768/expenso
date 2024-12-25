package com.example.expenso.sms;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;


public class Sms {
    private String referenceNumber;
    private Timestamp transactionDate;
    private BigDecimal amount;
    private String description;
    private String via;
    private String bank;
    private String transferTo;
    private String debitedAccountNumber;
    private String transactionType;
    private String category;
    private String infoFrom = "SMS";

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(String transferTo) {
        this.transferTo = transferTo;
    }

    public String getDebitedAccountNumber() {
        return debitedAccountNumber;
    }

    public void setDebitedAccountNumber(String debitedAccountNumber) {
        this.debitedAccountNumber = debitedAccountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInfoFrom() {
        return infoFrom;
    }

    public void setInfoFrom(String infoFrom) {
        this.infoFrom = infoFrom;
    }
}
