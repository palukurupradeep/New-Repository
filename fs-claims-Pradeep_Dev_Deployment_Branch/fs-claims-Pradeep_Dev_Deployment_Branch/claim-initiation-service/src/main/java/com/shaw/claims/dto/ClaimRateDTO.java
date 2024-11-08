package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ClaimRateDTO {
    private BigDecimal overallRate;
    private BigDecimal deffectRate;
    private BigDecimal adminRate;
    private BigDecimal overallSalesAmount;
    private BigDecimal overallCreditAmount;
    private BigDecimal deffectCreditAmount;
    private BigDecimal adminCreditAmount;
    public ClaimRateDTO() {}
    public ClaimRateDTO(BigDecimal overallRate, BigDecimal deffectRate, BigDecimal adminRate) {
        this.overallRate = overallRate;
        this.deffectRate = deffectRate;
        this.adminRate = adminRate;
    }
    public ClaimRateDTO(BigDecimal overallSalesAmount, BigDecimal overallCreditAmount, BigDecimal deffectCreditAmount, BigDecimal adminCreditAmount) {
        this.overallSalesAmount = overallSalesAmount;
        this.overallCreditAmount = overallCreditAmount;
        this.deffectCreditAmount = deffectCreditAmount;
        this.adminCreditAmount = adminCreditAmount;
    }
    public ClaimRateDTO(BigDecimal overallRate, BigDecimal deffectRate, BigDecimal adminRate, BigDecimal overallSalesAmount, BigDecimal overallCreditAmount, BigDecimal deffectCreditAmount, BigDecimal adminCreditAmount) {
        this.overallRate = overallRate;
        this.deffectRate = deffectRate;
        this.adminRate = adminRate;
        this.overallSalesAmount = overallSalesAmount;
        this.overallCreditAmount = overallCreditAmount;
        this.deffectCreditAmount = deffectCreditAmount;
        this.adminCreditAmount = adminCreditAmount;
    }
}
