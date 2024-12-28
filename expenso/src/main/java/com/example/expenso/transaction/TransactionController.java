package com.example.expenso.transaction;

import com.example.expenso.common.commonResponse.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController                                                     //Add REST Support
@CrossOrigin
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping("/view/{referenceNumber}")
    public CommonResponse addExpense(@PathVariable String referenceNumber)throws Exception {
        logger.info("************* Inside Get Transaction By ReferenceNo Controller ****************");
        CommonResponse commonResponse = new CommonResponse();
        try {
            TransactionInfo transactionInfo = new TransactionDataAccess().getByReferenceNo(referenceNumber);
            commonResponse.setCode("200");
            commonResponse.setResponseMessage("Successfully Fetched");
            commonResponse.setResponseObject(transactionInfo);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return commonResponse;
    }
}
