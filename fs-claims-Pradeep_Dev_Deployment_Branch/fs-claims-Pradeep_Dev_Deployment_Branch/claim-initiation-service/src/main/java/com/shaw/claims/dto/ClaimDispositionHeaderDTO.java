package com.shaw.claims.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ClaimDispositionHeaderDTO  extends BaseEntityDTO{

	private int claimId;
	private Integer claimDispositionHeaderId;
	private String dispositionType;
	private String documentNumber;
	private String claimReasonCode;
	private BigDecimal amountUsd;
	private BigDecimal amountForeign;
	private String customerNumber;
	private String companyName;
	private String attentionName;
	private String phoneNumber;
	private String faxNumber;
	private String emailAddress;
	private String endUserName;
	private String specialInstructions;
	private int issuedByUserId;
	private LocalDateTime issuedDate ;
	private String dispositionStatus;
	private List<ClaimDispositionLineDTO> claimDispositionLineDTO;
	private int noteTemplateId;
	private String standardMessage="";
	private String dispositionNumber="";
	
}
