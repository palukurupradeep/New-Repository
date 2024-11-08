package com.shaw.claims.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.UnitOfMeasureSerializer;

import jakarta.persistence.CascadeType;
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
@Table(name = "claimrgaline", schema = "clm")
@Data
public class ClaimRgaLine  extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "claimrgalineid")
	private int claimRgaLineId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimrgaheaderid", referencedColumnName = "claimrgaheaderid")
	@JsonIgnore
	private ClaimRgaHeader claimRgaHeader;

	@Column(name = "rgalinenumber")
	private int rgaLineNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimid")
	@JsonIgnore
	private Claim claim;

	@OneToOne
	@JsonIgnore
	@JoinColumn(name = "claimlineid", referencedColumnName = "claimlineid")
	private ClaimLineDetail claimLineDetail;

	
	@OneToOne
	@JsonIgnore
	@JoinColumn(name = "rgastatusid", referencedColumnName = "rgastatusid")
	private RgaStatus rgaStatus;

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

	@Column(name = "issuedwidthinfeet")
	private BigDecimal issuedWidthInFeet = BigDecimal.ZERO;

	@Column(name = "issuedwidthininches")
	private BigDecimal issuedWidthInInches = BigDecimal.ZERO;

	@Column(name = "issuedlengthinfeet")
	private BigDecimal issuedLengthInFeet = BigDecimal.ZERO;

	@Column(name = "issuedlengthininches")
	private BigDecimal issuedLengthInInches = BigDecimal.ZERO;

	@Column(name = "issuedsquarefeet")
	private BigDecimal issuedSquareFeet = BigDecimal.ZERO;

	@Column(name = "issuedsquareyards")
	private BigDecimal issuedSquareYards = BigDecimal.ZERO;

	@Column(name = "issuedquantity")
	private BigDecimal issuedQuantity = BigDecimal.ZERO;

	@JsonSerialize(using = UnitOfMeasureSerializer.class)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issuedunitofmeasureid", referencedColumnName = "unitofmeasureid")
	private UnitOfMeasure unitOfMeasure;

	@Column(name = "issuedamount")
	private BigDecimal issuedAmount = BigDecimal.ZERO;

	@Column(name = "receivedquantity")
	private BigDecimal receivedQuantity = BigDecimal.ZERO;

	@Column(name = "receiveddate")
	private LocalDateTime receivedDate = LocalDateTime.now();

	@Column(name = "receivedinfull")
	private Boolean ReceivedInFull=false;

	@Column(name = "rdclocationid")
	private int rdcLocationId;

	@Column(name = "binlocation")
	private String binLocation = "";

	@Column(name = "carriercode")
	private String carrierCode = "";

	@Column(name = "freightbillnumber")
	private String freightBillNumber = "";

	@Column(name = "freightdate")
	private LocalDateTime freightDate=  LocalDateTime.now();

	@Column(name = "freightamount")
	private BigDecimal freightAmount = BigDecimal.ZERO;

	@Column(name = "freightstatuscode")
	private String freightStatusCode = "";

	@Column(name = "isprepaid")
	private Boolean isPrepaid=false;
}
