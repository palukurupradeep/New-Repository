package com.shaw.claims.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ClaimCrmHeaderDTO extends BaseEntityDTO{

	private int claimId;
	private Integer claimCrmHeaderId;
	private String claimCustomerNumber;
	private String creditCustomerNumber;
	private String claimReasonCode;
	private BigDecimal amountUsd;
	private BigDecimal amountForeign;
	private List<ClaimCrmLineDTO> claimCrmLineDTO;
	private String crmStatusCode;
	private int preparedByUserId=0;
	private int currentUserId;
	private int issuedByUserId;
	private int associatedRgaNumber;
	private LocalDateTime issuedDate ;
	private String associatedDebitNumber="";
	private String associatedCcbNumber="";
	private boolean showUserName;
	private boolean showInvoice;
	private String emailAddress="";
	private String territoryManagerEmail="";
	private boolean showTotalOnly;
	private boolean suppressReasonCode;
	private boolean autoApply;
	private boolean doNotApply;
	private boolean manualApply;
	private boolean leaveClaimOpen;
	private boolean closeClaimWithTasks;
	private boolean leaveInspectionOpen;
	private boolean removeDispute;
	private boolean atkAttempted;
	private boolean atkAccepted;
	private boolean stkAttempted;
	private boolean stkAccepted;
	private String generalLedgerNumber="";
	private boolean dispositionNeeded;
	private int serviceToKeepTypeId;
	private String comments;
	private boolean sendEmailToTM;
	private LocalDateTime creditMemoDate;
	private String claimNumber;
	private int claimsArea;
	private int claimsAreaType;
	private String userName;
	private String customerNumber;
	private String customerDept="";
	private String div;
	private String salesPerson;
	private String crmNum;
	private String customerOrderNumber;
	
}
