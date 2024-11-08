package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InStoreClaimLineDTO {
    private int lineNumber;
    private String rollNumber;
    private String styleNumber;
    private String colorNumber;
    private BigDecimal lineAmountUsd;
    private String dyeLot;
    private String uom;
    private String detailType;
}
