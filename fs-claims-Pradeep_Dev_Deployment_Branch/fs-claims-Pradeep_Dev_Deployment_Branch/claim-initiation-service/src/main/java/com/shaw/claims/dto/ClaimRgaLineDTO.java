package com.shaw.claims.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ClaimRgaLineDTO {

	private Integer claimRgaLineId;
	private int rgaLineNumber;
	private int invoiceLineNumber;
	private int claimLineId;
	private int lineNumber;
	private String rollNumber = "";
	private String styleNumber = "";
	private String colorNumber = "";
	private String styleName = "";
	private String colorName = "";
	private BigDecimal issuedWidthInFeet= BigDecimal.ZERO;
	private BigDecimal issuedLengthInFeet= BigDecimal.ZERO;
	private BigDecimal issuedWidthInInches= BigDecimal.ZERO;
	private BigDecimal issuedLengthInInches= BigDecimal.ZERO;
	private BigDecimal issuedSquareFeet= BigDecimal.ZERO;
	private BigDecimal issuedSquareYards= BigDecimal.ZERO;
	private BigDecimal issuedQuantity= BigDecimal.ZERO;
	private String unitOfMeasureCode;
	private BigDecimal issuedAmount= BigDecimal.ZERO;
	private int rdcLocationId;
	private String carrierCode = "";
	private String freightBillNumber = "";
	private LocalDateTime freightDate=  LocalDateTime.now();
	private BigDecimal freightAmount= BigDecimal.ZERO;
	private Boolean isPrepaid=false;
	private BigDecimal receivedQuantity = BigDecimal.ZERO;
	private LocalDateTime receivedDate = LocalDateTime.now();
	private Boolean ReceivedInFull=false;
	private String binLocation = "";
	private String type="";
	private String invNumber="";
	private LocalDateTime invDate;
	private String lot="";
	private String grade="";
	private BigDecimal price= BigDecimal.ZERO;
	private String reasonCode = "";
	private BigDecimal claimLineAmountUsd = BigDecimal.ZERO;
	private String bolNumber;
	private String poNumber;
	private String orderNumber;
	private BigDecimal quantity = BigDecimal.ZERO;
	private BigDecimal claimLineForeignAmountUsd = BigDecimal.ZERO;
	private String reasonCodeDescription;
	private BigDecimal lineAmount= BigDecimal.ZERO;
	private BigDecimal lineForeignAmountUsd = BigDecimal.ZERO;
			
	private String locationCode="";
}
