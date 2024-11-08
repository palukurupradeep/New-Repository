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
@Table(name = "claimdispositionheader", schema = "clm")
@Data
public class ClaimDispositionHeader extends BaseEntity  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "claimdispositionheaderid")
	private int claimDispositionHeaderId;
	
	@Column(name = "dispositionnumber")
	private String dispositionNumber;
	
	@OneToMany(mappedBy = "claimDispositionHeader", cascade = CascadeType.ALL)
    private List<ClaimDispositionLine> claimDispositionLine;
	
	@OneToOne
	@JoinColumn(name = "dispositiontypeid", referencedColumnName = "dispositiontypeid")
	private DispositionType dispositionType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimid")
	@JsonIgnore
	private Claim claim;
	
	@OneToOne
	@JoinColumn(name = "claimdocumentid", referencedColumnName = "claimdocumentid")
	private ClaimDocument claimDocument;
	
	@Column(name = "amountusd")
	private BigDecimal amountUsd = BigDecimal.ZERO;
	
	@Column(name = "amountforeign")
	private BigDecimal amountForeign = BigDecimal.ZERO;
	
	@Column(name = "customernumber")
	private String customerNumber;
	
	@Column(name = "companyname")
	private String companyName;
	
	@Column(name = "attentionname")
	private String attentionName;
	
	@Column(name = "phonenumber")
	private String phoneNumber;
	
	@Column(name = "faxnumber")
	private String faxNumber;
	
	@Column(name = "emailaddress")
	private String emailAddress;
	
	@Column(name = "endusername")
	private String endUserName;
	
	 @OneToOne
	 @JoinColumn(name = "notetemplateid", referencedColumnName = "notetemplateid")
	 private NoteTemplate noteTemplate;
	 
	 @OneToOne
	 @JoinColumn(name = "noteid", referencedColumnName = "claimnoteid")
	 private ClaimNote claimNote;
	 
	 @Column(name = "specialinstructions")
	 private String specialInstructions;
	 
	 @OneToOne
	 @JoinColumn(name = "dispositionstatusid", referencedColumnName = "dispositionstatusid")
	 private DispositionStatus dispositionStatus;
	 
	 @Column(name = "issuedbyuserid")
	 private int issuedByUserId;
		
	@Column(name = "issueddate")
	private LocalDateTime issuedDate = LocalDateTime.now();
	 
	 
}

