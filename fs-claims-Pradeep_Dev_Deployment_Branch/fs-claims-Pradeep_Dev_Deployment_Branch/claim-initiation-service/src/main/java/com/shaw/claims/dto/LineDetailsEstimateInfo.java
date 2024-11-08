package com.shaw.claims.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LineDetailsEstimateInfo {
	private int lineId;
	private int lineNumber;
	private String reasonCode;
	private String style;
	private String color;
	private String creditMemoNo;
	private String creditMemoDate;
	private String resolution;
	private BigDecimal estimateAmount;
	private BigDecimal quantity;
	private String unitOfMeasure;
}
