package com.shaw.claims.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WorkHistoryDTO {
	private String username;
	private String workStatus;
	private String claimNumber;
	private BigDecimal claimAmount;
	private String reasonCode;
	private String customerNumber;
	private String customerName;
	private String city;
	private String state;
	private String zip;
	private String endUserName;
	private LocalDateTime workTime;
}
