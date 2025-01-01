package com.example.expenso.transaction;

import com.example.expenso.common.commonResponse.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                                                     //Add REST Support
@CrossOrigin
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping("/viewByReferenceNumber/{referenceNumber}")
    public CommonResponse viewByrefernceNumber(@PathVariable String referenceNumber)throws Exception {
        logger.info("************* Inside Get Transaction By ReferenceNo Controller ****************");
        CommonResponse commonResponse = new CommonResponse();
        try {
            TransactionInfo transactionInfo = new TransactionDataAccess().getByReferenceNo(referenceNumber);
            logger.info("transactionInfo.transferTo {}:",transactionInfo.getTransferTo());
            commonResponse.setCode("200");
            commonResponse.setResponseMessage("Successfully Fetched");
            commonResponse.setResponseObject(transactionInfo);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return commonResponse;
    }
    @GetMapping("/viewByMobileNumber/{mobileNumber}")
    public CommonResponse viewByMobileNumber(@PathVariable String mobileNumber)throws Exception {
        logger.info("************* Inside Get Transactions By MobileNumber Controller ****************");
        CommonResponse commonResponse = new CommonResponse();
        try {
            List<TransactionDisplay> transactionDisplays = new TransactionDataAccess().getByMobileNumber(mobileNumber);
            commonResponse.setCode("200");
            commonResponse.setResponseMessage("Successfully Fetched");
            commonResponse.setResponseObject(transactionDisplays);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return commonResponse;
    }
}
