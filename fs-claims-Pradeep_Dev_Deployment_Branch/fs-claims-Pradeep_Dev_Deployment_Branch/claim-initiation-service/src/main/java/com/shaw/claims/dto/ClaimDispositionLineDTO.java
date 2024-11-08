package com.shaw.claims.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ClaimDispositionLineDTO {

	private Integer claimDispositionLineId;
	private int dispositionLineNumber;
	private int claimLineId;
	private String rollNumber = "";
	private String styleNumber = "";
	private String colorNumber = "";
	private String styleName = "";
	private String colorName = "";
	private BigDecimal widthInFeet= BigDecimal.ZERO;
	private BigDecimal widthInInches= BigDecimal.ZERO;
	private BigDecimal lengthInFeet= BigDecimal.ZERO;
	private BigDecimal lengthInInches= BigDecimal.ZERO;
	private BigDecimal squareFeet= BigDecimal.ZERO;
	private BigDecimal squareYards= BigDecimal.ZERO;
	private BigDecimal quantity= BigDecimal.ZERO;
	private BigDecimal amountUsd= BigDecimal.ZERO;
	private BigDecimal amountForeign= BigDecimal.ZERO;
	private int lineNumber;
	private String invNumber="";
	private LocalDateTime invDate;
	private String purchaseOrder;
	private String orderNUmber;
	private String unitOfMeasure;
}
