package com.shaw.claims.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LaborRateDTO {

	private int claimLaborRateId;
	
	private String claimLaborRateCode;
	
	private String claimLaborRateDescription;
	
	private BigDecimal rate;
	
	private int status;
}
