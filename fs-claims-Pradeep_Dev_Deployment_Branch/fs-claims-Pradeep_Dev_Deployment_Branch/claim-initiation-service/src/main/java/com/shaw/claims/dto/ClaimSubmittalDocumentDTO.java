package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ClaimSubmittalDocumentDTO {

    private String documentNumber;
    private BigDecimal exchangeRate;
    private String orderNumber;
    private String purchaseOrderNumber;
    private String billofLading;
    private String masterBillofLading;
    private String documentTypeCode;
    private String claimReasonCode;
    private String reasonCodeDesc;
    private String invoiceDate;
    private String rdcCity;
    private String rdcStateCode;
    private String documentStatusCode;
    private String documentStatusDescription;
    private List<ClaimSubmittalLineDetailDTO> claimLineDetailDTOS;
}
