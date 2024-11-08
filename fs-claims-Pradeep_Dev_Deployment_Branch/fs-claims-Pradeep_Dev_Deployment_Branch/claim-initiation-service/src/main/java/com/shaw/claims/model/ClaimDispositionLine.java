package com.shaw.claims.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "claimdispositionline", schema = "clm")
@Data
public class ClaimDispositionLine extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "claimdispositionlineid")
	private int claimDispositionLineId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimdispositionheaderid", referencedColumnName = "claimdispositionheaderid")
	@JsonIgnore
	private ClaimDispositionHeader claimDispositionHeader;
	
	@Column(name = "dispositionlinenumber")
	private int dispositionLineNumber;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimid")
	@JsonIgnore
	private Claim claim;
	
	@OneToOne
	@JsonIgnore
	@JoinColumn(name = "claimlineid", referencedColumnName = "claimlineid")
	private ClaimLineDetail claimLineDetail;
	
	@Column(name = "rollnumber")
	private String rollNumber = "";

	@Column(name = "stylenumber")
	private String styleNumber = "";
	
	@Column(name = "colornumber")
	private String colorNumber = "";
	
	@Column(name = "stylename")
	private String styleName = "";

	@Column(name = "colorname")
	private String colorName = "";
	
	@Column(name = "widthinfeet")
	private BigDecimal widthInFeet = BigDecimal.ZERO;

	@Column(name = "widthininches")
	private BigDecimal widthInInches = BigDecimal.ZERO;
	
	@Column(name = "lengthinfeet")
	private BigDecimal lengthInFeet = BigDecimal.ZERO;

	@Column(name = "lengthininches")
	private BigDecimal lengthInInches = BigDecimal.ZERO;
	
	@Column(name = "squarefeet")
	private BigDecimal squareFeet = BigDecimal.ZERO;

	@Column(name = "squareyards")
	private BigDecimal squareYards = BigDecimal.ZERO;
	
	@Column(name = "quantity")
	private BigDecimal quantity = BigDecimal.ZERO;
	
	@Column(name = "amountusd")
	private BigDecimal amountUsd = BigDecimal.ZERO;
	
	@Column(name = "amountforeign")
	private BigDecimal amountForeign = BigDecimal.ZERO;
}
