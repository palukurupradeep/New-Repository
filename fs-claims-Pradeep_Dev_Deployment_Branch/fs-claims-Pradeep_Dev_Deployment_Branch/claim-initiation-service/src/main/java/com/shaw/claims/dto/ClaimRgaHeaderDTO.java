package com.shaw.claims.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ClaimRgaHeaderDTO extends BaseEntityDTO{
	
	private int claimId;
	private String claimNumber;
	private Integer claimRgaHeaderId;
	private String rgaNumber;
	private String customerNumber;
	private String claimReasonCode;
	private BigDecimal amountUsd;
	private BigDecimal claimAmountForeign;
	private List<ClaimRgaLineDTO> claimRgaLineDTO;
	private RgaAddressDTO rgaAddressDTO;
	private String rgaStatusCode;
	private int issuedByUserId=0;
	private int currentUserId=0;
	private int preparedByUserId=0;
	private String comments;
	private String reasonDesc;
	private LocalDateTime issuedDate ;
	private String sellingCompany;
	private String attentionName;
	private String sellingCompanyName;
	private char sellingCompanyType;
	private String region;
	private String salesPerson;
	private String division;
	private int claimAreaId;
	private String claimAreaCode;
	private boolean sendMailToTm;
	private String[] recipients;
	private boolean sendCorrespondence;
	private String phoneNumber;
	private String faxNumber;
	private String email;
	private String endUserName;
	private LocalDateTime creditMemoDate;
	private String claimName;
	private String claimReasonCodeDescription;
}
