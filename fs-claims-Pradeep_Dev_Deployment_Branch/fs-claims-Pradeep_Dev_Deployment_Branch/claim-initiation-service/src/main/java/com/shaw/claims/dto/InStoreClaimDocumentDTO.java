package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InStoreClaimDocumentDTO {
    private String documentNumber;
    private String documentType;
    private BigDecimal amountUsd;
    private String orderNumber;
    private String purchaseOrderNumber;
    private String billofLading;
    private LocalDateTime documentDate;
    private List<InStoreClaimLineDTO> inStoreClaimLineDTOS;
}
