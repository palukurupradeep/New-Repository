package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RGARequestLineDetailsDTO {
    private int claimLineId;
    private String reasonCode;
    private BigDecimal squareFeet;
    private BigDecimal quantity;
    private String productCode;
}
