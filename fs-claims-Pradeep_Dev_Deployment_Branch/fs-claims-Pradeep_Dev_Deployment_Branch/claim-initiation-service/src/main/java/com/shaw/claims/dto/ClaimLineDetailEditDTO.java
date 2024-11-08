package com.shaw.claims.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ClaimLineDetailEditDTO {
	private int claimLineId;
	private BigDecimal quantity;
	private BigDecimal widthInFeet;
	private BigDecimal lengthInFeet;
	private String dyeLot;
	private String grade;
	private BigDecimal squareFeet;
	private BigDecimal unitPrice;
	private BigDecimal lineAmount;
}
