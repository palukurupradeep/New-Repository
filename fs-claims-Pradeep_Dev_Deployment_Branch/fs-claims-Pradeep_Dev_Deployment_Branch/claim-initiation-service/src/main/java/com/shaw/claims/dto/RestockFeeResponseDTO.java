package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RestockFeeResponseDTO {

    private String sellingCompanyCode;
    private BigDecimal restockFeePercentage;
    private BigDecimal restockFee;
    private String codeRuleReturnType="";
}
