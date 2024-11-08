package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
public class AssociateDocumentDTO extends BaseEntityDTO{
    private int claimId;
    private String documentType;
    private String documentNumber;
    private String customerNumber;
    private String  documentDate;
    private Optional<BigDecimal> amount;
}
