package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ClaimBatchDetailsDTO {

    private String customerNumber;
    private String claimReasonCode;
    private String invoiceNumber;
    private String invoiceDate;
    private String styleNumber;
    private String colorNumber;
    private BigDecimal claimAmountUsd;
    private String claimNote;
    private int claimId;
    private int creditMemoId;
    private String incentiveId;
    private String associateInvoiceNumber="";
    private String associateInvoiceDate="";
    private BigDecimal quantity=BigDecimal.valueOf(0.0);
    private String unitOfMeasureCode="";
    private BigDecimal unitPriceUsd=BigDecimal.valueOf(0.0);
    private int createdByUserId;
    private int processedByUserId;
    private String createdDateTime ;
    private String processedDateTime;
  
}
