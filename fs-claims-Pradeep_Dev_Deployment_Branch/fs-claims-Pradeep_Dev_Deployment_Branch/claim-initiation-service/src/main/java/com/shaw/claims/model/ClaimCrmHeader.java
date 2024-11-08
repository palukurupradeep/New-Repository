package com.shaw.claims.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "claimcrmheader", schema = "clm")
@Data
public class ClaimCrmHeader  extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "claimcrmheaderid")
	private int claimCrmHeaderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimid")
	@JsonIgnore
	private Claim claim;
	
	@OneToMany(mappedBy = "claimCrmHeader", cascade = CascadeType.ALL)
    private List<ClaimCrmLine> claimCrmLine;
	
	@Column(name = "crmnumber")
	private String crmNumber;
	
	@Column(name = "claimcustomernumber")
	private String claimCustomerNumber;
	
	@Column(name = "creditcustomernumber")
	private String creditCustomerNumber;
	
	@OneToOne
	@JoinColumn(name = "claimreasonid", referencedColumnName = "claimreasonid")
	private ClaimReasonDefinition claimReasonDefinition;
	
	@Column(name = "amountusd")
	private BigDecimal amountUsd = BigDecimal.ZERO;
	
	@Column(name = "amountforeign")
	private BigDecimal amountForeign = BigDecimal.ZERO;
	
	@Column(name = "crmstatusid")
	private int crmStatusId;
	
	@Column(name = "preparedbyuserid")
	private int preparedByUserId;

	@Column(name = "currentuserid")
	private int currentUserId;
	
	@Column(name = "issuedbyuserid")
	private int issuedByUserId;
	
	@Column(name = "issueddate")
	private LocalDateTime issuedDate = LocalDateTime.now();
	
	@OneToOne
	@JoinColumn(name = "claimdocumentid", referencedColumnName = "claimdocumentid")
	private ClaimDocument claimDocument;
	
	@Column(name = "associatedrganumber")
	private int associatedRgaNumber;

	@Column(name = "associateddebitnumber")
	private String associatedDebitNumber;
	
	@Column(name = "associatedccbnumber")
	private String associatedCcbNumber;
	
	 @Column(name = "showusername")
	 private boolean showUserName;
	 
	 @Column(name = "showinvoice")
	 private boolean showInvoice;
	 
	 @Column(name = "emailaddress")
     private String emailAddress;
	 
	 @Column(name = "territorymanageremail")
     private String territoryManagerEmail;
	 
	 @Column(name = "showtotalonly")
	 private boolean showTotalOnly;
	 
	 @Column(name = "suppressreasoncode")
	 private boolean suppressReasonCode;
	 
	 @Column(name = "autoapply")
	 private boolean autoApply;
	 
	 @Column(name = "donotapply")
	 private boolean doNotApply;
	 
	 @Column(name = "manualapply")
	 private boolean manualApply;
	 
	 @Column(name = "leaveclaimopen")
	 private boolean leaveClaimOpen;
	 
	 @Column(name = "closeclaimwithtasks")
	 private boolean closeClaimWithTasks;
	 
	 @Column(name = "leaveinspectionopen")
	 private boolean leaveInspectionOpen;
	 
	 @Column(name = "removedispute")
	 private boolean removeDispute;
	 
	 @Column(name = "atkattempted")
	 private boolean atkAttempted;
	 
	 @Column(name = "atkaccepted")
	 private boolean atkAccepted;
	 
	 @Column(name = "stkattempted")
	 private boolean stkAttempted;
	 
	 @Column(name = "stkaccepted")
	 private boolean stkAccepted;
	
	 @OneToOne
	 @JoinColumn(name = "servicetokeeptypeid", referencedColumnName = "servicetokeeptypeid")
	 private ServiceToKeepType serviceToKeepType;
	 
	 @Column(name = "generalledgernumber")
     private String generalLedgerNumber;
	 
	 @Column(name = "dispositionneeded")
	 private boolean dispositionNeeded;
}
