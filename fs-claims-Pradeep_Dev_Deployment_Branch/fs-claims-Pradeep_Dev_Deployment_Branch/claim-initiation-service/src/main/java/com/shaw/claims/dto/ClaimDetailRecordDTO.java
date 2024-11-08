package com.shaw.claims.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class ClaimDetailRecordDTO  extends BaseEntityDTO{
    private int claimDetailRecordId;
    private String detailRecordTypeCode;
    private int widthInFeet;
    private int widthInInches;
    private int lengthInFeet;
    private int lengthInInches;
    private BigDecimal quantity;
    private String unitOfMeasure;
    private BigDecimal amountUsd;
    private BigDecimal amountForeign;
    private BigDecimal unitPriceUsd;
    private BigDecimal unitPriceForeign;
    private BigDecimal exchangeRate;
}
