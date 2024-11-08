package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ClaimLineDetailDTO  extends BaseEntityDTO{
    private int claimLineId;
    private String unitOfMeasureCode;
    private String rcsCode;
    private int lineNumber;
    private int invoiceLineNumber;
    private String rollNumber;
    private String styleNumber;
    private String colorNumber;
    private String claimReasonCode;
    private String grade;
    private BigDecimal lineAmountUsd;
    private BigDecimal lineAmountForeign;
    private BigDecimal unitPriceUsd;
    private BigDecimal unitPriceForeign;
    private String pricingCurrencyCode;
    private String currencyCodeDesignation;
    private String currencyCodeName;
    private BigDecimal exchangeRate;
    private String dyeLot;
    private BigDecimal quantity;
    private String sellingCompany;
    private String productCode;
    private BigDecimal squareFeet;
    private BigDecimal salesDiscount;
    private String inventoryStyle;
    private String inventoryColor;
    private String manufacturingPlant;
    private String detailTypeCode;
    private String detailStatusTypeCode;
    private String lineSourceCode;
    private String lineSourceReference;
    private String lineAddReasonCode;
    private String vendorId;
    private List<ClaimDetailRecordDTO> claimDetailRecordDTOS;

}
