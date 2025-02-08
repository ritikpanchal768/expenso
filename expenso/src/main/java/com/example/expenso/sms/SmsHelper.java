package com.example.expenso.sms;

import com.example.expenso.common.Enums.TransactionEnum;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsHelper {
    public Sms parseSms(String sms) throws Exception{
        Sms parsedSms = new Sms();

        if (sms.contains(TransactionEnum.BANK.HDFC.name()) && sms.contains("Sent")){
            parseForHDFC(sms,parsedSms);
        }
        else if(sms.contains(TransactionEnum.BANK.SBI.name())) {
            if(sms.contains("debited")){
                parseForSBI(sms, parsedSms);
            }
            if(sms.contains("credited")){
                if (sms.contains("NEFT")){
                    parseForSalaryCredit(sms, parsedSms);
                }
            }
        }

        return parsedSms;
    }
    public void parseForHDFC(String sms,Sms parsedSms) throws Exception{
        try {
            // Extracting Amount
            String amountRegex = "Sent Rs\\.([0-9]+\\.[0-9]{2})";
            parsedSms.setAmount(new BigDecimal(extractUsingRegex(sms, amountRegex, 1)));

            // Extracting Debited Account Number
            String accountRegex = "From .* A/C x(\\d{4})";
            parsedSms.setDebitedAccountNumber(extractUsingRegex(sms, accountRegex, 1));

            // Extracting Transfer To
            String transferToRegex = "To (.*) On";
            parsedSms.setTransferTo(extractUsingRegex(sms, transferToRegex, 1));

            // Extracting Transaction Date
            String dateRegex = "On (\\d{2}/\\d{2}/\\d{2})";
            String dateStr = extractUsingRegex(sms, dateRegex, 1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            parsedSms.setTransactionDate(new Timestamp(dateFormat.parse(dateStr).getTime()));

            // Extracting Reference Number
            String referenceRegex = "Ref (\\d+)";
            parsedSms.setReferenceNumber(extractUsingRegex(sms, referenceRegex, 1));

            // Setting Additional Fields
            parsedSms.setVia("UPI");
            parsedSms.setBank("HDFC Bank");
            parsedSms.setTransactionType("DEBIT");
            parsedSms.setInfoFrom("SMS");
            parsedSms.setDescription("Rs. "+parsedSms.getAmount() + " Transfered to " + parsedSms.getTransferTo());
            parsedSms.setCreatedOn(new Timestamp(System.currentTimeMillis()));
            parsedSms.setCreatedBy("SYSTEM");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void parseForSBI(String sms,Sms parsedSms) throws Exception {
        try {
            // Patterns for matching SMS content
            String accountPattern = "(A/c|A/C)\\s([A-Za-z0-9]+)(?:-debited)?"; // Account number pattern
            String amountPattern = "(\\d{1,13}\\.\\d{1,2})"; // Updated amount regex
            String transferToPattern = "trf to\\s([\\w\\s-]+?)\\s?Ref";
            // Transfer to (name of the recipient)
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
                String amountStr = amountMatcher.group(1);
                parsedSms.setAmount(new BigDecimal(amountStr));
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

            if(!ObjectUtils.isEmpty(parsedSms.getTransferTo())) parsedSms.setTransactionType("DEBIT");
            parsedSms.setDescription("Rs. "+parsedSms.getAmount() + " Transfered to " + parsedSms.getTransferTo());
            parsedSms.setCreatedOn(new Timestamp(System.currentTimeMillis()));
            parsedSms.setCreatedBy("SYSTEM");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void parseForSalaryCredit(String sms,Sms parsedSms) throws Exception {
        // Regex to extract details from the SMS
        String regex = "INR ([\\d,]+\\.\\d{2}) credited to your A/c No ([\\w]+) on (\\d{2}/\\d{2}/\\d{4}) through (\\w+) with UTR ([\\w]+) by (.*?), INFO: (.*)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sms);

        if (matcher.find()) {
            // Extracted details
            String amountStr = matcher.group(1).replace(",", ""); // Remove commas
            String accountNumber = matcher.group(2);
            String dateStr = matcher.group(3);
            String transactionVia = matcher.group(4);
            String referenceNumber = matcher.group(5);
            String creditedBy = matcher.group(6);
            String info = matcher.group(7);

            // Parsing amount
            BigDecimal amount = new BigDecimal(amountStr);

            // Parsing date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Timestamp transactionDate = new Timestamp(dateFormat.parse(dateStr).getTime());

            // Setting values in parsedSms object
            parsedSms.setAmount(amount);
            parsedSms.setDebitedAccountNumber(accountNumber);
            parsedSms.setTransactionDate(transactionDate);
            parsedSms.setVia(transactionVia);
            parsedSms.setReferenceNumber(referenceNumber);
            parsedSms.setDescription(info);
            parsedSms.setTransferFrom(creditedBy);
            parsedSms.setTransactionType("CREDIT"); // Assuming this type for salary credit
            parsedSms.setBank("SBI");// Bank name extracted directly for simplicity
            parsedSms.setCreatedOn(new Timestamp(System.currentTimeMillis()));
            parsedSms.setCreatedBy("SYSTEM");
        } else {
            throw new Exception("SMS format not recognized or invalid!");
        }
    }

    private static String extractUsingRegex(String text, String regex, int group) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(group); // Return the matching group
        }
        return null; // Return null if no match is found
    }

}
