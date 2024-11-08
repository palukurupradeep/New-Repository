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
@Table(name = "claimrgaheader", schema = "clm")
@Data
public class ClaimRgaHeader  extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "claimrgaheaderid")
	private int claimRgaHeaderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimid")
	@JsonIgnore
	private Claim claim;
	
	@OneToMany(mappedBy = "claimRgaHeader", cascade = CascadeType.ALL)
    private List<ClaimRgaLine> claimRgaLine;
	
	@OneToOne(mappedBy = "claimRgaHeader", cascade = CascadeType.ALL)
    private RgaAddress rgaAddress;

	@Column(name = "rganumber")
	private String rgaNumber;

	@Column(name = "customernumber")
	private String customerNumber;

	@OneToOne
	@JoinColumn(name = "claimreasonid", referencedColumnName = "claimreasonid")
	private ClaimReasonDefinition claimReasonDefinition;

	@Column(name = "amountusd")
	private BigDecimal amountUsd = BigDecimal.ZERO;

	@Column(name = "amountforeign")
	private BigDecimal claimAmountForeign = BigDecimal.ZERO;

	@Column(name = "rgastatusid")
	private int rgaStatusId;

	@Column(name = "preparedbyuserid")
	private int preparedByUserId;

	@Column(name = "prepareddate")
	private LocalDateTime preparedDate = LocalDateTime.now();

	@Column(name = "currentuserid")
	private int currentUserId;

	@Column(name = "issuedbyuserid")
	private int issuedByUserId=0;

	@Column(name = "issueddate")
	private LocalDateTime issuedDate = LocalDateTime.now();

	@Column(name = "receiveddate")
	private LocalDateTime receivedDate = LocalDateTime.now();

	@Column(name = "rdclocationid")
	private int rDCLocationId=0;
}
