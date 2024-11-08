package com.shaw.claims.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TicCalculationResponse {

	
	private TicCalculation marchandiseTotal;
	private TicCalculation labor;
	private TicCalculation DealerServiceFees;
	private TicCalculation freightResponse;
	private TicCalculation OtherCharges;
	private TicCalculation salesTax;
	private BigDecimal total;
	

}
