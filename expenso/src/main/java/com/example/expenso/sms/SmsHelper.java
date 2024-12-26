package com.example.expenso.sms;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsHelper {
    public Sms parseSms(String sms) {
        Sms parsedSms = new Sms();
        try {
            // Patterns for matching SMS content
            String accountPattern = "(A/c|A/C)\\s([A-Za-z0-9]+)(?:-debited)?"; // Account number pattern
            String amountPattern = "(Rs?\\s?(\\d+(?:\\.\\d{1,2})?))|([\\d]+(?:\\.\\d{1,2})?)"; // Updated amount regex
            String transferToPattern = "trf to\\s([A-Za-z\\s]+?)\\s?Ref"; // Transfer to (name of the recipient)
            String referencePattern = "(Ref\\s?No\\.?\\s?(\\d+))|(Refno\\s?(\\d+))"; // Reference number pattern (Ref No and Refno)
            String datePattern = "date\\s(\\d{1,2}[A-Za-z]{3}\\d{2})"; // Date pattern
            String bankPattern = "(SBI)"; // Bank name pattern (hardcoded for simplicity)
            String transactionTypePattern = "(debited|credited)"; // Transaction type (debited or credited)
            String viaPattern = "(UPI)"; // Transaction method (UPI)

            // Debug log
            System.out.println("Parsing SMS: " + sms);

            // Account Number
            Matcher accountMatcher = Pattern.compile(accountPattern).matcher(sms);
            if (accountMatcher.find()) {
                parsedSms.setDebitedAccountNumber(accountMatcher.group(2));
                System.out.println("Debited Account Number: " + accountMatcher.group(2));
            }

            // Amount (with Rs or without Rs)
            Matcher amountMatcher = Pattern.compile(amountPattern).matcher(sms);
            if (amountMatcher.find()) {
                String amountStr = amountMatcher.group(2) != null ? amountMatcher.group(2) : amountMatcher.group(3);
                parsedSms.setAmount(new BigDecimal(amountStr)); // Group 2 or 3 for the amount value
                System.out.println("Amount: " + amountStr);
            }

            // Transfer To
            Matcher transferToMatcher = Pattern.compile(transferToPattern).matcher(sms);
            if (transferToMatcher.find()) {
                parsedSms.setTransferTo(transferToMatcher.group(1).trim());
                System.out.println("Transfer To: " + transferToMatcher.group(1));
            }

            // Reference Number (handling both Ref No and Refno)
            Matcher referenceMatcher = Pattern.compile(referencePattern).matcher(sms);
            if (referenceMatcher.find()) {
                String refNumber = referenceMatcher.group(2) != null ? referenceMatcher.group(2) : referenceMatcher.group(4);
                parsedSms.setReferenceNumber(refNumber);
                System.out.println("Reference Number: " + refNumber);
            }

            // Date
            Matcher dateMatcher = Pattern.compile(datePattern).matcher(sms);
            if (dateMatcher.find()) {
                String dateStr = dateMatcher.group(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dMMMyy", Locale.ENGLISH);
                LocalDate parsedDate = LocalDate.parse(dateStr, formatter);
                parsedSms.setTransactionDate(Timestamp.valueOf(parsedDate.atStartOfDay()));
                System.out.println("Transaction Date: " + parsedDate);
            }

            // Bank Name (hardcoded for now as "SBI")
            Matcher bankMatcher = Pattern.compile(bankPattern).matcher(sms);
            if (bankMatcher.find()) {
                parsedSms.setBank(bankMatcher.group(1));
                System.out.println("Bank: " + bankMatcher.group(1));
            }

            // Determine via (UPI or Card)
            Matcher viaMatcher = Pattern.compile(viaPattern).matcher(sms);
            if (viaMatcher.find()) {
                parsedSms.setVia("UPI");
                System.out.println("Via: UPI");
            } else {
                parsedSms.setVia("Card");
                System.out.println("Via: Card");
            }

            // Determine transaction type (debited or credited)
            Matcher transactionTypeMatcher = Pattern.compile(transactionTypePattern).matcher(sms);
            if (transactionTypeMatcher.find()) {
                parsedSms.setTransactionType(transactionTypeMatcher.group(1));
                System.out.println("Transaction Type: " + transactionTypeMatcher.group(1));
            } else {
                parsedSms.setTransactionType("unknown");
                System.out.println("Transaction Type: unknown");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedSms;
    }
}
