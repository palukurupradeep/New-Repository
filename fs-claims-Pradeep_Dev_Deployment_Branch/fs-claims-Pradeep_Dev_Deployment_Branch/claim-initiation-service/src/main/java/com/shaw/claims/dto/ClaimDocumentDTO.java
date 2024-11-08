package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClaimDocumentDTO extends BaseEntityDTO{
    private int claimDocumentId;
    private int clmId;
    private String documentNumber;
    private BigDecimal exchangeRate;
    private String orderNumber;
    private String purchaseOrderNumber;
    private String billofLading;
    private String masterBillofLading;
    private String documentTypeCode;
    private String claimReasonCode;
    private String invoiceDate;
    private String rdcCity="";
    private String rdcStateCode="";
    private String documentStatusCode = "";
    private String documentStatusDescription = "";
    private List<ClaimLineDetailDTO> claimLineDetailDTOS;
    
}
