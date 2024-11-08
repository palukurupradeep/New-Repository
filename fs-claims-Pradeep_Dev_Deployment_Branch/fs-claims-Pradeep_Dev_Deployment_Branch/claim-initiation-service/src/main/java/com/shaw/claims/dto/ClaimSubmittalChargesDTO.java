package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClaimSubmittalChargesDTO {
    private String chargeCode;
    private String chargeDescription;
    private String seqNbr;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalCharge;
    private String onClaim;
}
