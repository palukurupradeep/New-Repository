package com.shaw.claims.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WorkQueueDetailsDTO {
	private String claimNumber;
	private Boolean priorityClaim;
	private Boolean jobStop;
	private String workStatus;
	private BigDecimal claimAmount;
	private String claimReasonCode;
	private Long claimAge;
	private Long lastActivityAge;
	private String sellingCompanyId;
	private String customerNumber;
	private String customerName;
	private String stateCode;
	private String endUserName;
	private String globalorNational;
	private LocalDateTime inspectionReportDate;
	private LocalDateTime traceDate;
	private String claimReasonDescription;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private String sellingCompanyName;
	private String stateName;
	private String primaryCustNumber;
	private String userName;
	
}
